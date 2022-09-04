package com.jana.bms.web.rest;

import com.jana.bms.repository.MovieRepository;
import com.jana.bms.service.MovieQueryService;
import com.jana.bms.service.MovieService;
import com.jana.bms.service.criteria.MovieCriteria;
import com.jana.bms.service.dto.MovieDTO;
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
 * REST controller for managing {@link com.jana.bms.domain.Movie}.
 */
@RestController
@RequestMapping("/api")
public class MovieResource {

    private final Logger log = LoggerFactory.getLogger(MovieResource.class);

    private static final String ENTITY_NAME = "movie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MovieService movieService;

    private final MovieRepository movieRepository;

    private final MovieQueryService movieQueryService;

    public MovieResource(MovieService movieService, MovieRepository movieRepository, MovieQueryService movieQueryService) {
        this.movieService = movieService;
        this.movieRepository = movieRepository;
        this.movieQueryService = movieQueryService;
    }

    /**
     * {@code POST  /movies} : Create a new movie.
     *
     * @param movieDTO the movieDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new movieDTO, or with status {@code 400 (Bad Request)} if the movie has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/movies")
    public ResponseEntity<MovieDTO> createMovie(@Valid @RequestBody MovieDTO movieDTO) throws URISyntaxException {
        log.debug("REST request to save Movie : {}", movieDTO);
        if (movieDTO.getMovieId() != null) {
            throw new BadRequestAlertException("A new movie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MovieDTO result = movieService.save(movieDTO);
        return ResponseEntity
            .created(new URI("/api/movies/" + result.getMovieId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getMovieId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /movies/:movieId} : Updates an existing movie.
     *
     * @param movieId the id of the movieDTO to save.
     * @param movieDTO the movieDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated movieDTO,
     * or with status {@code 400 (Bad Request)} if the movieDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the movieDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/movies/{movieId}")
    public ResponseEntity<MovieDTO> updateMovie(
        @PathVariable(value = "movieId", required = false) final Long movieId,
        @Valid @RequestBody MovieDTO movieDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Movie : {}, {}", movieId, movieDTO);
        if (movieDTO.getMovieId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(movieId, movieDTO.getMovieId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!movieRepository.existsById(movieId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MovieDTO result = movieService.update(movieDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, movieDTO.getMovieId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /movies/:movieId} : Partial updates given fields of an existing movie, field will ignore if it is null
     *
     * @param movieId the id of the movieDTO to save.
     * @param movieDTO the movieDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated movieDTO,
     * or with status {@code 400 (Bad Request)} if the movieDTO is not valid,
     * or with status {@code 404 (Not Found)} if the movieDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the movieDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/movies/{movieId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MovieDTO> partialUpdateMovie(
        @PathVariable(value = "movieId", required = false) final Long movieId,
        @NotNull @RequestBody MovieDTO movieDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Movie partially : {}, {}", movieId, movieDTO);
        if (movieDTO.getMovieId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(movieId, movieDTO.getMovieId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!movieRepository.existsById(movieId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MovieDTO> result = movieService.partialUpdate(movieDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, movieDTO.getMovieId().toString())
        );
    }

    /**
     * {@code GET  /movies} : get all the movies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of movies in body.
     */
    @GetMapping("/movies")
    public ResponseEntity<List<MovieDTO>> getAllMovies(
        MovieCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Movies by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        Page<MovieDTO> page = movieQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /movies/count} : count all the movies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/movies/count")
    public ResponseEntity<Long> countMovies(MovieCriteria criteria) {
        log.debug("REST request to count Movies by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        return ResponseEntity.ok().body(movieQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /movies/:id} : get the "id" movie.
     *
     * @param id the id of the movieDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the movieDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/movies/{id}")
    public ResponseEntity<MovieDTO> getMovie(@PathVariable Long id) {
        log.debug("REST request to get Movie : {}", id);
        Optional<MovieDTO> movieDTO = movieService.findOne(id);
        return ResponseUtil.wrapOrNotFound(movieDTO);
    }

    /**
     * {@code DELETE  /movies/:id} : delete the "id" movie.
     *
     * @param id the id of the movieDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        log.debug("REST request to delete Movie : {}", id);
        movieService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
