package com.jana.bms.service;

import com.jana.bms.domain.*; // for static metamodels
import com.jana.bms.domain.Booking;
import com.jana.bms.repository.BookingRepository;
import com.jana.bms.service.criteria.BookingCriteria;
import com.jana.bms.service.dto.BookingDTO;
import com.jana.bms.service.mapper.BookingMapper;
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
 * Service for executing complex queries for {@link Booking} entities in the database.
 * The main input is a {@link BookingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BookingDTO} or a {@link Page} of {@link BookingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BookingQueryService extends QueryService<Booking> {

    private final Logger log = LoggerFactory.getLogger(BookingQueryService.class);

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    public BookingQueryService(BookingRepository bookingRepository, BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    /**
     * Return a {@link List} of {@link BookingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BookingDTO> findByCriteria(BookingCriteria criteria) {
        log.debug("find by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Booking> specification = createSpecification(criteria);
        return bookingMapper.toDto(bookingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BookingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BookingDTO> findByCriteria(BookingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria.toString().replaceAll("[\n\r\t]", "_"), page);
        final Specification<Booking> specification = createSpecification(criteria);
        return bookingRepository.findAll(specification, page).map(bookingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BookingCriteria criteria) {
        log.debug("count by criteria : {}", criteria.toString().replaceAll("[\n\r\t]", "_"));
        final Specification<Booking> specification = createSpecification(criteria);
        return bookingRepository.count(specification);
    }

    /**
     * Function to convert {@link BookingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Booking> createSpecification(BookingCriteria criteria) {
        Specification<Booking> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getBookingId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBookingId(), Booking_.bookingId));
            }
            if (criteria.getTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimestamp(), Booking_.timestamp));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), Booking_.status));
            }
            if (criteria.getCustomerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerId(),
                            root -> root.join(Booking_.customer, JoinType.LEFT).get(Customer_.customerId)
                        )
                    );
            }
            if (criteria.getShowId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getShowId(), root -> root.join(Booking_.show, JoinType.LEFT).get(Show_.showId))
                    );
            }
        }
        return specification;
    }
}
