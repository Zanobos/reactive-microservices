package it.zano.microservices.controller.soa;


import it.zano.microservices.exception.MicroServiceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

/**
 * @author a.zanotti
 * @since 12/10/2018
 */
public abstract class BaseSoaController {

    protected <REQ extends BaseSoaRequestPayload, RES extends BaseSoaResponsePayload, IREQ, IRES> ResponseEntity<RES>
        apply(BaseSoaLogicExecutor<REQ, RES, IREQ, IRES> baseSoaLogicExecutor, HttpHeaders httpHeaders, REQ request) throws MicroServiceException {

        return baseSoaLogicExecutor.execute(httpHeaders,request);
    }

}
