package it.zano.microservices.observableprocess;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author a.zanotti
 * @since 23/11/2018
 */
@Configuration
@ConfigurationProperties(prefix = "observable-process-manager")
public class OprocPropertiesImpl extends ObservableProcessProperties
        <OprocStateEnum, OprocEventEnum> {
}
