package com.jana.bms.web.rest;

import com.jana.bms.repository.TheatreRepository;
import com.jana.bms.service.TheatreQueryService;
import com.jana.bms.service.TheatreService;
import com.jana.bms.service.criteria.TheatreCriteria;
import com.jana.bms.service.dto.TheatreDTO;
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
 * REST controller for managing {@link com.jana.bms.domain.Theatre}.
 */
@RestController
@RequestMapping("/api")
public class TheatreResource {

    private final Logger log = LoggerFactory.getLogger(TheatreResource.class);

    private static final String ENTITY_NAME = "theatre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TheatreService theatreService;

    private final TheatreRepository theatreRepository;

    private final TheatreQueryService theatreQueryService;

    public TheatreResource(TheatreService theatreService, TheatreRepository theatreRepository, TheatreQueryService theatreQueryService) {
        this.theatreService = theatreService;
        this.theatreRepository = theatreRepository;
        this.theatreQueryService = theatreQueryService;
    }

    /**
     * {@code POST  /theatres} : Create a new theatre.
     *
     * @param theatreDTO the theatreDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new theatreDTO, or with status {@code 400 (Bad Request)} if the theatre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/theatres")
    public ResponseEntity<TheatreDTO> createTheatre(@Valid @RequestBody TheatreDTO theatreDTO) throws URISyntaxException {
        log.debug("REST request to save Theatre : {}", theatreDTO);
        if (theatreDTO.getTheatreId() != null) {
            throw new BadRequestAlertException("A new theatre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TheatreDTO result = theatreService.save(theatreDTO);
        return ResponseEntity
            .created(new URI("/api/theatres/" + result.getTheatreId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getTheatreId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /theatres/:theatreId} : Updates an existing theatre.
     *
     * @param theatreId the id of the theatreDTO to save.
     * @param theatreDTO the theatreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated theatreDTO,
     * or with status {@code 400 (Bad Request)} if the theatreDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the theatreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/theatres/{theatreId}")
    public ResponseEntity<TheatreDTO> updateTheatre(
        @PathVariable(value = "theatreId", required = false) final Long theatreId,
        @Valid @RequestBody TheatreDTO theatreDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Theatre : {}, {}", theatreId, theatreDTO);
        if (theatreDTO.getTheatreId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(theatreId, theatreDTO.getTheatreId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!theatreRepository.existsById(theatreId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TheatreDTO result = theatreService.update(theatreDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, theatreDTO.getTheatreId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /theatres/:theatreId} : Partial updates given fields of an existing theatre, field will ignore if it is null
     *
     * @param theatreId the id of the theatreDTO to save.
     * @param theatreDTO the theatreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated theatreDTO,
     * or with status {@code 400 (Bad Request)} if the theatreDTO is not valid,
     * or with status {@code 404 (Not Found)} if the theatreDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the theatreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/theatres/{theatreId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TheatreDTO> partialUpdateTheatre(
        @PathVariable(value = "theatreId", required = false) final Long theatreId,
        @NotNull @RequestBody TheatreDTO theatreDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Theatre partially : {}, {}", theatreId, theatreDTO);
        if (theatreDTO.getTheatreId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(theatreId, theatreDTO.getTheatreId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!theatreRepository.existsById(theatreId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TheatreDTO> result = theatreService.partialUpdate(theatreDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, theatreDTO.getTheatreId().toString())
        );
    }

    /**
     * {@code GET  /theatres} : get all the theatres.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of theatres in body.
     */
    @GetMapping("/theatres")
    public ResponseEntity<List<TheatreDTO>> getAllTheatres(
        TheatreCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Theatres by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        Page<TheatreDTO> page = theatreQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /theatres/count} : count all the theatres.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/theatres/count")
    public ResponseEntity<Long> countTheatres(TheatreCriteria criteria) {
        log.debug("REST request to count Theatres by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        return ResponseEntity.ok().body(theatreQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /theatres/:id} : get the "id" theatre.
     *
     * @param id the id of the theatreDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the theatreDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/theatres/{id}")
    public ResponseEntity<TheatreDTO> getTheatre(@PathVariable Long id) {
        log.debug("REST request to get Theatre : {}", id);
        Optional<TheatreDTO> theatreDTO = theatreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(theatreDTO);
    }

    /**
     * {@code DELETE  /theatres/:id} : delete the "id" theatre.
     *
     * @param id the id of the theatreDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/theatres/{id}")
    public ResponseEntity<Void> deleteTheatre(@PathVariable Long id) {
        log.debug("REST request to delete Theatre : {}", id);
        theatreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
