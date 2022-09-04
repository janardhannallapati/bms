package com.jana.bms.repository;

import com.jana.bms.domain.Show;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Show entity.
 *
 * When extending this class, extend ShowRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ShowRepository extends ShowRepositoryWithBagRelationships, JpaRepository<Show, Long>, JpaSpecificationExecutor<Show> {
    default Optional<Show> findOneWithEagerRelationships(Long showId) {
        return this.fetchBagRelationships(this.findById(showId));
    }

    default List<Show> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<Show> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
