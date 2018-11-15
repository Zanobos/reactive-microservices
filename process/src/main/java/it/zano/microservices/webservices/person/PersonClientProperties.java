package it.zano.microservices.webservices.person;

import it.zano.microservices.webservices.soap.ArchSoapClientProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author a.zanotti
 * @since 07/11/2018
 */
@Configuration
@ConfigurationProperties(prefix = "soap-service.person-client")
public class PersonClientProperties extends ArchSoapClientProperties {
}
