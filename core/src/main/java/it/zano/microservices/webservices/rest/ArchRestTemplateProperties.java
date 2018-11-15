package it.zano.microservices.webservices.rest;

/**
 * @author a.zanotti
 * @since 15/11/2018
 */
public abstract class ArchRestTemplateProperties {

    private String name;
    private String endpoint;
    private Integer maxLoggingLength;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public Integer getMaxLoggingLength() {
        return maxLoggingLength;
    }

    public void setMaxLoggingLength(Integer maxLoggingLength) {
        this.maxLoggingLength = maxLoggingLength;
    }
}
