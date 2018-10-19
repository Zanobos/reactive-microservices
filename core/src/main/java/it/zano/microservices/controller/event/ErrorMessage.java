package it.zano.microservices.controller.event;

import it.zano.microservices.util.JsonUtils;

import java.io.Serializable;

/**
 * @author a.zanotti
 * @since 19/10/2018
 */
public class ErrorMessage implements Serializable {

    private static final long serialVersionUID = 42L;

    private String serviceId;
    private String errorId;

    public ErrorMessage() {
    }

    public ErrorMessage(String serviceId, String errorId) {
        this.serviceId = serviceId;
        this.errorId = errorId;
    }

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
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
