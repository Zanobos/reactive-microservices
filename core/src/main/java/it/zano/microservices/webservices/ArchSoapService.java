package it.zano.microservices.webservices;

import it.zano.microservices.exception.MicroServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.StopWatch;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.axiom.AxiomSoapMessageFactory;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;

import javax.annotation.PostConstruct;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * @author a.zanotti
 * @since 16/02/2018
 */
public abstract class ArchSoapService extends WebServiceGatewaySupport {

    protected static final Logger log = LoggerFactory.getLogger(ArchSoapService.class);

    protected static final String SOAP_OK = "{}, SOAP executed OK in {} ms";
    protected static final String SOAP_KO = "{}, SOAP failed KO in {} ms";

    protected enum AuthType {
        USERNAME_TOKEN("UsernameToken"),
        BASIC("basic"),
        MUTUAL("mutual"),
        NONE("none"),
        UNKNOWN("");

        private String value;

        AuthType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static AuthType fromString(String value) {
            return Arrays.stream(values())
                    .filter(v -> v.value.equalsIgnoreCase(value))
                    .findFirst()
                    .orElse(UNKNOWN);
        }
    }

    protected String name;
    protected String endpoint;
    protected String contextPath;
    protected String authType;
    protected String username;
    protected String password;
    protected String certificateName;
    protected String certificatePassword;

    /** Connection and read timeout, in seconds. */
    protected Integer timeout;

    @PostConstruct
    public void init() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(contextPath);
        marshaller.setMtomEnabled(true);
        this.setMarshaller(marshaller);
        this.setUnmarshaller(marshaller);
        this.setDefaultUri(endpoint);
        this.setMessageFactory(new AxiomSoapMessageFactory());
        List<ClientInterceptor> interceptorList = new ArrayList<>();
        switch (AuthType.fromString(authType)) {
            case BASIC: {
                this.setMessageSender(new HttpUrlConnectionMessageSender() {
                    @Override
                    protected void prepareConnection(HttpURLConnection connection) throws IOException {
                        Base64.Encoder enc = Base64.getEncoder();
                        String userpassword = username + ":" + password;
                        String encodedAuthorization = enc.encodeToString(userpassword.getBytes());
                        connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
                        super.prepareConnection(connection);
                    }
                });
            }
            break;
            case USERNAME_TOKEN: {
                Wss4jSecurityInterceptor wss4jSecurityInterceptor = new Wss4jSecurityInterceptor();
                wss4jSecurityInterceptor.setSecurementActions(AuthType.USERNAME_TOKEN.getValue());
                wss4jSecurityInterceptor.setSecurementUsername(username);
                wss4jSecurityInterceptor.setSecurementPassword(password);
                interceptorList.add(wss4jSecurityInterceptor);
            }
            break;
            case MUTUAL: {
                if (timeout != null) {
                    int httpTimeout = timeout * 1000;
                    HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender();
                    messageSender.setConnectionTimeout(httpTimeout);
                    messageSender.setReadTimeout(httpTimeout);
                    setMessageSender(messageSender);
                }
                Wss4jSecurityInterceptor wss4jSecurityInterceptor = new Wss4jSecurityInterceptor();
                wss4jSecurityInterceptor.setSecurementActions("Signature");
                wss4jSecurityInterceptor.setSecurementSignatureKeyIdentifier("DirectReference");
                wss4jSecurityInterceptor.setSecurementUsername(certificateName);
                wss4jSecurityInterceptor.setSecurementPassword(certificatePassword);
                wss4jSecurityInterceptor.setValidationActions("Signature");
                interceptorList.add(wss4jSecurityInterceptor);
            }
            break;
            case NONE:
            default:
                // Unknown auth type
                break;
        }

        interceptorList.add(new ArchSoapLoggingInterceptor(name));
        this.setInterceptors(interceptorList.toArray(new ClientInterceptor[interceptorList.size()]));
    }

    protected <REQ, RES> RES call(REQ request) throws MicroServiceException {
        StopWatch stopWatchSoapInvocation = new StopWatch();
        log.info("{}, executing SOAP call to URI: {} ", name, endpoint);
        stopWatchSoapInvocation.start();
        Object response = null;
        try {
            response = getWebServiceTemplate().marshalSendAndReceive(endpoint, request);
            stopWatchSoapInvocation.stop();
            log.info(SOAP_OK, name, stopWatchSoapInvocation.getLastTaskTimeMillis());
        } catch (Exception e) {
            stopWatchSoapInvocation.stop();
            log.error(SOAP_KO,name,stopWatchSoapInvocation.getLastTaskTimeMillis());
            throw new MicroServiceException(e);
        }
        return (RES) response;
    }

    /**
     * This overload of the call method adds a SOAP Header.
     *
     * @param request
     * @param soapHeader
     * @param <REQ>
     * @param <RES>
     * @param <HEADER>
     * @return
     * @throws MicroServiceException
     */
    protected <REQ, RES, HEADER> RES call(REQ request, JAXBElement<HEADER> soapHeader) throws MicroServiceException {
        StopWatch stopWatchSoapInvocation = new StopWatch();
        log.info("{}, executing SOAP call (with SOAP header) to URI: {} ", name, endpoint);
        stopWatchSoapInvocation.start();
        Object response = null;
        try {
            response = getWebServiceTemplate().marshalSendAndReceive(endpoint, request, webServiceMessage -> {
                // Retrieve SOAP message
                SoapMessage soapMessage = (SoapMessage)webServiceMessage;
                // Get the SOAP header entry in the message
                SoapHeader header = soapMessage.getSoapHeader();
                // Marshal the supplied header to a string
                StringResult marshaledHeaderResult = new StringResult();
                getMarshaller().marshal(soapHeader, marshaledHeaderResult);
                // Create a source for the transformer
                StringSource marshaledHeaderSource = new StringSource(marshaledHeaderResult.toString());
                // Create a transformer factory
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                // Secure the transformer factory
                transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
                // Create the transformer
                Transformer transformer = transformerFactory.newTransformer();
                // Transform
                transformer.transform(marshaledHeaderSource, header.getResult());
            });
            stopWatchSoapInvocation.stop();
            log.info(SOAP_OK, name, stopWatchSoapInvocation.getLastTaskTimeMillis());
        } catch (Exception e) {
            stopWatchSoapInvocation.stop();
            log.error(SOAP_KO,name,stopWatchSoapInvocation.getLastTaskTimeMillis());
            throw new MicroServiceException(e);
        }
        return (RES) response;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getAuthType() {
        return authType;
    }

    public void setAuthType(String authType) {
        this.authType = authType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public String getCertificatePassword() {
        return certificatePassword;
    }

    public void setCertificatePassword(String certificatePassword) {
        this.certificatePassword = certificatePassword;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }
}
