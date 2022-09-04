package com.jana.bms.repository;

import com.jana.bms.domain.Seat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Seat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SeatRepository extends JpaRepository<Seat, Long>, JpaSpecificationExecutor<Seat> {}
