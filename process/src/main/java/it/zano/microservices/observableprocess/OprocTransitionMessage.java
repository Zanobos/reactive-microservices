package it.zano.microservices.observableprocess;

import it.zano.microservices.controller.event.RabbitMessage;

/**
 * @author a.zanotti
 * @since 19/10/2018
 */
public class OprocTransitionMessage extends RabbitMessage {

    private OprocTransitionEnum transition;
    private Integer processId;

    public OprocTransitionMessage() {
    }

    public OprocTransitionMessage(OprocTransitionEnum transition, Integer processId) {
        this.transition = transition;
        this.processId = processId;
    }

    public Integer getProcessId() {
        return processId;
    }

    public void setProcessId(Integer processId) {
        this.processId = processId;
    }

    public OprocTransitionEnum getTransition() {
        return transition;
    }

    public void setTransition(OprocTransitionEnum transition) {
        this.transition = transition;
    }
}
