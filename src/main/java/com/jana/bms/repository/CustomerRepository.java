package com.jana.bms.repository;

import com.jana.bms.domain.Customer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Customer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {}
