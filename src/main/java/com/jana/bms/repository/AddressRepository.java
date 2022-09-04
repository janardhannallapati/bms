package com.jana.bms.repository;

import com.jana.bms.domain.Address;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Address entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AddressRepository extends JpaRepository<Address, Long>, JpaSpecificationExecutor<Address> {}
