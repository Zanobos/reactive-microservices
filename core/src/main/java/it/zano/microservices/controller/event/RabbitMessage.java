package it.zano.microservices.controller.event;

import it.zano.microservices.util.JsonUtils;

import java.io.Serializable;

/**
 * @author a.zanotti
 * @since 20/11/2018
 */
public abstract class RabbitMessage implements Serializable {

    private static final long serialVersionUID = 42L;

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
