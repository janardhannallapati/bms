package com.jana.bms.repository;

import com.jana.bms.domain.Language;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Language entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LanguageRepository extends JpaRepository<Language, Long>, JpaSpecificationExecutor<Language> {}
