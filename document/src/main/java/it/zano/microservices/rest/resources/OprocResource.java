package it.zano.microservices.rest.resources;

/**
 * @author a.zanotti
 * @since 23/11/2018
 */
public class OprocResource {

    private Integer id;
    private String actualState;
    private String lastObservedState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getActualState() {
        return actualState;
    }

    public void setActualState(String actualState) {
        this.actualState = actualState;
    }

    public String getLastObservedState() {
        return lastObservedState;
    }

    public void setLastObservedState(String lastObservedState) {
        this.lastObservedState = lastObservedState;
    }
}
