package com.jana.bms.repository;

import com.jana.bms.domain.Movie;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Movie entity.
 *
 * When extending this class, extend MovieRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface MovieRepository extends MovieRepositoryWithBagRelationships, JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {
    default Optional<Movie> findOneWithEagerRelationships(Long movieId) {
        return this.fetchBagRelationships(this.findById(movieId));
    }

    default List<Movie> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Movie> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}