package it.zano.microservices.webservices.rest;

import it.zano.microservices.util.LogMaskUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @author a.zanotti
 * @since 14/05/2018
 */
public class ArchRestLoggingInterceptor implements ClientHttpRequestInterceptor {

    private final Logger logger = LoggerFactory.getLogger(ArchRestLoggingInterceptor.class);
    private static final int DEFAULT_MAX_LENGTH = 300000;

    private String serviceName;
    private int maxLength;


    ArchRestLoggingInterceptor(String serviceName, Integer maxLoggingLength) {
        this.serviceName = serviceName;
        this.maxLength = maxLoggingLength != null ? maxLoggingLength : DEFAULT_MAX_LENGTH;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logRequest(request,body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) {

        int bodyLength = body.length;
        if(bodyLength <= maxLength) {
            logger.info("{}, uri: {}, method: {}, headers: {}, request: {}", serviceName, request.getURI(), request.getMethod().name(),
                    request.getHeaders(), LogMaskUtils.maskFields(new String(body, Charset.defaultCharset())));
        } else {
            logger.info("{}, uri: {}, method: {}, headers: {}, request: {}: {} bytes", serviceName, request.getURI(), request.getMethod().name(),
                    request.getHeaders(), "[Request is too long to be logged]", bodyLength);
        }
    }

    private void logResponse(ClientHttpResponse response) throws IOException {
        logger.info("{}, Response with status code: {}, headers: {}, raw response: {}", serviceName, response.getStatusCode(),
                response.getHeaders(),LogMaskUtils.maskFields(StreamUtils.copyToString(response.getBody(), Charset.defaultCharset())));
    }
}
