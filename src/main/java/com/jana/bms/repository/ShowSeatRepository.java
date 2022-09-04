package com.jana.bms.repository;

import com.jana.bms.domain.ShowSeat;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ShowSeat entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShowSeatRepository extends JpaRepository<ShowSeat, Long>, JpaSpecificationExecutor<ShowSeat> {}
