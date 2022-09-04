package com.jana.bms.repository;

import com.jana.bms.domain.Theatre;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Theatre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TheatreRepository extends JpaRepository<Theatre, Long>, JpaSpecificationExecutor<Theatre> {}
