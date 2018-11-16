package it.zano.microservices.soa.controllers;

import io.swagger.annotations.Api;
import it.zano.microservices.controller.soa.BaseSoaController;
import it.zano.microservices.exception.MicroServiceException;
import it.zano.microservices.soa.executors.stateretrieveprice.StateRetrievePriceExecutor;
import it.zano.microservices.soa.executors.stateretrieveprice.StateRetrievePriceRequest;
import it.zano.microservices.soa.executors.stateretrieveprice.StateRetrievePriceResponse;
import it.zano.microservices.soa.executors.statecheck.StateCheckExecutor;
import it.zano.microservices.soa.executors.statecheck.StateCheckRequest;
import it.zano.microservices.soa.executors.statecheck.StateCheckResponse;
import it.zano.microservices.soa.executors.stateend.StateEndExecutor;
import it.zano.microservices.soa.executors.stateend.StateEndRequest;
import it.zano.microservices.soa.executors.stateend.StateEndResponse;
import it.zano.microservices.soa.executors.stateinit.StateInitExecutor;
import it.zano.microservices.soa.executors.stateinit.StateInitRequest;
import it.zano.microservices.soa.executors.stateinit.StateInitResponse;
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
        return apply(stateInitExecutor, httpHeaders, request);
    }

    @PostMapping(value = "/check")
    public ResponseEntity<StateCheckResponse> check(@RequestHeader HttpHeaders httpHeaders,
                                                    @RequestBody StateCheckRequest request) throws MicroServiceException {
        return apply(stateCheckExecutor, httpHeaders, request);
    }

    @PostMapping(value = "/retrieveprice")
    public ResponseEntity<StateRetrievePriceResponse> retrievePrice(@RequestHeader HttpHeaders httpHeaders,
                                                                    @RequestBody StateRetrievePriceRequest request) throws MicroServiceException {
        return apply(stateRetrievePriceExecutor, httpHeaders, request);
    }

    @PostMapping(value = "/end")
    public ResponseEntity<StateEndResponse> end(@RequestHeader HttpHeaders httpHeaders,
                                                @RequestBody StateEndRequest request) throws MicroServiceException {
        return apply(stateEndExecutor, httpHeaders, request);
    }

    @Autowired
    public StateController(
            StateInitExecutor stateInitExecutor,
            StateEndExecutor stateEndExecutor,
            StateCheckExecutor stateCheckExecutor,
            StateRetrievePriceExecutor stateRetrievePriceExecutor) {
        this.stateInitExecutor = stateInitExecutor;
        this.stateEndExecutor = stateEndExecutor;
        this.stateCheckExecutor = stateCheckExecutor;
        this.stateRetrievePriceExecutor = stateRetrievePriceExecutor;
    }

    private StateInitExecutor stateInitExecutor;
    private StateEndExecutor stateEndExecutor;
    private StateCheckExecutor stateCheckExecutor;
    private StateRetrievePriceExecutor stateRetrievePriceExecutor;

}
