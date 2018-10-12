package it.zano.microservices.webservices;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.Collections;
import java.util.Map;

public abstract class ArchRestTemplate extends RestTemplate {

	public ArchRestTemplate() {
		super(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
	}

	@PostConstruct
	private void init () {
		this.setInterceptors(Collections.singletonList(new ArchRestLoggingInterceptor(name)));
	}

	protected static final Logger log = LoggerFactory.getLogger(ArchRestTemplate.class);

	protected String name;
	protected String endpoint;

	protected URI buildUri(Map<String,String> queryParam) {
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(endpoint);
		queryParam.forEach(builder::queryParam);
		return builder.build().encode().toUri();
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

}
