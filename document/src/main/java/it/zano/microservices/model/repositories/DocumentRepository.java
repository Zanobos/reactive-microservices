package it.zano.microservices.model.repositories;

import it.zano.microservices.model.BaseRepository;
import it.zano.microservices.model.entities.Document;
import org.springframework.stereotype.Service;

/**
 * @author a.zanotti
 * @since 17/10/2018
 */
@Service
public interface DocumentRepository extends BaseRepository<Document> {
}
