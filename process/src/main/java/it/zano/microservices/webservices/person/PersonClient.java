package it.zano.microservices.webservices.person;

import it.zano.microservices.exception.MicroServiceException;
import it.zano.microservices.gen.person.CheckBlockedNames;
import it.zano.microservices.gen.person.CheckBlockedNamesResponse;
import it.zano.microservices.gen.person.ObjectFactory;
import it.zano.microservices.webservices.soap.ArchSoapClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author a.zanotti
 * @since 07/11/2018
 */
@Service
public class PersonClient extends ArchSoapClient {

    private ObjectFactory objectFactory;

    @Autowired
    public PersonClient(PersonClientProperties properties) {
        super(properties);
        this.objectFactory = new ObjectFactory();
    }

    public Boolean checkPerson(String firstName, String lastName) throws MicroServiceException {
        CheckBlockedNames request = objectFactory.createCheckBlockedNames();
        request.setFirstName(firstName);
        request.setLastName(lastName);
        request.setUserName("?");
        request.setPassword("?");

        CheckBlockedNamesResponse response = call(request);
        return response.isCheckBlockedNamesResult();
    }
}
