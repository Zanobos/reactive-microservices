package it.zano.microservices.rest.assembler;

import it.zano.microservices.rest.controllers.UserController;
import it.zano.microservices.controller.rest.BaseAssembler;
import it.zano.microservices.model.entities.User;
import it.zano.microservices.rest.resources.UserResource;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author a.zanotti
 * @since 15/10/2018
 */
@Service
public class UserAssembler extends BaseAssembler<User, UserResource> {

    @Autowired
    public UserAssembler(Mapper mapper) {
        super(UserController.class, UserResource.class , mapper);
    }

    @Override
    public UserResource toResource(User user) {
        UserResource userResource = createResourceWithId(user.getId(),user);
        mapper.map(user,userResource);
        return userResource;
    }
}
