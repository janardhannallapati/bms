package com.jana.bms.web.rest;

import com.jana.bms.repository.GenreRepository;
import com.jana.bms.service.GenreQueryService;
import com.jana.bms.service.GenreService;
import com.jana.bms.service.criteria.GenreCriteria;
import com.jana.bms.service.dto.GenreDTO;
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
 * REST controller for managing {@link com.jana.bms.domain.Genre}.
 */
@RestController
@RequestMapping("/api")
public class GenreResource {

    private final Logger log = LoggerFactory.getLogger(GenreResource.class);

    private static final String ENTITY_NAME = "genre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GenreService genreService;

    private final GenreRepository genreRepository;

    private final GenreQueryService genreQueryService;

    public GenreResource(GenreService genreService, GenreRepository genreRepository, GenreQueryService genreQueryService) {
        this.genreService = genreService;
        this.genreRepository = genreRepository;
        this.genreQueryService = genreQueryService;
    }

    /**
     * {@code POST  /genres} : Create a new genre.
     *
     * @param genreDTO the genreDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new genreDTO, or with status {@code 400 (Bad Request)} if the genre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/genres")
    public ResponseEntity<GenreDTO> createGenre(@Valid @RequestBody GenreDTO genreDTO) throws URISyntaxException {
        log.debug("REST request to save Genre : {}", genreDTO);
        if (genreDTO.getGenreId() != null) {
            throw new BadRequestAlertException("A new genre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GenreDTO result = genreService.save(genreDTO);
        return ResponseEntity
            .created(new URI("/api/genres/" + result.getGenreId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getGenreId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /genres/:genreId} : Updates an existing genre.
     *
     * @param genreId the id of the genreDTO to save.
     * @param genreDTO the genreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated genreDTO,
     * or with status {@code 400 (Bad Request)} if the genreDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the genreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/genres/{genreId}")
    public ResponseEntity<GenreDTO> updateGenre(
        @PathVariable(value = "genreId", required = false) final Long genreId,
        @Valid @RequestBody GenreDTO genreDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Genre : {}, {}", genreId, genreDTO);
        if (genreDTO.getGenreId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(genreId, genreDTO.getGenreId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!genreRepository.existsById(genreId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GenreDTO result = genreService.update(genreDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, genreDTO.getGenreId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /genres/:genreId} : Partial updates given fields of an existing genre, field will ignore if it is null
     *
     * @param genreId the id of the genreDTO to save.
     * @param genreDTO the genreDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated genreDTO,
     * or with status {@code 400 (Bad Request)} if the genreDTO is not valid,
     * or with status {@code 404 (Not Found)} if the genreDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the genreDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/genres/{genreId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GenreDTO> partialUpdateGenre(
        @PathVariable(value = "genreId", required = false) final Long genreId,
        @NotNull @RequestBody GenreDTO genreDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Genre partially : {}, {}", genreId, genreDTO);
        if (genreDTO.getGenreId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(genreId, genreDTO.getGenreId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!genreRepository.existsById(genreId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GenreDTO> result = genreService.partialUpdate(genreDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, genreDTO.getGenreId().toString())
        );
    }

    /**
     * {@code GET  /genres} : get all the genres.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of genres in body.
     */
    @GetMapping("/genres")
    public ResponseEntity<List<GenreDTO>> getAllGenres(
        GenreCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Genres by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        Page<GenreDTO> page = genreQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /genres/count} : count all the genres.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/genres/count")
    public ResponseEntity<Long> countGenres(GenreCriteria criteria) {
        log.debug("REST request to count Genres by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        return ResponseEntity.ok().body(genreQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /genres/:id} : get the "id" genre.
     *
     * @param id the id of the genreDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the genreDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/genres/{id}")
    public ResponseEntity<GenreDTO> getGenre(@PathVariable Long id) {
        log.debug("REST request to get Genre : {}", id);
        Optional<GenreDTO> genreDTO = genreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(genreDTO);
    }

    /**
     * {@code DELETE  /genres/:id} : delete the "id" genre.
     *
     * @param id the id of the genreDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/genres/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable Long id) {
        log.debug("REST request to delete Genre : {}", id);
        genreService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
