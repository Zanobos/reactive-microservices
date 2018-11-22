package it.zano.microservices.event.observableprocess;

import it.zano.microservices.controller.event.RabbitMessage;
import it.zano.microservices.model.beans.ObservableProcessTransitionEnum;

/**
 * @author a.zanotti
 * @since 19/10/2018
 */
public class EventTaskMessage extends RabbitMessage {

    private Integer processId;
    private ObservableProcessTransitionEnum transition;

    public Integer getProcessId() {
        return processId;
    }

    public void setProcessId(Integer processId) {
        this.processId = processId;
    }

    public ObservableProcessTransitionEnum getTransition() {
        return transition;
    }

    public void setTransition(ObservableProcessTransitionEnum transition) {
        this.transition = transition;
    }
}
