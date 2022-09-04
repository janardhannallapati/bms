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
 * Criteria class for the {@link com.jana.bms.domain.Screen} entity. This class is used
 * in {@link com.jana.bms.web.rest.ScreenResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /screens?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class ScreenCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter screenId;

    private StringFilter name;

    private IntegerFilter totalSeats;

    private LongFilter theatreId;

    private Boolean distinct;

    public ScreenCriteria() {}

    public ScreenCriteria(ScreenCriteria other) {
        this.screenId = other.screenId == null ? null : other.screenId.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.totalSeats = other.totalSeats == null ? null : other.totalSeats.copy();
        this.theatreId = other.theatreId == null ? null : other.theatreId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ScreenCriteria copy() {
        return new ScreenCriteria(this);
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

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public IntegerFilter getTotalSeats() {
        return totalSeats;
    }

    public IntegerFilter totalSeats() {
        if (totalSeats == null) {
            totalSeats = new IntegerFilter();
        }
        return totalSeats;
    }

    public void setTotalSeats(IntegerFilter totalSeats) {
        this.totalSeats = totalSeats;
    }

    public LongFilter getTheatreId() {
        return theatreId;
    }

    public LongFilter theatreId() {
        if (theatreId == null) {
            theatreId = new LongFilter();
        }
        return theatreId;
    }

    public void setTheatreId(LongFilter theatreId) {
        this.theatreId = theatreId;
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
        final ScreenCriteria that = (ScreenCriteria) o;
        return (
            Objects.equals(screenId, that.screenId) &&
            Objects.equals(name, that.name) &&
            Objects.equals(totalSeats, that.totalSeats) &&
            Objects.equals(theatreId, that.theatreId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(screenId, name, totalSeats, theatreId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScreenCriteria{" +
            (screenId != null ? "screenId=" + screenId + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (totalSeats != null ? "totalSeats=" + totalSeats + ", " : "") +
            (theatreId != null ? "theatreId=" + theatreId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
