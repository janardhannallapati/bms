package com.jana.bms.web.rest;

import com.jana.bms.repository.CustomerRepository;
import com.jana.bms.service.CustomerQueryService;
import com.jana.bms.service.CustomerService;
import com.jana.bms.service.criteria.CustomerCriteria;
import com.jana.bms.service.dto.CustomerDTO;
import com.jana.bms.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.jana.bms.domain.Customer}.
 */
@RestController
@RequestMapping("/api")
public class CustomerResource {

    private final Logger log = LoggerFactory.getLogger(CustomerResource.class);

    private static final String ENTITY_NAME = "customer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerService customerService;

    private final CustomerRepository customerRepository;

    private final CustomerQueryService customerQueryService;

    public CustomerResource(
        CustomerService customerService,
        CustomerRepository customerRepository,
        CustomerQueryService customerQueryService
    ) {
        this.customerService = customerService;
        this.customerRepository = customerRepository;
        this.customerQueryService = customerQueryService;
    }

    /**
     * {@code POST  /customers} : Create a new customer.
     *
     * @param customerDTO the customerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerDTO, or with status {@code 400 (Bad Request)} if the customer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customers")
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) throws URISyntaxException {
        log.debug("REST request to save Customer : {}", customerDTO);
        if (customerDTO.getCustomerId() != null) {
            throw new BadRequestAlertException("A new customer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerDTO result = customerService.save(customerDTO);
        return ResponseEntity
            .created(new URI("/api/customers/" + result.getCustomerId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getCustomerId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customers/:customerId} : Updates an existing customer.
     *
     * @param customerId the id of the customerDTO to save.
     * @param customerDTO the customerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerDTO,
     * or with status {@code 400 (Bad Request)} if the customerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customers/{customerId}")
    public ResponseEntity<CustomerDTO> updateCustomer(
        @PathVariable(value = "customerId", required = false) final Long customerId,
        @Valid @RequestBody CustomerDTO customerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Customer : {}, {}", customerId, customerDTO);
        if (customerDTO.getCustomerId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(customerId, customerDTO.getCustomerId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerRepository.existsById(customerId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CustomerDTO result = customerService.update(customerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerDTO.getCustomerId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /customers/:customerId} : Partial updates given fields of an existing customer, field will ignore if it is null
     *
     * @param customerId the id of the customerDTO to save.
     * @param customerDTO the customerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerDTO,
     * or with status {@code 400 (Bad Request)} if the customerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/customers/{customerId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerDTO> partialUpdateCustomer(
        @PathVariable(value = "customerId", required = false) final Long customerId,
        @NotNull @RequestBody CustomerDTO customerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Customer partially : {}, {}", customerId, customerDTO);
        if (customerDTO.getCustomerId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(customerId, customerDTO.getCustomerId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerRepository.existsById(customerId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerDTO> result = customerService.partialUpdate(customerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerDTO.getCustomerId().toString())
        );
    }

    /**
     * {@code GET  /customers} : get all the customers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customers in body.
     */
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers(
        CustomerCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Customers by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        Page<CustomerDTO> page = customerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customers/count} : count all the customers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/customers/count")
    public ResponseEntity<Long> countCustomers(CustomerCriteria criteria) {
        log.debug("REST request to count Customers by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        return ResponseEntity.ok().body(customerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /customers/:id} : get the "id" customer.
     *
     * @param id the id of the customerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerDTO> getCustomer(@PathVariable Long id) {
        log.debug("REST request to get Customer : {}", id);
        Optional<CustomerDTO> customerDTO = customerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerDTO);
    }

    /**
     * {@code DELETE  /customers/:id} : delete the "id" customer.
     *
     * @param id the id of the customerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customers/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        log.debug("REST request to delete Customer : {}", id);
        customerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
