package it.zano.microservices.soa.controllers;

import io.swagger.annotations.Api;
import it.zano.microservices.exception.MicroServiceException;
import it.zano.microservices.controller.soa.BaseSoaController;
import it.zano.microservices.soa.executors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author a.zanotti
 * @since 16/10/2018
 */
@Api(tags = "state")
@RestController
@RequestMapping(value = "/state", produces = {MediaType.APPLICATION_JSON_VALUE})
public class StateController extends BaseSoaController{

    @PostMapping(value = "/init")
    public ResponseEntity<StateInitResponse> init(@RequestHeader HttpHeaders httpHeaders,
                                                  @RequestBody StateInitRequest request) throws MicroServiceException {
        return apply(stateInitExecutorBaseSoa, httpHeaders, request);
    }

    @PostMapping(value = "/check")
    public ResponseEntity<StateCheckResponse> check(@RequestHeader HttpHeaders httpHeaders,
                                                  @RequestBody StateCheckRequest request) throws MicroServiceException {
        return apply(stateCheckExecutorBaseSoa, httpHeaders, request);
    }

    @PostMapping(value = "/end")
    public ResponseEntity<StateEndResponse> end(@RequestHeader HttpHeaders httpHeaders,
                                                @RequestBody StateEndRequest request) throws MicroServiceException {
        return apply(stateEndExecutorBaseSoa, httpHeaders, request);
    }

    @Autowired
    public StateController(
            StateInitExecutorBaseSoa stateInitExecutorBaseSoa,
            StateEndExecutorBaseSoa stateEndExecutorBaseSoa,
            StateCheckExecutorBaseSoa stateCheckExecutorBaseSoa) {
        this.stateInitExecutorBaseSoa = stateInitExecutorBaseSoa;
        this.stateEndExecutorBaseSoa = stateEndExecutorBaseSoa;
        this.stateCheckExecutorBaseSoa = stateCheckExecutorBaseSoa;
    }

    private StateInitExecutorBaseSoa stateInitExecutorBaseSoa;
    private StateEndExecutorBaseSoa stateEndExecutorBaseSoa;
    private StateCheckExecutorBaseSoa stateCheckExecutorBaseSoa;

}
