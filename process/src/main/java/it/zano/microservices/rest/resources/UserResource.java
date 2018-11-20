package it.zano.microservices.rest.resources;

import it.zano.microservices.controller.rest.BaseResource;

/**
 * @author a.zanotti
 * @since 15/10/2018
 */
public class UserResource extends BaseResource {

    private Integer userId;
    private String firstName;
    private String lastName;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

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
