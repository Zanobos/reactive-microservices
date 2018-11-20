package it.zano.microservices.controller.event;

/**
 * @author a.zanotti
 * @since 19/10/2018
 */
public class ErrorMessage extends RabbitMessage {

    private String serviceId;
    private String errorId;

    public ErrorMessage() {
    }

    public ErrorMessage(String serviceId, String errorId) {
        this.serviceId = serviceId;
        this.errorId = errorId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }
}
