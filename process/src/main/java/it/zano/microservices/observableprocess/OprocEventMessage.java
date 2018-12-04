package it.zano.microservices.observableprocess;

import it.zano.microservices.controller.event.RabbitMessage;

/**
 * @author a.zanotti
 * @since 19/10/2018
 */
public class OprocEventMessage extends RabbitMessage {

    private OprocEventEnum eventEnum;
    private Integer processId;

    public OprocEventMessage() {
    }

    public OprocEventMessage(OprocEventEnum eventEnum, Integer processId) {
        this.eventEnum = eventEnum;
        this.processId = processId;
    }

    public Integer getProcessId() {
        return processId;
    }

    public void setProcessId(Integer processId) {
        this.processId = processId;
    }

    public OprocEventEnum getEventEnum() {
        return eventEnum;
    }

    public void setEventEnum(OprocEventEnum eventEnum) {
        this.eventEnum = eventEnum;
    }
}
