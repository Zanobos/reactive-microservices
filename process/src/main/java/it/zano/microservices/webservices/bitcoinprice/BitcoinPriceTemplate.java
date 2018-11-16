package it.zano.microservices.webservices.bitcoinprice;

import it.zano.microservices.webservices.rest.ArchRestTemplate;
import it.zano.microservices.webservices.rest.ArchRestTemplateProperties;
import org.springframework.stereotype.Service;

/**
 * @author a.zanotti
 * @since 16/11/2018
 */
@Service
public class BitcoinPriceTemplate extends ArchRestTemplate<BitcoinPriceResource> {

    public BitcoinPriceTemplate(ArchRestTemplateProperties properties) {
        super(properties, BitcoinPriceResource.class);
    }
}
