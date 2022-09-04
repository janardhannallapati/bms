package com.jana.bms.web.rest;

import com.jana.bms.repository.SeatRepository;
import com.jana.bms.service.SeatQueryService;
import com.jana.bms.service.SeatService;
import com.jana.bms.service.criteria.SeatCriteria;
import com.jana.bms.service.dto.SeatDTO;
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
 * REST controller for managing {@link com.jana.bms.domain.Seat}.
 */
@RestController
@RequestMapping("/api")
public class SeatResource {

    private final Logger log = LoggerFactory.getLogger(SeatResource.class);

    private static final String ENTITY_NAME = "seat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SeatService seatService;

    private final SeatRepository seatRepository;

    private final SeatQueryService seatQueryService;

    public SeatResource(SeatService seatService, SeatRepository seatRepository, SeatQueryService seatQueryService) {
        this.seatService = seatService;
        this.seatRepository = seatRepository;
        this.seatQueryService = seatQueryService;
    }

    /**
     * {@code POST  /seats} : Create a new seat.
     *
     * @param seatDTO the seatDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new seatDTO, or with status {@code 400 (Bad Request)} if the seat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/seats")
    public ResponseEntity<SeatDTO> createSeat(@Valid @RequestBody SeatDTO seatDTO) throws URISyntaxException {
        log.debug("REST request to save Seat : {}", seatDTO);
        if (seatDTO.getSeatId() != null) {
            throw new BadRequestAlertException("A new seat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SeatDTO result = seatService.save(seatDTO);
        return ResponseEntity
            .created(new URI("/api/seats/" + result.getSeatId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getSeatId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /seats/:seatId} : Updates an existing seat.
     *
     * @param seatId the id of the seatDTO to save.
     * @param seatDTO the seatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated seatDTO,
     * or with status {@code 400 (Bad Request)} if the seatDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the seatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/seats/{seatId}")
    public ResponseEntity<SeatDTO> updateSeat(
        @PathVariable(value = "seatId", required = false) final Long seatId,
        @Valid @RequestBody SeatDTO seatDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Seat : {}, {}", seatId, seatDTO);
        if (seatDTO.getSeatId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(seatId, seatDTO.getSeatId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!seatRepository.existsById(seatId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SeatDTO result = seatService.update(seatDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, seatDTO.getSeatId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /seats/:seatId} : Partial updates given fields of an existing seat, field will ignore if it is null
     *
     * @param seatId the id of the seatDTO to save.
     * @param seatDTO the seatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated seatDTO,
     * or with status {@code 400 (Bad Request)} if the seatDTO is not valid,
     * or with status {@code 404 (Not Found)} if the seatDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the seatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/seats/{seatId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SeatDTO> partialUpdateSeat(
        @PathVariable(value = "seatId", required = false) final Long seatId,
        @NotNull @RequestBody SeatDTO seatDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Seat partially : {}, {}", seatId, seatDTO);
        if (seatDTO.getSeatId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(seatId, seatDTO.getSeatId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!seatRepository.existsById(seatId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SeatDTO> result = seatService.partialUpdate(seatDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, seatDTO.getSeatId().toString())
        );
    }

    /**
     * {@code GET  /seats} : get all the seats.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of seats in body.
     */
    @GetMapping("/seats")
    public ResponseEntity<List<SeatDTO>> getAllSeats(
        SeatCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Seats by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        Page<SeatDTO> page = seatQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /seats/count} : count all the seats.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/seats/count")
    public ResponseEntity<Long> countSeats(SeatCriteria criteria) {
        log.debug("REST request to count Seats by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        return ResponseEntity.ok().body(seatQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /seats/:id} : get the "id" seat.
     *
     * @param id the id of the seatDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the seatDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/seats/{id}")
    public ResponseEntity<SeatDTO> getSeat(@PathVariable Long id) {
        log.debug("REST request to get Seat : {}", id);
        Optional<SeatDTO> seatDTO = seatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(seatDTO);
    }

    /**
     * {@code DELETE  /seats/:id} : delete the "id" seat.
     *
     * @param id the id of the seatDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/seats/{id}")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long id) {
        log.debug("REST request to delete Seat : {}", id);
        seatService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
