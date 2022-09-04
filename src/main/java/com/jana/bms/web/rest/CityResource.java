package com.jana.bms.web.rest;

import com.jana.bms.repository.CityRepository;
import com.jana.bms.service.CityQueryService;
import com.jana.bms.service.CityService;
import com.jana.bms.service.criteria.CityCriteria;
import com.jana.bms.service.dto.CityDTO;
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
 * REST controller for managing {@link com.jana.bms.domain.City}.
 */
@RestController
@RequestMapping("/api")
public class CityResource {

    private final Logger log = LoggerFactory.getLogger(CityResource.class);

    private static final String ENTITY_NAME = "city";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CityService cityService;

    private final CityRepository cityRepository;

    private final CityQueryService cityQueryService;

    public CityResource(CityService cityService, CityRepository cityRepository, CityQueryService cityQueryService) {
        this.cityService = cityService;
        this.cityRepository = cityRepository;
        this.cityQueryService = cityQueryService;
    }

    /**
     * {@code POST  /cities} : Create a new city.
     *
     * @param cityDTO the cityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cityDTO, or with status {@code 400 (Bad Request)} if the city has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cities")
    public ResponseEntity<CityDTO> createCity(@Valid @RequestBody CityDTO cityDTO) throws URISyntaxException {
        log.debug("REST request to save City : {}", cityDTO);
        if (cityDTO.getCityId() != null) {
            throw new BadRequestAlertException("A new city cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CityDTO result = cityService.save(cityDTO);
        return ResponseEntity
            .created(new URI("/api/cities/" + result.getCityId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getCityId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cities/:cityId} : Updates an existing city.
     *
     * @param cityId the id of the cityDTO to save.
     * @param cityDTO the cityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cityDTO,
     * or with status {@code 400 (Bad Request)} if the cityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cities/{cityId}")
    public ResponseEntity<CityDTO> updateCity(
        @PathVariable(value = "cityId", required = false) final Long cityId,
        @Valid @RequestBody CityDTO cityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update City : {}, {}", cityId, cityDTO);
        if (cityDTO.getCityId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(cityId, cityDTO.getCityId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cityRepository.existsById(cityId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CityDTO result = cityService.update(cityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cityDTO.getCityId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cities/:cityId} : Partial updates given fields of an existing city, field will ignore if it is null
     *
     * @param cityId the id of the cityDTO to save.
     * @param cityDTO the cityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cityDTO,
     * or with status {@code 400 (Bad Request)} if the cityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cities/{cityId}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CityDTO> partialUpdateCity(
        @PathVariable(value = "cityId", required = false) final Long cityId,
        @NotNull @RequestBody CityDTO cityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update City partially : {}, {}", cityId, cityDTO);
        if (cityDTO.getCityId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(cityId, cityDTO.getCityId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cityRepository.existsById(cityId)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CityDTO> result = cityService.partialUpdate(cityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cityDTO.getCityId().toString())
        );
    }

    /**
     * {@code GET  /cities} : get all the cities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cities in body.
     */
    @GetMapping("/cities")
    public ResponseEntity<List<CityDTO>> getAllCities(
        CityCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Cities by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        Page<CityDTO> page = cityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cities/count} : count all the cities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cities/count")
    public ResponseEntity<Long> countCities(CityCriteria criteria) {
        log.debug("REST request to count Cities by criteria: {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        return ResponseEntity.ok().body(cityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cities/:id} : get the "id" city.
     *
     * @param id the id of the cityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cities/{id}")
    public ResponseEntity<CityDTO> getCity(@PathVariable Long id) {
        log.debug("REST request to get City : {}", id);
        Optional<CityDTO> cityDTO = cityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cityDTO);
    }

    /**
     * {@code DELETE  /cities/:id} : delete the "id" city.
     *
     * @param id the id of the cityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cities/{id}")
    public ResponseEntity<Void> deleteCity(@PathVariable Long id) {
        log.debug("REST request to delete City : {}", id);
        cityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
