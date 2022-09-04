package com.jana.bms.service;

import com.jana.bms.domain.*; // for static metamodels
import com.jana.bms.domain.Actor;
import com.jana.bms.repository.ActorRepository;
import com.jana.bms.service.criteria.ActorCriteria;
import com.jana.bms.service.dto.ActorDTO;
import com.jana.bms.service.mapper.ActorMapper;
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
 * Service for executing complex queries for {@link Actor} entities in the database.
 * The main input is a {@link ActorCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ActorDTO} or a {@link Page} of {@link ActorDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ActorQueryService extends QueryService<Actor> {

    private final Logger log = LoggerFactory.getLogger(ActorQueryService.class);

    private final ActorRepository actorRepository;

    private final ActorMapper actorMapper;

    public ActorQueryService(ActorRepository actorRepository, ActorMapper actorMapper) {
        this.actorRepository = actorRepository;
        this.actorMapper = actorMapper;
    }

    /**
     * Return a {@link List} of {@link ActorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ActorDTO> findByCriteria(ActorCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Actor> specification = createSpecification(criteria);
        return actorMapper.toDto(actorRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ActorDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ActorDTO> findByCriteria(ActorCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<Actor> specification = createSpecification(criteria);
        return actorRepository.findAll(specification, page).map(actorMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ActorCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Actor> specification = createSpecification(criteria);
        return actorRepository.count(specification);
    }

    /**
     * Function to convert {@link ActorCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Actor> createSpecification(ActorCriteria criteria) {
        Specification<Actor> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getActorId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActorId(), Actor_.actorId));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Actor_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Actor_.lastName));
            }
            if (criteria.getMovieId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMovieId(), root -> root.join(Actor_.movies, JoinType.LEFT).get(Movie_.movieId))
                    );
            }
        }
        return specification;
    }
}
