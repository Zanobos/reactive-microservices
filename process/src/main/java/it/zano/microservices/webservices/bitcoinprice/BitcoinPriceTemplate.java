package it.zano.microservices.webservices.bitcoinprice;

import it.zano.microservices.webservices.rest.ArchRestTemplate;
import it.zano.microservices.webservices.rest.ArchRestTemplateProperties;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;

/**
 * @author a.zanotti
 * @since 16/11/2018
 */
@Service
public class BitcoinPriceTemplate extends ArchRestTemplate<BitcoinPriceResource> {

    public BitcoinPriceTemplate(ArchRestTemplateProperties properties) {
        super(properties, BitcoinPriceResource.class);
        getMessageConverters().add(new JavaScriptMessageConverter());
    }

    //I create a new converter, that it's basically the same jackson, but it works with when header is javascript
    private static class JavaScriptMessageConverter extends AbstractJackson2HttpMessageConverter {

        //Add a new mediatype converter
        private JavaScriptMessageConverter() {
            super(Jackson2ObjectMapperBuilder.json().build(),new MediaType("application","javascript"));
        }
    }
}
