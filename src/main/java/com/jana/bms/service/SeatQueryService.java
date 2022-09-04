package com.jana.bms.service;

import com.jana.bms.domain.*; // for static metamodels
import com.jana.bms.domain.Seat;
import com.jana.bms.repository.SeatRepository;
import com.jana.bms.service.criteria.SeatCriteria;
import com.jana.bms.service.dto.SeatDTO;
import com.jana.bms.service.mapper.SeatMapper;
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
 * Service for executing complex queries for {@link Seat} entities in the database.
 * The main input is a {@link SeatCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SeatDTO} or a {@link Page} of {@link SeatDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SeatQueryService extends QueryService<Seat> {

    private final Logger log = LoggerFactory.getLogger(SeatQueryService.class);

    private final SeatRepository seatRepository;

    private final SeatMapper seatMapper;

    public SeatQueryService(SeatRepository seatRepository, SeatMapper seatMapper) {
        this.seatRepository = seatRepository;
        this.seatMapper = seatMapper;
    }

    /**
     * Return a {@link List} of {@link SeatDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SeatDTO> findByCriteria(SeatCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Seat> specification = createSpecification(criteria);
        return seatMapper.toDto(seatRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SeatDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SeatDTO> findByCriteria(SeatCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<Seat> specification = createSpecification(criteria);
        return seatRepository.findAll(specification, page).map(seatMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SeatCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Seat> specification = createSpecification(criteria);
        return seatRepository.count(specification);
    }

    /**
     * Function to convert {@link SeatCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Seat> createSpecification(SeatCriteria criteria) {
        Specification<Seat> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getSeatId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSeatId(), Seat_.seatId));
            }
            if (criteria.getSeatNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSeatNumber(), Seat_.seatNumber));
            }
            if (criteria.getSeatDescr() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSeatDescr(), Seat_.seatDescr));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Seat_.type));
            }
            if (criteria.getScreenId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getScreenId(), root -> root.join(Seat_.screen, JoinType.LEFT).get(Screen_.screenId))
                    );
            }
            if (criteria.getShowId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getShowId(), root -> root.join(Seat_.shows, JoinType.LEFT).get(Show_.showId))
                    );
            }
        }
        return specification;
    }
}
