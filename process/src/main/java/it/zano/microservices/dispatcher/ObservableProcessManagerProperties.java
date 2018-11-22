package it.zano.microservices.dispatcher;

import it.zano.microservices.model.beans.ObservableProcessStateEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author a.zanotti
 * @since 21/11/2018
 */
@Configuration
@ConfigurationProperties(prefix = "observable-process-manager")
public class ObservableProcessManagerProperties {

    private Map<ObservableProcessStateEnum,Integer> timeouts;

    public Map<ObservableProcessStateEnum, Integer> getTimeouts() {
        return timeouts;
    }

    public void setTimeouts(Map<ObservableProcessStateEnum, Integer> timeouts) {
        this.timeouts = timeouts;
    }
}
