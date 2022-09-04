package com.jana.bms.service.criteria;

import com.jana.bms.domain.enumeration.SeatStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.jana.bms.domain.ShowSeat} entity. This class is used
 * in {@link com.jana.bms.web.rest.ShowSeatResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /show-seats?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class ShowSeatCriteria implements Serializable, Criteria {

    /**
     * Class for filtering SeatStatus
     */
    public static class SeatStatusFilter extends Filter<SeatStatus> {

        public SeatStatusFilter() {}

        public SeatStatusFilter(SeatStatusFilter filter) {
            super(filter);
        }

        @Override
        public SeatStatusFilter copy() {
            return new SeatStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter showSeatId;

    private BigDecimalFilter price;

    private SeatStatusFilter status;

    private LongFilter seatId;

    private LongFilter showId;

    private LongFilter bookingId;

    private Boolean distinct;

    public ShowSeatCriteria() {}

    public ShowSeatCriteria(ShowSeatCriteria other) {
        this.showSeatId = other.showSeatId == null ? null : other.showSeatId.copy();
        this.price = other.price == null ? null : other.price.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.seatId = other.seatId == null ? null : other.seatId.copy();
        this.showId = other.showId == null ? null : other.showId.copy();
        this.bookingId = other.bookingId == null ? null : other.bookingId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ShowSeatCriteria copy() {
        return new ShowSeatCriteria(this);
    }

    public LongFilter getShowSeatId() {
        return showSeatId;
    }

    public LongFilter showSeatId() {
        if (showSeatId == null) {
            showSeatId = new LongFilter();
        }
        return showSeatId;
    }

    public void setShowSeatId(LongFilter showSeatId) {
        this.showSeatId = showSeatId;
    }

    public BigDecimalFilter getPrice() {
        return price;
    }

    public BigDecimalFilter price() {
        if (price == null) {
            price = new BigDecimalFilter();
        }
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
    }

    public SeatStatusFilter getStatus() {
        return status;
    }

    public SeatStatusFilter status() {
        if (status == null) {
            status = new SeatStatusFilter();
        }
        return status;
    }

    public void setStatus(SeatStatusFilter status) {
        this.status = status;
    }

    public LongFilter getSeatId() {
        return seatId;
    }

    public LongFilter seatId() {
        if (seatId == null) {
            seatId = new LongFilter();
        }
        return seatId;
    }

    public void setSeatId(LongFilter seatId) {
        this.seatId = seatId;
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
        final ShowSeatCriteria that = (ShowSeatCriteria) o;
        return (
            Objects.equals(showSeatId, that.showSeatId) &&
            Objects.equals(price, that.price) &&
            Objects.equals(status, that.status) &&
            Objects.equals(seatId, that.seatId) &&
            Objects.equals(showId, that.showId) &&
            Objects.equals(bookingId, that.bookingId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(showSeatId, price, status, seatId, showId, bookingId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShowSeatCriteria{" +
            (showSeatId != null ? "showSeatId=" + showSeatId + ", " : "") +
            (price != null ? "price=" + price + ", " : "") +
            (status != null ? "status=" + status + ", " : "") +
            (seatId != null ? "seatId=" + seatId + ", " : "") +
            (showId != null ? "showId=" + showId + ", " : "") +
            (bookingId != null ? "bookingId=" + bookingId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
