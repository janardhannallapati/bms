package com.jana.bms.repository;

import com.jana.bms.domain.Screen;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Screen entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScreenRepository extends JpaRepository<Screen, Long>, JpaSpecificationExecutor<Screen> {}
