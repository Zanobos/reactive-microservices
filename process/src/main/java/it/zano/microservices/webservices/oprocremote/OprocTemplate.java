package it.zano.microservices.webservices.oprocremote;

import it.zano.microservices.webservices.rest.ArchRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Template to connect with the controller in document micro service
 * @author a.zanotti
 * @since 23/11/2018
 */
@Service
public class OprocTemplate extends ArchRestTemplate{

    @Autowired
    public OprocTemplate(OprocProperties properties) {
        super(properties);
    }
}
