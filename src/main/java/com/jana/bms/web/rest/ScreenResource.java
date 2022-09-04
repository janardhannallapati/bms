package com.jana.bms.web.rest;

import com.jana.bms.repository.ScreenRepository;
import com.jana.bms.service.ScreenQueryService;
import com.jana.bms.service.ScreenService;
import com.jana.bms.service.criteria.ScreenCriteria;
import com.jana.bms.service.dto.ScreenDTO;
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
 * REST controller for managing {@link com.jana.bms.domain.Screen}.
 */
@RestController
@RequestMapping("/api")
public class ScreenResource {

    private final Logger log = LoggerFactory.getLogger(ScreenResource.class);

    private static final String ENTITY_NAME = "screen";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScreenService screenService;

    private final ScreenRepository screenRepository;

    private final ScreenQueryService screenQueryService;

    public ScreenResource(ScreenService screenService, ScreenRepository screenRepository, ScreenQueryService screenQueryService) {
        this.screenService = screenService;
        this.screenRepository = screenRepository;
        this.screenQueryService = screenQueryService;
    }

    /**
     * {@code POST  /screens} : Create a new screen.
     *
     * @param screenDTO the screenDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new screenDTO, or with status {@code 400 (Bad Request)} if the screen has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/screens")
    public ResponseEntity<ScreenDTO> createScreen(@Valid @RequestBody ScreenDTO screenDTO) throws URISyntaxException {
        log.debug("REST request to save Screen : {}", screenDTO);
        if (screenDTO.getScreenId() != null) {
            throw new BadRequestAlertException("A new screen cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ScreenDTO result = screenService.save(screenDTO);
        return ResponseEntity
            .created(new URI("/api/screens/" + result.getScreenId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getScreenId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /screens/:screenId} : Updates an existing screen.
     *
     * @param screenId the id of the screenDTO to save.
     * @param screenDTO the screenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated screenDTO,
     * or with status {@code 400 (Bad Request)} if the screenDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the screenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/screens/{screenId}")
    public ResponseEntity<ScreenDTO> updateScreen(
        @PathVariable(value = "screenId", required = false) final Long screenId,
        @Valid @RequestBody ScreenDTO screenDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Screen : {}, {}", screenId, screenDTO);
        if (screenDTO.getScreenId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(screenId, screenDTO.getScreenId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!screenRepository.existsById(screenId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ScreenDTO result = screenService.update(screenDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, screenDTO.getScreenId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /screens/:screenId} : Partial updates given fields of an existing screen, field will ignore if it is null
     *
     * @param screenId the id of the screenDTO to save.
     * @param screenDTO the screenDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated screenDTO,
     * or with status {@code 400 (Bad Request)} if the screenDTO is not valid,
     * or with status {@code 404 (Not Found)} if the screenDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the screenDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/screens/{screenId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ScreenDTO> partialUpdateScreen(
        @PathVariable(value = "screenId", required = false) final Long screenId,
        @NotNull @RequestBody ScreenDTO screenDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Screen partially : {}, {}", screenId, screenDTO);
        if (screenDTO.getScreenId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(screenId, screenDTO.getScreenId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!screenRepository.existsById(screenId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ScreenDTO> result = screenService.partialUpdate(screenDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, screenDTO.getScreenId().toString())
        );
    }

    /**
     * {@code GET  /screens} : get all the screens.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of screens in body.
     */
    @GetMapping("/screens")
    public ResponseEntity<List<ScreenDTO>> getAllScreens(
        ScreenCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Screens by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        Page<ScreenDTO> page = screenQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /screens/count} : count all the screens.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/screens/count")
    public ResponseEntity<Long> countScreens(ScreenCriteria criteria) {
        log.debug("REST request to count Screens by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        return ResponseEntity.ok().body(screenQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /screens/:id} : get the "id" screen.
     *
     * @param id the id of the screenDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the screenDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/screens/{id}")
    public ResponseEntity<ScreenDTO> getScreen(@PathVariable Long id) {
        log.debug("REST request to get Screen : {}", id);
        Optional<ScreenDTO> screenDTO = screenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(screenDTO);
    }

    /**
     * {@code DELETE  /screens/:id} : delete the "id" screen.
     *
     * @param id the id of the screenDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/screens/{id}")
    public ResponseEntity<Void> deleteScreen(@PathVariable Long id) {
        log.debug("REST request to delete Screen : {}", id);
        screenService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
