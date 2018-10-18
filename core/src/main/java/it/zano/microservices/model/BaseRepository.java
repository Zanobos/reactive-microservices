package it.zano.microservices.model;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author a.zanotti
 * @since 17/10/2018
 */
public interface BaseRepository<E extends BaseEntity> extends JpaRepository<E, Integer> {
}
