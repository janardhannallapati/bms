package com.jana.bms.web.rest;

import com.jana.bms.repository.ShowSeatRepository;
import com.jana.bms.service.ShowSeatQueryService;
import com.jana.bms.service.ShowSeatService;
import com.jana.bms.service.criteria.ShowSeatCriteria;
import com.jana.bms.service.dto.ShowSeatDTO;
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
 * REST controller for managing {@link com.jana.bms.domain.ShowSeat}.
 */
@RestController
@RequestMapping("/api")
public class ShowSeatResource {

    private final Logger log = LoggerFactory.getLogger(ShowSeatResource.class);

    private static final String ENTITY_NAME = "showSeat";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShowSeatService showSeatService;

    private final ShowSeatRepository showSeatRepository;

    private final ShowSeatQueryService showSeatQueryService;

    public ShowSeatResource(
        ShowSeatService showSeatService,
        ShowSeatRepository showSeatRepository,
        ShowSeatQueryService showSeatQueryService
    ) {
        this.showSeatService = showSeatService;
        this.showSeatRepository = showSeatRepository;
        this.showSeatQueryService = showSeatQueryService;
    }

    /**
     * {@code POST  /show-seats} : Create a new showSeat.
     *
     * @param showSeatDTO the showSeatDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new showSeatDTO, or with status {@code 400 (Bad Request)} if the showSeat has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/show-seats")
    public ResponseEntity<ShowSeatDTO> createShowSeat(@Valid @RequestBody ShowSeatDTO showSeatDTO) throws URISyntaxException {
        log.debug("REST request to save ShowSeat : {}", showSeatDTO);
        if (showSeatDTO.getShowSeatId() != null) {
            throw new BadRequestAlertException("A new showSeat cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShowSeatDTO result = showSeatService.save(showSeatDTO);
        return ResponseEntity
            .created(new URI("/api/show-seats/" + result.getShowSeatId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getShowSeatId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /show-seats/:showSeatId} : Updates an existing showSeat.
     *
     * @param showSeatId the id of the showSeatDTO to save.
     * @param showSeatDTO the showSeatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated showSeatDTO,
     * or with status {@code 400 (Bad Request)} if the showSeatDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the showSeatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/show-seats/{showSeatId}")
    public ResponseEntity<ShowSeatDTO> updateShowSeat(
        @PathVariable(value = "showSeatId", required = false) final Long showSeatId,
        @Valid @RequestBody ShowSeatDTO showSeatDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ShowSeat : {}, {}", showSeatId, showSeatDTO);
        if (showSeatDTO.getShowSeatId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(showSeatId, showSeatDTO.getShowSeatId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!showSeatRepository.existsById(showSeatId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ShowSeatDTO result = showSeatService.update(showSeatDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, showSeatDTO.getShowSeatId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /show-seats/:showSeatId} : Partial updates given fields of an existing showSeat, field will ignore if it is null
     *
     * @param showSeatId the id of the showSeatDTO to save.
     * @param showSeatDTO the showSeatDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated showSeatDTO,
     * or with status {@code 400 (Bad Request)} if the showSeatDTO is not valid,
     * or with status {@code 404 (Not Found)} if the showSeatDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the showSeatDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/show-seats/{showSeatId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShowSeatDTO> partialUpdateShowSeat(
        @PathVariable(value = "showSeatId", required = false) final Long showSeatId,
        @NotNull @RequestBody ShowSeatDTO showSeatDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ShowSeat partially : {}, {}", showSeatId, showSeatDTO);
        if (showSeatDTO.getShowSeatId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(showSeatId, showSeatDTO.getShowSeatId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!showSeatRepository.existsById(showSeatId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShowSeatDTO> result = showSeatService.partialUpdate(showSeatDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, showSeatDTO.getShowSeatId().toString())
        );
    }

    /**
     * {@code GET  /show-seats} : get all the showSeats.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of showSeats in body.
     */
    @GetMapping("/show-seats")
    public ResponseEntity<List<ShowSeatDTO>> getAllShowSeats(
        ShowSeatCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ShowSeats by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        Page<ShowSeatDTO> page = showSeatQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /show-seats/count} : count all the showSeats.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/show-seats/count")
    public ResponseEntity<Long> countShowSeats(ShowSeatCriteria criteria) {
        log.debug("REST request to count ShowSeats by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        return ResponseEntity.ok().body(showSeatQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /show-seats/:id} : get the "id" showSeat.
     *
     * @param id the id of the showSeatDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the showSeatDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/show-seats/{id}")
    public ResponseEntity<ShowSeatDTO> getShowSeat(@PathVariable Long id) {
        log.debug("REST request to get ShowSeat : {}", id);
        Optional<ShowSeatDTO> showSeatDTO = showSeatService.findOne(id);
        return ResponseUtil.wrapOrNotFound(showSeatDTO);
    }

    /**
     * {@code DELETE  /show-seats/:id} : delete the "id" showSeat.
     *
     * @param id the id of the showSeatDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/show-seats/{id}")
    public ResponseEntity<Void> deleteShowSeat(@PathVariable Long id) {
        log.debug("REST request to delete ShowSeat : {}", id);
        showSeatService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
