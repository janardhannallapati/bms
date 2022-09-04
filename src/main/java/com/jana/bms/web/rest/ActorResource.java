package com.jana.bms.web.rest;

import com.jana.bms.repository.ActorRepository;
import com.jana.bms.service.ActorQueryService;
import com.jana.bms.service.ActorService;
import com.jana.bms.service.criteria.ActorCriteria;
import com.jana.bms.service.dto.ActorDTO;
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
 * REST controller for managing {@link com.jana.bms.domain.Actor}.
 */
@RestController
@RequestMapping("/api")
public class ActorResource {

    private final Logger log = LoggerFactory.getLogger(ActorResource.class);

    private static final String ENTITY_NAME = "actor";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ActorService actorService;

    private final ActorRepository actorRepository;

    private final ActorQueryService actorQueryService;

    public ActorResource(ActorService actorService, ActorRepository actorRepository, ActorQueryService actorQueryService) {
        this.actorService = actorService;
        this.actorRepository = actorRepository;
        this.actorQueryService = actorQueryService;
    }

    /**
     * {@code POST  /actors} : Create a new actor.
     *
     * @param actorDTO the actorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new actorDTO, or with status {@code 400 (Bad Request)} if the actor has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/actors")
    public ResponseEntity<ActorDTO> createActor(@Valid @RequestBody ActorDTO actorDTO) throws URISyntaxException {
        log.debug("REST request to save Actor : {}", actorDTO);
        if (actorDTO.getActorId() != null) {
            throw new BadRequestAlertException("A new actor cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ActorDTO result = actorService.save(actorDTO);
        return ResponseEntity
            .created(new URI("/api/actors/" + result.getActorId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getActorId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /actors/:actorId} : Updates an existing actor.
     *
     * @param actorId the id of the actorDTO to save.
     * @param actorDTO the actorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated actorDTO,
     * or with status {@code 400 (Bad Request)} if the actorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the actorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/actors/{actorId}")
    public ResponseEntity<ActorDTO> updateActor(
        @PathVariable(value = "actorId", required = false) final Long actorId,
        @Valid @RequestBody ActorDTO actorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Actor : {}, {}", actorId, actorDTO);
        if (actorDTO.getActorId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(actorId, actorDTO.getActorId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!actorRepository.existsById(actorId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ActorDTO result = actorService.update(actorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, actorDTO.getActorId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /actors/:actorId} : Partial updates given fields of an existing actor, field will ignore if it is null
     *
     * @param actorId the id of the actorDTO to save.
     * @param actorDTO the actorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated actorDTO,
     * or with status {@code 400 (Bad Request)} if the actorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the actorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the actorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/actors/{actorId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ActorDTO> partialUpdateActor(
        @PathVariable(value = "actorId", required = false) final Long actorId,
        @NotNull @RequestBody ActorDTO actorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Actor partially : {}, {}", actorId, actorDTO);
        if (actorDTO.getActorId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(actorId, actorDTO.getActorId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!actorRepository.existsById(actorId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ActorDTO> result = actorService.partialUpdate(actorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, actorDTO.getActorId().toString())
        );
    }

    /**
     * {@code GET  /actors} : get all the actors.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of actors in body.
     */
    @GetMapping("/actors")
    public ResponseEntity<List<ActorDTO>> getAllActors(
        ActorCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Actors by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        Page<ActorDTO> page = actorQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /actors/count} : count all the actors.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/actors/count")
    public ResponseEntity<Long> countActors(ActorCriteria criteria) {
        log.debug("REST request to count Actors by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        return ResponseEntity.ok().body(actorQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /actors/:id} : get the "id" actor.
     *
     * @param id the id of the actorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the actorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/actors/{id}")
    public ResponseEntity<ActorDTO> getActor(@PathVariable Long id) {
        log.debug("REST request to get Actor : {}", id);
        Optional<ActorDTO> actorDTO = actorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(actorDTO);
    }

    /**
     * {@code DELETE  /actors/:id} : delete the "id" actor.
     *
     * @param id the id of the actorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/actors/{id}")
    public ResponseEntity<Void> deleteActor(@PathVariable Long id) {
        log.debug("REST request to delete Actor : {}", id);
        actorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
