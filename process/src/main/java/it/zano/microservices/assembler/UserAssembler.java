package it.zano.microservices.assembler;

import it.zano.microservices.controllers.user.UserController;
import it.zano.microservices.model.User;
import it.zano.microservices.resources.UserResource;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

/**
 * @author a.zanotti
 * @since 15/10/2018
 */
@Service
public class UserAssembler extends ResourceAssemblerSupport<User, UserResource> {

    private final Mapper mapper;

    @Autowired
    public UserAssembler(Mapper mapper) {
        super(UserController.class, UserResource.class);
        this.mapper = mapper;
    }

    @Override
    public UserResource toResource(User user) {
        UserResource userResource = createResourceWithId(user.getFirstName(),user);
        mapper.map(user,userResource);
        return userResource;
    }
}
