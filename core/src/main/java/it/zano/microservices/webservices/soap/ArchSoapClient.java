package it.zano.microservices.webservices.soap;

import it.zano.microservices.exception.MicroServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.util.StopWatch;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.axiom.AxiomSoapMessageFactory;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBElement;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * @author a.zanotti
 * @since 16/02/2018
 */
public abstract class ArchSoapClient extends WebServiceGatewaySupport {

    private static final Logger log = LoggerFactory.getLogger(ArchSoapClient.class);

    private static final String SOAP_PERFORMANCE = "{}, soap call to uri {} done in {} ms";

    private ArchSoapClientProperties properties;

    public ArchSoapClient(ArchSoapClientProperties properties) {
        super();
        this.properties = properties;

        //Setting the marshaller
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(properties.getContextPath());
        marshaller.setMtomEnabled(properties.isMtomEnabled());
        setMarshaller(marshaller);
        setUnmarshaller(marshaller);

        //Setting default uri
        setDefaultUri(properties.getEndpoint());

        //Setting message factory
        SoapVersion soapVersion = ( properties.getSoapVersion() != null && properties.getSoapVersion().equals("1.2") )
                ? SoapVersion.SOAP_12 : SoapVersion.SOAP_11;
        AxiomSoapMessageFactory axiomSoapMessageFactory = new AxiomSoapMessageFactory();
        axiomSoapMessageFactory.setSoapVersion(soapVersion);
        setMessageFactory(axiomSoapMessageFactory);

        //Preparing the interceptors for logging and security
        List<ClientInterceptor> interceptorList = new ArrayList<>();
        interceptorList.add(new ArchSoapLoggingInterceptor(properties.getName(),
                properties.getEndpoint(), properties.getMaxLoggingLength()));

        switch (properties.getAuthType()) {
            case BASIC: {
                this.setMessageSender(new HttpUrlConnectionMessageSender() {
                    @Override
                    protected void prepareConnection(HttpURLConnection connection) throws IOException {
                        Base64.Encoder enc = Base64.getEncoder();
                        String userpassword = properties.getUsername() + ":" + properties.getPassword();
                        String encodedAuthorization = enc.encodeToString(userpassword.getBytes());
                        connection.setRequestProperty("Authorization", "Basic " + encodedAuthorization);
                        super.prepareConnection(connection);
                    }
                });
            }
            break;
            case USERNAME_TOKEN: {
                Wss4jSecurityInterceptor wss4jSecurityInterceptor = new Wss4jSecurityInterceptor();
                wss4jSecurityInterceptor.setSecurementActions("UsernameToken");
                wss4jSecurityInterceptor.setSecurementUsername(properties.getUsername());
                wss4jSecurityInterceptor.setSecurementPassword(properties.getPassword());
                interceptorList.add(wss4jSecurityInterceptor);
            }
            break;
            case MUTUAL: {
                if (properties.getTimeout() != null) {
                    int httpTimeout = properties.getTimeout() * 1000;
                    HttpComponentsMessageSender messageSender = new HttpComponentsMessageSender();
                    messageSender.setConnectionTimeout(httpTimeout);
                    messageSender.setReadTimeout(httpTimeout);
                    setMessageSender(messageSender);
                }
                Wss4jSecurityInterceptor wss4jSecurityInterceptor = new Wss4jSecurityInterceptor();
                wss4jSecurityInterceptor.setSecurementActions("Signature");
                wss4jSecurityInterceptor.setSecurementSignatureKeyIdentifier("DirectReference");
                wss4jSecurityInterceptor.setSecurementUsername(properties.getCertificateName());
                wss4jSecurityInterceptor.setSecurementPassword(properties.getCertificatePassword());
                wss4jSecurityInterceptor.setValidationActions("Signature");
                interceptorList.add(wss4jSecurityInterceptor);
            }
            break;
            case NONE:
            case UNKNOWN:
            default:
                // Unknown auth type
                break;
        }

        setInterceptors(interceptorList.toArray(new ClientInterceptor[interceptorList.size()]));

    }

    protected <REQ, RES> RES call(REQ request) throws MicroServiceException {
        return call(request, null);
    }

    protected <REQ, RES, HEADER> RES call(REQ request, JAXBElement<HEADER> soapHeader) throws MicroServiceException {
        StopWatch stopWatchSoapInvocation = new StopWatch();
        stopWatchSoapInvocation.start();
        Object response;
        try {
            WebServiceMessageCallback requestCallback;
            //Add soap header only if needed
            if(properties.isCustomSoapHeader()) {
                requestCallback = createRequestCallback(soapHeader, properties);
            } else {
                requestCallback = null;
            }
            response = getWebServiceTemplate().marshalSendAndReceive(request, requestCallback);
            stopWatchSoapInvocation.stop();
            log.info(SOAP_PERFORMANCE, properties.getName(), properties.getEndpoint(),
                                stopWatchSoapInvocation.getLastTaskTimeMillis());
        } catch (Exception e) {
            stopWatchSoapInvocation.stop();
            log.info(SOAP_PERFORMANCE, properties.getName(), properties.getEndpoint(),
                    stopWatchSoapInvocation.getLastTaskTimeMillis());
            throw new MicroServiceException(e);
        }
        return (RES) response;
    }

    private <HEADER> WebServiceMessageCallback createRequestCallback(JAXBElement<HEADER> soapHeader, ArchSoapClientProperties properties) {
        return webServiceMessage -> {
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
            transformerFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, properties.isSecureProcessingInSoapHeader());
            // Create the transformer
            Transformer transformer = transformerFactory.newTransformer();
            // Transform
            transformer.transform(marshaledHeaderSource, header.getResult());
        };
    }

}
