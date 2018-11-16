package it.zano.microservices.webservices.bitcoinprice;

import it.zano.microservices.webservices.rest.ArchRestTemplateProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author a.zanotti
 * @since 16/11/2018
 */
@Configuration
@ConfigurationProperties(prefix = "rest-template.bitcoin-price")
public class BitcoinPriceProperties extends ArchRestTemplateProperties {
}
