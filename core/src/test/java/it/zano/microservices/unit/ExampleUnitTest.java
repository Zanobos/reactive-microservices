package it.zano.microservices.unit;

import it.zano.microservices.util.LogMaskUtils;
import it.zano.microservices.webservices.rest.ArchRestResource;
import it.zano.microservices.webservices.rest.ArchRestTemplate;
import it.zano.microservices.webservices.rest.ArchRestTemplateProperties;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * Unit tests follow these guidelines:
 * 1 - Spring managed objects are instantiated with new
 * 2 - They test a single class
 * 3 - They do not need any special annotation, except the classical ones from junit
 * @author a.zanotti
 * @since 16/11/2018
 */
public class ExampleUnitTest {

    @Test
    public void genericTestOnNonSpringObject() {

        String logMessage = "{\"password\":\"my secret password\"}";
        String maskedMessage = LogMaskUtils.maskFields(logMessage);
        Assert.assertEquals("{\"password\":\"********\"}",maskedMessage);
    }

    @Test
    public void genericTestOnSpringObject() {

        StubRestProperties properties = new StubRestProperties();
        properties.setEndpoint("ENDPOINT");
        StubRestTemplate stubRestTemplate = new StubRestTemplate(properties);
        Assert.assertEquals("ENDPOINT", stubRestTemplate.getEndpoint());

    }

    private static class StubRestResource extends ArchRestResource {}

    @Configuration
    @ConfigurationProperties(prefix = "rest-template.example-service")
    private static class StubRestProperties extends ArchRestTemplateProperties {}

    @Service
    private static class StubRestTemplate extends ArchRestTemplate<StubRestResource> {

        public StubRestTemplate(ArchRestTemplateProperties properties) {
            super(properties);
        }
    }

}
