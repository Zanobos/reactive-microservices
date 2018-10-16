package it.zano.microservices.layers.controller.soa;

import it.zano.microservices.exception.MicroServiceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Created by a.zanotti on 18/01/2018
 */
public abstract class LogicExecutor<REQ,RES> {

    public ResponseEntity<RES> execute(HttpHeaders httpHeaders, REQ request) throws MicroServiceException {
        return null;
    }

}
