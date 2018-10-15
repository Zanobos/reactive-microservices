package it.zano.microservices.controllers.user;

import it.zano.microservices.layers.controller.rest.BaseRestController;
import it.zano.microservices.model.User;
import it.zano.microservices.resources.UserResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author a.zanotti
 * @since 12/10/2018
 */
@RestController
@RequestMapping(value = "/users", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserController extends BaseRestController<User,UserResource> {

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserResource> getUser(@PathVariable(value = "id") String id){

        User user = new User();
        user.setFirstName("Andrea");
        user.setLastName(id);
        UserResource userResource = assembler.toResource(user);
        return ResponseEntity.ok(userResource);
    }

}
