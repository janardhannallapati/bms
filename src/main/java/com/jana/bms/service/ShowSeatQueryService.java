package com.jana.bms.service;

import com.jana.bms.domain.*; // for static metamodels
import com.jana.bms.domain.ShowSeat;
import com.jana.bms.repository.ShowSeatRepository;
import com.jana.bms.service.criteria.ShowSeatCriteria;
import com.jana.bms.service.dto.ShowSeatDTO;
import com.jana.bms.service.mapper.ShowSeatMapper;
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
 * Service for executing complex queries for {@link ShowSeat} entities in the database.
 * The main input is a {@link ShowSeatCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ShowSeatDTO} or a {@link Page} of {@link ShowSeatDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShowSeatQueryService extends QueryService<ShowSeat> {

    private final Logger log = LoggerFactory.getLogger(ShowSeatQueryService.class);

    private final ShowSeatRepository showSeatRepository;

    private final ShowSeatMapper showSeatMapper;

    public ShowSeatQueryService(ShowSeatRepository showSeatRepository, ShowSeatMapper showSeatMapper) {
        this.showSeatRepository = showSeatRepository;
        this.showSeatMapper = showSeatMapper;
    }

    /**
     * Return a {@link List} of {@link ShowSeatDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ShowSeatDTO> findByCriteria(ShowSeatCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<ShowSeat> specification = createSpecification(criteria);
        return showSeatMapper.toDto(showSeatRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ShowSeatDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShowSeatDTO> findByCriteria(ShowSeatCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<ShowSeat> specification = createSpecification(criteria);
        return showSeatRepository.findAll(specification, page).map(showSeatMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShowSeatCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<ShowSeat> specification = createSpecification(criteria);
        return showSeatRepository.count(specification);
    }

    /**
     * Function to convert {@link ShowSeatCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShowSeat> createSpecification(ShowSeatCriteria criteria) {
        Specification<ShowSeat> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getShowSeatId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getShowSeatId(), ShowSeat_.showSeatId));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), ShowSeat_.price));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), ShowSeat_.status));
            }
            if (criteria.getSeatId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getSeatId(), root -> root.join(ShowSeat_.seat, JoinType.LEFT).get(Seat_.seatId))
                    );
            }
            if (criteria.getShowId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getShowId(), root -> root.join(ShowSeat_.show, JoinType.LEFT).get(Show_.showId))
                    );
            }
            if (criteria.getBookingId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBookingId(),
                            root -> root.join(ShowSeat_.booking, JoinType.LEFT).get(Booking_.bookingId)
                        )
                    );
            }
        }
        return specification;
    }
}
