package it.zano.microservices.layers.controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

public abstract class BaseRestController<T, D extends BaseResource> {

    @Autowired
    protected ResourceAssemblerSupport<T,D> assembler;


}
