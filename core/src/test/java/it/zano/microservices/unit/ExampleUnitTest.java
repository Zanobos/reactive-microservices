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

        ExampleArchRestProperties properties = new ExampleArchRestProperties();
        properties.setEndpoint("ENDPOINT");
        ExampleArchRestTemplate exampleArchRestTemplate = new ExampleArchRestTemplate(properties);
        Assert.assertEquals("ENDPOINT",exampleArchRestTemplate.getEndpoint());

    }

    private static class ExampleResource extends ArchRestResource {}

    @Configuration
    @ConfigurationProperties(prefix = "rest-template.example-service")
    private static class ExampleArchRestProperties extends ArchRestTemplateProperties {}

    @Service
    private static class ExampleArchRestTemplate extends ArchRestTemplate<ExampleResource> {

        public ExampleArchRestTemplate(ArchRestTemplateProperties properties) {
            super(properties);
        }
    }

}
