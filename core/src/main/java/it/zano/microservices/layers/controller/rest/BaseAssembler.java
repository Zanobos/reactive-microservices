package it.zano.microservices.layers.controller.rest;

import org.dozer.Mapper;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

/**
 * @author a.zanotti
 * @since 15/10/2018
 */
public abstract class BaseAssembler<T,D extends BaseResource> extends ResourceAssemblerSupport<T,D> {

    protected final Mapper mapper;

    public BaseAssembler(Class<?> controllerClass, Class<D> resourceType, Mapper mapper) {
        super(controllerClass, resourceType);
        this.mapper = mapper;
    }
}
