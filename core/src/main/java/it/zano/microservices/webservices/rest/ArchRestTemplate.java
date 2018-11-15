package it.zano.microservices.webservices.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class ArchRestTemplate extends RestTemplate {

    protected static final Logger log = LoggerFactory.getLogger(ArchRestTemplate.class);

    private ArchRestTemplateProperties properties;

	public ArchRestTemplate(ArchRestTemplateProperties properties) {
		super(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
		this.properties = properties;
        setInterceptors(Collections.singletonList(new ArchRestLoggingInterceptor(properties.getName(),
                properties.getMaxLoggingLength())));
	}


	protected URI buildUri(Map<String,String> queryParam, Map<String,String> uriParam) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(properties.getEndpoint());
        if (queryParam != null)
            queryParam.forEach(builder::queryParam);
        if(uriParam == null)
            uriParam = new HashMap<>();
        return builder.buildAndExpand(uriParam).encode().toUri();
	}

}
