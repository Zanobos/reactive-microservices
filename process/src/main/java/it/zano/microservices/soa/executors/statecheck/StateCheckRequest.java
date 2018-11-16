package it.zano.microservices.soa.executors.statecheck;

import it.zano.microservices.controller.soa.BaseSoaRequestPayload;

/**
 * @author a.zanotti
 * @since 07/11/2018
 */
public class StateCheckRequest extends BaseSoaRequestPayload {

    private String firstName;
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
