package it.zano.microservices.soa;

import io.swagger.annotations.Api;
import it.zano.microservices.exception.MicroServiceException;
import it.zano.microservices.layers.controller.soa.BaseSoaController;
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

    @Autowired
    public StateController(StateInitExecutorBaseSoa stateInitExecutorBaseSoa) {
        this.stateInitExecutorBaseSoa = stateInitExecutorBaseSoa;
    }

    private StateInitExecutorBaseSoa stateInitExecutorBaseSoa;

}
