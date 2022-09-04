package com.jana.bms.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.jana.bms.domain.Seat} entity. This class is used
 * in {@link com.jana.bms.web.rest.SeatResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /seats?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class SeatCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter seatId;

    private IntegerFilter seatNumber;

    private StringFilter seatDescr;

    private StringFilter type;

    private LongFilter screenId;

    private LongFilter showId;

    private Boolean distinct;

    public SeatCriteria() {}

    public SeatCriteria(SeatCriteria other) {
        this.seatId = other.seatId == null ? null : other.seatId.copy();
        this.seatNumber = other.seatNumber == null ? null : other.seatNumber.copy();
        this.seatDescr = other.seatDescr == null ? null : other.seatDescr.copy();
        this.type = other.type == null ? null : other.type.copy();
        this.screenId = other.screenId == null ? null : other.screenId.copy();
        this.showId = other.showId == null ? null : other.showId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public SeatCriteria copy() {
        return new SeatCriteria(this);
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

    public IntegerFilter getSeatNumber() {
        return seatNumber;
    }

    public IntegerFilter seatNumber() {
        if (seatNumber == null) {
            seatNumber = new IntegerFilter();
        }
        return seatNumber;
    }

    public void setSeatNumber(IntegerFilter seatNumber) {
        this.seatNumber = seatNumber;
    }

    public StringFilter getSeatDescr() {
        return seatDescr;
    }

    public StringFilter seatDescr() {
        if (seatDescr == null) {
            seatDescr = new StringFilter();
        }
        return seatDescr;
    }

    public void setSeatDescr(StringFilter seatDescr) {
        this.seatDescr = seatDescr;
    }

    public StringFilter getType() {
        return type;
    }

    public StringFilter type() {
        if (type == null) {
            type = new StringFilter();
        }
        return type;
    }

    public void setType(StringFilter type) {
        this.type = type;
    }

    public LongFilter getScreenId() {
        return screenId;
    }

    public LongFilter screenId() {
        if (screenId == null) {
            screenId = new LongFilter();
        }
        return screenId;
    }

    public void setScreenId(LongFilter screenId) {
        this.screenId = screenId;
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
        final SeatCriteria that = (SeatCriteria) o;
        return (
            Objects.equals(seatId, that.seatId) &&
            Objects.equals(seatNumber, that.seatNumber) &&
            Objects.equals(seatDescr, that.seatDescr) &&
            Objects.equals(type, that.type) &&
            Objects.equals(screenId, that.screenId) &&
            Objects.equals(showId, that.showId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(seatId, seatNumber, seatDescr, type, screenId, showId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SeatCriteria{" +
            (seatId != null ? "seatId=" + seatId + ", " : "") +
            (seatNumber != null ? "seatNumber=" + seatNumber + ", " : "") +
            (seatDescr != null ? "seatDescr=" + seatDescr + ", " : "") +
            (type != null ? "type=" + type + ", " : "") +
            (screenId != null ? "screenId=" + screenId + ", " : "") +
            (showId != null ? "showId=" + showId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
