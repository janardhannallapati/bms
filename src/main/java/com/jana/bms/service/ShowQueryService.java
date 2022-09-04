package com.jana.bms.service;

import com.jana.bms.domain.*; // for static metamodels
import com.jana.bms.domain.Show;
import com.jana.bms.repository.ShowRepository;
import com.jana.bms.service.criteria.ShowCriteria;
import com.jana.bms.service.dto.ShowDTO;
import com.jana.bms.service.mapper.ShowMapper;
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
 * Service for executing complex queries for {@link Show} entities in the database.
 * The main input is a {@link ShowCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ShowDTO} or a {@link Page} of {@link ShowDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShowQueryService extends QueryService<Show> {

    private final Logger log = LoggerFactory.getLogger(ShowQueryService.class);

    private final ShowRepository showRepository;

    private final ShowMapper showMapper;

    public ShowQueryService(ShowRepository showRepository, ShowMapper showMapper) {
        this.showRepository = showRepository;
        this.showMapper = showMapper;
    }

    /**
     * Return a {@link List} of {@link ShowDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ShowDTO> findByCriteria(ShowCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Show> specification = createSpecification(criteria);
        return showMapper.toDto(showRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ShowDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShowDTO> findByCriteria(ShowCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<Show> specification = createSpecification(criteria);
        return showRepository.findAll(specification, page).map(showMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShowCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Show> specification = createSpecification(criteria);
        return showRepository.count(specification);
    }

    /**
     * Function to convert {@link ShowCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Show> createSpecification(ShowCriteria criteria) {
        Specification<Show> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getShowId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShowId(), Show_.showId));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Show_.date));
            }
            if (criteria.getStartTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartTime(), Show_.startTime));
            }
            if (criteria.getEndTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndTime(), Show_.endTime));
            }
            if (criteria.getMovieId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getMovieId(), root -> root.join(Show_.movie, JoinType.LEFT).get(Movie_.movieId))
                    );
            }
            if (criteria.getScreenId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getScreenId(), root -> root.join(Show_.screen, JoinType.LEFT).get(Screen_.screenId))
                    );
            }
            if (criteria.getSeatId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSeatId(), root -> root.join(Show_.seats, JoinType.LEFT).get(Seat_.seatId))
                    );
            }
        }
        return specification;
    }
}
