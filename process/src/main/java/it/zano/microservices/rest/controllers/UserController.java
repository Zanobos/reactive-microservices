package it.zano.microservices.rest.controllers;

import io.swagger.annotations.Api;
import it.zano.microservices.controller.rest.BaseRestController;
import it.zano.microservices.exception.MicroServiceException;
import it.zano.microservices.model.entities.User;
import it.zano.microservices.model.repositories.UserRepository;
import it.zano.microservices.rest.assembler.UserAssembler;
import it.zano.microservices.rest.resources.UserResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author a.zanotti
 * @since 12/10/2018
 */
@Api(tags = "users")
@RestController
@RequestMapping(value = "/users", produces = {MediaType.APPLICATION_JSON_VALUE})
public class UserController extends BaseRestController<User,UserResource> {

    @Autowired
    protected UserController(UserAssembler assembler, UserRepository userRepository) {
        super(assembler);
        this.userRepository = userRepository;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserResource> getUser(@RequestHeader HttpHeaders httpHeaders, @PathVariable(value = "id") Integer id) throws MicroServiceException {
        User user = userRepository.findById(id).orElseThrow(() -> new MicroServiceException("Not found"));
        UserResource userResource = assembler.toResource(user);
        return ResponseEntity.ok(userResource);
    }

    @PostMapping
    public ResponseEntity<UserResource> postUser(@RequestHeader HttpHeaders httpHeaders, @RequestBody UserResource userResource) {
        User user = assembler.toModelClass(userResource);
        User userSaved = userRepository.save(user);
        UserResource resource = assembler.toResource(userSaved);
        return ResponseEntity.ok(resource);
    }

    private final UserRepository userRepository;

}


