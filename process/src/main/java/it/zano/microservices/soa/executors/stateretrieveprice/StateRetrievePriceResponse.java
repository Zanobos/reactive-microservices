package it.zano.microservices.soa.executors.stateretrieveprice;

import it.zano.microservices.controller.soa.BaseSoaResponsePayload;

/**
 * @author a.zanotti
 * @since 16/11/2018
 */
public class StateRetrievePriceResponse extends BaseSoaResponsePayload {

    private String currency;
    private String rate;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
