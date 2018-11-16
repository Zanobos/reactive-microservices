package it.zano.microservices.webservices.bitcoinprice;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.zano.microservices.webservices.rest.ArchRestResource;

import java.util.Map;

/**
 * @author a.zanotti
 * @since 16/11/2018
 */
public class BitcoinPriceResource extends ArchRestResource {

    private Map<String, String> time;
    private String disclaimer;
    private String chartName;
    private Map<String, BpiData> bpi;

    public Map<String, String> getTime() {
        return time;
    }

    public void setTime(Map<String, String> time) {
        this.time = time;
    }

    public String getDisclaimer() {
        return disclaimer;
    }

    public void setDisclaimer(String disclaimer) {
        this.disclaimer = disclaimer;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public Map<String, BpiData> getBpi() {
        return bpi;
    }

    public void setBpi(Map<String, BpiData> bpi) {
        this.bpi = bpi;
    }

    public static class BpiData {
        private String code;
        private String symbol;
        private String rate;
        private String description;
        @JsonProperty("rate_float")
        private double rateFloat;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public double getRateFloat() {
            return rateFloat;
        }

        public void setRateFloat(double rateFloat) {
            this.rateFloat = rateFloat;
        }
    }
}
