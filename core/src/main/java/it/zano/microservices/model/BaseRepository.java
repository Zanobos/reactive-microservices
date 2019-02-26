package it.zano.microservices.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author a.zanotti
 * @since 17/10/2018
 */
@NoRepositoryBean
public interface BaseRepository<E extends BaseEntity> extends JpaRepository<E, Integer> {
}
