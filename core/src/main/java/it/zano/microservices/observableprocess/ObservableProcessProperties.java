package it.zano.microservices.observableprocess;

import java.util.List;
import java.util.Map;

/**
 * @author a.zanotti
 * @since 22/11/2018
 */
public abstract class ObservableProcessProperties<STATE, TRANSITION> {

    private Map<STATE,StateInfo<TRANSITION>> states;
    private Map<TRANSITION, TransitionInfo<STATE>> transitions;

    public Map<STATE, StateInfo<TRANSITION>> getStates() {
        return states;
    }

    public void setStates(Map<STATE, StateInfo<TRANSITION>> states) {
        this.states = states;
    }

    public Map<TRANSITION, TransitionInfo<STATE>> getTransitions() {
        return transitions;
    }

    public void setTransitions(Map<TRANSITION, TransitionInfo<STATE>> transitions) {
        this.transitions = transitions;
    }

    public static class StateInfo<TRANSITION> {

        private Integer timeout;
        private List<TRANSITION> transitions;

        public Integer getTimeout() {
            return timeout;
        }

        public void setTimeout(Integer timeout) {
            this.timeout = timeout;
        }

        public List<TRANSITION> getTransitions() {
            return transitions;
        }

        public void setTransitions(List<TRANSITION> transitions) {
            this.transitions = transitions;
        }
    }

    public static class TransitionInfo<STATE> {

        private STATE from;
        private STATE to;

        public STATE getFrom() {
            return from;
        }

        public void setFrom(STATE from) {
            this.from = from;
        }

        public STATE getTo() {
            return to;
        }

        public void setTo(STATE to) {
            this.to = to;
        }
    }

}
