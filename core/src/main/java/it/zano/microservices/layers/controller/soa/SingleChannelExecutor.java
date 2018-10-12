package it.zano.microservices.layers.controller.soa;

import it.zano.microservices.exception.MicroServiceException;
import org.springframework.stereotype.Service;

/**
 * Created by a.zanotti on 18/01/2018
 */
@Service
public interface SingleChannelExecutor<CHREQ extends BaseSoaRequestPayload, CHRES extends BaseSoaResponsePayload> {

    CHRES executeBusinessLogic(CHREQ channelRequest) throws MicroServiceException;

}
