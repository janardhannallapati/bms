package com.jana.bms.service;

import com.jana.bms.domain.*; // for static metamodels
import com.jana.bms.domain.Theatre;
import com.jana.bms.repository.TheatreRepository;
import com.jana.bms.service.criteria.TheatreCriteria;
import com.jana.bms.service.dto.TheatreDTO;
import com.jana.bms.service.mapper.TheatreMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Theatre} entities in the database.
 * The main input is a {@link TheatreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TheatreDTO} or a {@link Page} of {@link TheatreDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TheatreQueryService extends QueryService<Theatre> {

    private final Logger log = LoggerFactory.getLogger(TheatreQueryService.class);

    private final TheatreRepository theatreRepository;

    private final TheatreMapper theatreMapper;

    public TheatreQueryService(TheatreRepository theatreRepository, TheatreMapper theatreMapper) {
        this.theatreRepository = theatreRepository;
        this.theatreMapper = theatreMapper;
    }

    /**
     * Return a {@link List} of {@link TheatreDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TheatreDTO> findByCriteria(TheatreCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Theatre> specification = createSpecification(criteria);
        return theatreMapper.toDto(theatreRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TheatreDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TheatreDTO> findByCriteria(TheatreCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<Theatre> specification = createSpecification(criteria);
        return theatreRepository.findAll(specification, page).map(theatreMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TheatreCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Theatre> specification = createSpecification(criteria);
        return theatreRepository.count(specification);
    }

    /**
     * Function to convert {@link TheatreCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Theatre> createSpecification(TheatreCriteria criteria) {
        Specification<Theatre> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getTheatreId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTheatreId(), Theatre_.theatreId));
            }
            if (criteria.getTheatreName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTheatreName(), Theatre_.theatreName));
            }
            if (criteria.getNoOfScreens() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNoOfScreens(), Theatre_.noOfScreens));
            }
        }
        return specification;
    }
}
