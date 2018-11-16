package it.zano.microservices.unit.resttemplates;

import it.zano.microservices.webservices.bitcoinprice.BitcoinPriceProperties;
import it.zano.microservices.webservices.bitcoinprice.BitcoinPriceTemplate;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author a.zanotti
 * @since 16/11/2018
 */
public class BitcoinRestTemplateTest {

    private static final String ENDPOINT = "http://api.coindesk.com/v1/bpi/currentprice.json";

    @Test
    public void createBitcoinTemplateTest() {

        BitcoinPriceProperties bitcoinPriceProperties = new BitcoinPriceProperties();
        bitcoinPriceProperties.setEndpoint(ENDPOINT);
        BitcoinPriceTemplate bitcoinPriceTemplate = new BitcoinPriceTemplate(bitcoinPriceProperties);
        Assert.assertEquals(bitcoinPriceProperties.getEndpoint(), bitcoinPriceTemplate.getEndpoint());

    }

}
