package it.zano.microservices.dispatcher;

import it.zano.microservices.model.beans.ObservableProcessStateEnum;
import it.zano.microservices.model.beans.ObservableProcessTransitionEnum;
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

    private Map<ObservableProcessStateEnum,StateInfo> states;
    private Map<ObservableProcessTransitionEnum, TransitionInfo> transitions;

    public Map<ObservableProcessStateEnum, StateInfo> getStates() {
        return states;
    }

    public void setStates(Map<ObservableProcessStateEnum, StateInfo> states) {
        this.states = states;
    }

    public Map<ObservableProcessTransitionEnum, TransitionInfo> getTransitions() {
        return transitions;
    }

    public void setTransitions(Map<ObservableProcessTransitionEnum, TransitionInfo> transitions) {
        this.transitions = transitions;
    }

    public static class TransitionInfo {

        private ObservableProcessStateEnum from;
        private ObservableProcessStateEnum to;

        public ObservableProcessStateEnum getFrom() {
            return from;
        }

        public void setFrom(ObservableProcessStateEnum from) {
            this.from = from;
        }

        public ObservableProcessStateEnum getTo() {
            return to;
        }

        public void setTo(ObservableProcessStateEnum to) {
            this.to = to;
        }
    }

    public static class StateInfo {

        private Integer timeout;

        public Integer getTimeout() {
            return timeout;
        }

        public void setTimeout(Integer timeout) {
            this.timeout = timeout;
        }

    }
}
