package it.zano.microservices.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;

/**
 * Auto configuration only if the spring.datasource.url is present
 * @author a.zanotti
 * @since 17/10/2018
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.datasource", name = "url")
public class DbConfiguration extends DataSourceAutoConfiguration {
}
