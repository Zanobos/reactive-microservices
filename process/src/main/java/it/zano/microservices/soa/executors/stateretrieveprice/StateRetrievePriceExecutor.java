package it.zano.microservices.soa.executors.stateretrieveprice;

import it.zano.microservices.controller.soa.BaseSoaLogicExecutor;
import it.zano.microservices.exception.MicroServiceException;
import it.zano.microservices.webservices.bitcoinprice.BitcoinPriceResource;
import it.zano.microservices.webservices.bitcoinprice.BitcoinPriceTemplate;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author a.zanotti
 * @since 16/11/2018
 */
@Service
public class StateRetrievePriceExecutor extends BaseSoaLogicExecutor<StateRetrievePriceRequest, StateRetrievePriceResponse,
        StateRetrievePriceRequest, StateRetrievePriceResponse> {

    private BitcoinPriceTemplate bitcoinPriceTemplate;

    @Autowired
    public StateRetrievePriceExecutor(Mapper mapper, BitcoinPriceTemplate bitcoinPriceTemplate) {
        super(mapper);
        this.bitcoinPriceTemplate = bitcoinPriceTemplate;
    }

    @Override
    protected StateRetrievePriceResponse performBusinessLogic(StateRetrievePriceRequest innerRequest) throws MicroServiceException {

        ResponseEntity<BitcoinPriceResource> responseEntity = bitcoinPriceTemplate.getForEntity(
                bitcoinPriceTemplate.getEndpoint(), bitcoinPriceTemplate.getRestResourceClass());

        StateRetrievePriceResponse stateRetrievePriceResponse = new StateRetrievePriceResponse();
        stateRetrievePriceResponse.setCurrency(innerRequest.getCurrency());
        try {
            stateRetrievePriceResponse.setRate(responseEntity.getBody().getBpi().get(innerRequest.getCurrency()).getRate());
        } catch (Exception e) {
            throw new MicroServiceException(e);
        }
        return stateRetrievePriceResponse;
    }

}
