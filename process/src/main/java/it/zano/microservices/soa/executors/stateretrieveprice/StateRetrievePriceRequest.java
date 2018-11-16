package it.zano.microservices.soa.executors.stateretrieveprice;

import it.zano.microservices.controller.soa.BaseSoaRequestPayload;

/**
 * @author a.zanotti
 * @since 16/11/2018
 */
public class StateRetrievePriceRequest extends BaseSoaRequestPayload {

    private String currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
