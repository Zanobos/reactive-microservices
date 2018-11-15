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

    private String serviceName;
    private static final int MAX_LENGTH = 3000000;

    public ArchRestLoggingInterceptor(String serviceName) {
        this.serviceName = serviceName;
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        logRequest(request,body);
        ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
    }

    private void logRequest(HttpRequest request, byte[] body) {

        int bodyLength = body.length;
        if(bodyLength <= MAX_LENGTH) {
            logger.info("{}, Request to URI: {}, method: {}, headers: {}, request: {}", serviceName, request.getURI(), request.getMethod().name(),
                    request.getHeaders(), LogMaskUtils.maskFields(new String(body, Charset.defaultCharset())));
        } else {
            logger.info("{}, Request to URI: {}, method: {}, headers: {}, request: {}: {} bytes", serviceName, request.getURI(), request.getMethod().name(),
                    request.getHeaders(), "[Request is too long to be logged]", bodyLength);
        }
    }

    private void logResponse(ClientHttpResponse response) throws IOException {
        logger.info("{}, Response with status code: {}, headers: {}, raw response: {}", serviceName, response.getStatusCode(),
                response.getHeaders(),LogMaskUtils.maskFields(StreamUtils.copyToString(response.getBody(), Charset.defaultCharset())));
    }
}
