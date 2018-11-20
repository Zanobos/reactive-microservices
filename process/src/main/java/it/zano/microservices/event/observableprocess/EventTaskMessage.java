package it.zano.microservices.event.observableprocess;

import it.zano.microservices.controller.event.RabbitMessage;
import it.zano.microservices.dispatcher.ObservableProcessManager;

/**
 * @author a.zanotti
 * @since 19/10/2018
 */
public class EventTaskMessage extends RabbitMessage {

    private Integer processId;
    private ObservableProcessManager.EventTypeEnum eventTypeEnum;

    public Integer getProcessId() {
        return processId;
    }

    public void setProcessId(Integer processId) {
        this.processId = processId;
    }

    public ObservableProcessManager.EventTypeEnum getEventTypeEnum() {
        return eventTypeEnum;
    }

    public void setEventTypeEnum(ObservableProcessManager.EventTypeEnum eventTypeEnum) {
        this.eventTypeEnum = eventTypeEnum;
    }
}
