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
 * Criteria class for the {@link com.jana.bms.domain.Theatre} entity. This class is used
 * in {@link com.jana.bms.web.rest.TheatreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /theatres?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class TheatreCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter theatreId;

    private StringFilter theatreName;

    private IntegerFilter noOfScreens;

    private Boolean distinct;

    public TheatreCriteria() {}

    public TheatreCriteria(TheatreCriteria other) {
        this.theatreId = other.theatreId == null ? null : other.theatreId.copy();
        this.theatreName = other.theatreName == null ? null : other.theatreName.copy();
        this.noOfScreens = other.noOfScreens == null ? null : other.noOfScreens.copy();
        this.distinct = other.distinct;
    }

    @Override
    public TheatreCriteria copy() {
        return new TheatreCriteria(this);
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

    public StringFilter getTheatreName() {
        return theatreName;
    }

    public StringFilter theatreName() {
        if (theatreName == null) {
            theatreName = new StringFilter();
        }
        return theatreName;
    }

    public void setTheatreName(StringFilter theatreName) {
        this.theatreName = theatreName;
    }

    public IntegerFilter getNoOfScreens() {
        return noOfScreens;
    }

    public IntegerFilter noOfScreens() {
        if (noOfScreens == null) {
            noOfScreens = new IntegerFilter();
        }
        return noOfScreens;
    }

    public void setNoOfScreens(IntegerFilter noOfScreens) {
        this.noOfScreens = noOfScreens;
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
        final TheatreCriteria that = (TheatreCriteria) o;
        return (
            Objects.equals(theatreId, that.theatreId) &&
            Objects.equals(theatreName, that.theatreName) &&
            Objects.equals(noOfScreens, that.noOfScreens) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(theatreId, theatreName, noOfScreens, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TheatreCriteria{" +
            (theatreId != null ? "theatreId=" + theatreId + ", " : "") +
            (theatreName != null ? "theatreName=" + theatreName + ", " : "") +
            (noOfScreens != null ? "noOfScreens=" + noOfScreens + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
