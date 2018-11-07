package it.zano.microservices.webservices;

import it.zano.microservices.util.LogMaskUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptorAdapter;
import org.springframework.ws.context.MessageContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ArchSoapLoggingInterceptor extends ClientInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(ArchSoapLoggingInterceptor.class);
    private static final int DEFAULT_MAX_LENGTH = 300000;

    private String serviceName;
    private String endpoint;
    private int maxLength;


    ArchSoapLoggingInterceptor(String serviceName, String endpoint, Integer maxLoggingLength) {
        this.serviceName = serviceName;
        this.endpoint = endpoint;
        this.maxLength = maxLoggingLength != null ? maxLoggingLength : DEFAULT_MAX_LENGTH;
    }


    @Override
    public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {
        String message = extractMessage(messageContext.getRequest());
        logger.info("{}, uri: {}, request: {}", serviceName, endpoint, LogMaskUtils.maskFields(message));
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
        String message = extractMessage(messageContext.getResponse());
        logger.info("{}, response: {}", serviceName, LogMaskUtils.maskFields(message));
        return true;
    }

    @Override
    public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
        String message = extractMessage(messageContext.getResponse());
        logger.info("{}, fault response: {}", serviceName, LogMaskUtils.maskFields(message));
        return true;
    }


    private String extractMessage(WebServiceMessage soapMessage) {
        String message;
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            soapMessage.writeTo(stream);
            int size = stream.size();
            if(size <= maxLength)
                message = stream.toString(StandardCharsets.ISO_8859_1.name());
            else
                message = "[Request too long: "+ size +" bytes]";
        } catch (IOException e) {
            logger.error("Cannot parse message - {} ", e.getMessage());
            message = "[Cannot parse message]";
        }
        return message;
    }
}
