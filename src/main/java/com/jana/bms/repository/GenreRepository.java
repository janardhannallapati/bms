package com.jana.bms.repository;

import com.jana.bms.domain.Genre;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Genre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GenreRepository extends JpaRepository<Genre, Long>, JpaSpecificationExecutor<Genre> {}
