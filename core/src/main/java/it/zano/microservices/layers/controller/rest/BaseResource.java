package it.zano.microservices.layers.controller.rest;

import it.zano.microservices.util.JsonUtils;
import org.springframework.hateoas.ResourceSupport;

/**
 * @author a.zanotti
 * @since 15/10/2018
 */
public abstract class BaseResource extends ResourceSupport {

    @Override
    public String toString() {
        return JsonUtils.toJson(this);
    }
}
