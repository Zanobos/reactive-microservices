package it.zano.microservices.webservices.documents;

import it.zano.microservices.webservices.rest.ArchRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author a.zanotti
 * @since 20/11/2018
 */
@Service
public class DocumentTemplate extends ArchRestTemplate<DocumentResource>{

    @Autowired
    public DocumentTemplate(DocumentProperties properties) {
        super(properties, DocumentResource.class);
    }
}
