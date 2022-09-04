package com.jana.bms.service;

import com.jana.bms.domain.*; // for static metamodels
import com.jana.bms.domain.Screen;
import com.jana.bms.repository.ScreenRepository;
import com.jana.bms.service.criteria.ScreenCriteria;
import com.jana.bms.service.dto.ScreenDTO;
import com.jana.bms.service.mapper.ScreenMapper;
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
 * Service for executing complex queries for {@link Screen} entities in the database.
 * The main input is a {@link ScreenCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ScreenDTO} or a {@link Page} of {@link ScreenDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ScreenQueryService extends QueryService<Screen> {

    private final Logger log = LoggerFactory.getLogger(ScreenQueryService.class);

    private final ScreenRepository screenRepository;

    private final ScreenMapper screenMapper;

    public ScreenQueryService(ScreenRepository screenRepository, ScreenMapper screenMapper) {
        this.screenRepository = screenRepository;
        this.screenMapper = screenMapper;
    }

    /**
     * Return a {@link List} of {@link ScreenDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ScreenDTO> findByCriteria(ScreenCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Screen> specification = createSpecification(criteria);
        return screenMapper.toDto(screenRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ScreenDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ScreenDTO> findByCriteria(ScreenCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<Screen> specification = createSpecification(criteria);
        return screenRepository.findAll(specification, page).map(screenMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ScreenCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Screen> specification = createSpecification(criteria);
        return screenRepository.count(specification);
    }

    /**
     * Function to convert {@link ScreenCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Screen> createSpecification(ScreenCriteria criteria) {
        Specification<Screen> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getScreenId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getScreenId(), Screen_.screenId));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Screen_.name));
            }
            if (criteria.getTotalSeats() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalSeats(), Screen_.totalSeats));
            }
            if (criteria.getTheatreId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTheatreId(),
                            root -> root.join(Screen_.theatre, JoinType.LEFT).get(Theatre_.theatreId)
                        )
                    );
            }
        }
        return specification;
    }
}
