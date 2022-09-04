package com.jana.bms.repository;

import com.jana.bms.domain.Actor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Actor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ActorRepository extends JpaRepository<Actor, Long>, JpaSpecificationExecutor<Actor> {}
