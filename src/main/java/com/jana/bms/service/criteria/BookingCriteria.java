package com.jana.bms.service.criteria;

import com.jana.bms.domain.enumeration.BookingStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.jana.bms.domain.Booking} entity. This class is used
 * in {@link com.jana.bms.web.rest.BookingResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /bookings?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class BookingCriteria implements Serializable, Criteria {

    /**
     * Class for filtering BookingStatus
     */
    public static class BookingStatusFilter extends Filter<BookingStatus> {

        public BookingStatusFilter() {}

        public BookingStatusFilter(BookingStatusFilter filter) {
            super(filter);
        }

        @Override
        public BookingStatusFilter copy() {
            return new BookingStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter bookingId;

    private InstantFilter timestamp;

    private BookingStatusFilter status;

    private LongFilter customerId;

    private LongFilter showId;

    private Boolean distinct;

    public BookingCriteria() {}

    public BookingCriteria(BookingCriteria other) {
        this.bookingId = other.bookingId == null ? null : other.bookingId.copy();
        this.timestamp = other.timestamp == null ? null : other.timestamp.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.showId = other.showId == null ? null : other.showId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public BookingCriteria copy() {
        return new BookingCriteria(this);
    }

    public LongFilter getBookingId() {
        return bookingId;
    }

    public LongFilter bookingId() {
        if (bookingId == null) {
            bookingId = new LongFilter();
        }
        return bookingId;
    }

    public void setBookingId(LongFilter bookingId) {
        this.bookingId = bookingId;
    }

    public InstantFilter getTimestamp() {
        return timestamp;
    }

    public InstantFilter timestamp() {
        if (timestamp == null) {
            timestamp = new InstantFilter();
        }
        return timestamp;
    }

    public void setTimestamp(InstantFilter timestamp) {
        this.timestamp = timestamp;
    }

    public BookingStatusFilter getStatus() {
        return status;
    }

    public BookingStatusFilter status() {
        if (status == null) {
            status = new BookingStatusFilter();
        }
        return status;
    }

    public void setStatus(BookingStatusFilter status) {
        this.status = status;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public LongFilter customerId() {
        if (customerId == null) {
            customerId = new LongFilter();
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getShowId() {
        return showId;
    }

    public LongFilter showId() {
        if (showId == null) {
            showId = new LongFilter();
        }
        return showId;
    }

    public void setShowId(LongFilter showId) {
        this.showId = showId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BookingCriteria that = (BookingCriteria) o;
        return (
            Objects.equals(bookingId, that.bookingId) &&
            Objects.equals(timestamp, that.timestamp) &&
            Objects.equals(status, that.status) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(showId, that.showId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingId, timestamp, status, customerId, showId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingCriteria{" +
            (bookingId != null ? "bookingId=" + bookingId + ", " : "") +
            (timestamp != null ? "timestamp=" + timestamp + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (showId != null ? "showId=" + showId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
