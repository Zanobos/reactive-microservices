package it.zano.microservices.soa;

import io.swagger.annotations.Api;
import it.zano.microservices.exception.MicroServiceException;
import it.zano.microservices.layers.controller.soa.BaseSoaController;
import it.zano.microservices.layers.controller.soa.BaseSoaRequestPayload;
import it.zano.microservices.layers.controller.soa.BaseSoaResponsePayload;
import it.zano.microservices.layers.controller.soa.LogicExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author a.zanotti
 * @since 16/10/2018
 */
@Api(tags = "state")
@Controller
@RequestMapping(value = "/state", produces = {MediaType.APPLICATION_JSON_VALUE})
public class StateController extends BaseSoaController{

    @Autowired
    protected StateController(StateExecutorDispatcher stateExecutorDispatcher) {
        super(stateExecutorDispatcher);
    }

    @PostMapping(value = "/init")
    public ResponseEntity<StateInitResponse> init(  @RequestHeader HttpHeaders httpHeaders,
                                                    @RequestBody StateInitRequest request) throws MicroServiceException {

        LogicExecutor<? extends BaseSoaRequestPayload, ? extends BaseSoaResponsePayload> logicExecutor =
                executorDispatcher.getLogicExecutor(StateInitExecutor.class);

        return logicExecutor.execute(httpHeaders,request);
    }


}
