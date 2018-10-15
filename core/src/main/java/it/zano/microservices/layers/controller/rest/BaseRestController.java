package it.zano.microservices.layers.controller.rest;

public abstract class BaseRestController<T, D extends BaseResource> {

    protected final BaseAssembler<T,D> assembler;

    protected BaseRestController(BaseAssembler<T,D> assembler) {
        this.assembler = assembler;
    }

}
