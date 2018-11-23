package it.zano.microservices.webservices.oprocremote;

import it.zano.microservices.webservices.rest.ArchRestTemplateProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author a.zanotti
 * @since 23/11/2018
 */
@Configuration
@ConfigurationProperties(prefix = "rest-template.oproc-service")
public class OprocProperties extends ArchRestTemplateProperties {
}
