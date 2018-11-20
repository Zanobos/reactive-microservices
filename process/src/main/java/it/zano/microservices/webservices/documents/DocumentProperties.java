package it.zano.microservices.webservices.documents;

import it.zano.microservices.webservices.rest.ArchRestTemplateProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author a.zanotti
 * @since 20/11/2018
 */
@Configuration
@ConfigurationProperties(prefix = "rest-template.document-service")
public class DocumentProperties extends ArchRestTemplateProperties {
}
