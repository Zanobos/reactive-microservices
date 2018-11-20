package it.zano.microservices.rest.resources;

import it.zano.microservices.controller.rest.BaseResource;
import it.zano.microservices.model.beans.ObservableProcessStateEnum;

/**
 * @author a.zanotti
 * @since 20/11/2018
 */
public class ObservableProcessResource extends BaseResource {

    private ObservableProcessStateEnum state;

    public ObservableProcessStateEnum getState() {
        return state;
    }

    public void setState(ObservableProcessStateEnum state) {
        this.state = state;
    }
}
