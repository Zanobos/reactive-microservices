package it.zano.microservices.layers.controller.soa;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

/**
 * Created by a.zanotti on 18/01/2018
 */
public abstract class BaseSoaLogicExecutor<REQ extends BaseSoaRequestPayload,RES extends BaseSoaResponsePayload, IREQ, IRES> {

    public  ResponseEntity<RES> execute(HttpHeaders httpHeaders, REQ request) {
        IREQ innerRequest = mapperInput(httpHeaders, request);
        IRES innerResponse = performBusinessLogic(innerRequest);
        return mapperOutput(innerResponse);
    }

    protected ResponseEntity<RES> mapperOutput(IRES innerResponse) {
        return null;
    }

    protected IREQ mapperInput(HttpHeaders httpHeaders, REQ request) {
        return null;
    }

    protected abstract IRES performBusinessLogic(IREQ innerRequest);



}
