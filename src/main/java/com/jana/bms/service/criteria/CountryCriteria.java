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
 * Criteria class for the {@link com.jana.bms.domain.Country} entity. This class is used
 * in {@link com.jana.bms.web.rest.CountryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /countries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class CountryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter countryId;

    private StringFilter countryCode;

    private StringFilter countryName;

    private Boolean distinct;

    public CountryCriteria() {}

    public CountryCriteria(CountryCriteria other) {
        this.countryId = other.countryId == null ? null : other.countryId.copy();
        this.countryCode = other.countryCode == null ? null : other.countryCode.copy();
        this.countryName = other.countryName == null ? null : other.countryName.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CountryCriteria copy() {
        return new CountryCriteria(this);
    }

    public LongFilter getCountryId() {
        return countryId;
    }

    public LongFilter countryId() {
        if (countryId == null) {
            countryId = new LongFilter();
        }
        return countryId;
    }

    public void setCountryId(LongFilter countryId) {
        this.countryId = countryId;
    }

    public StringFilter getCountryCode() {
        return countryCode;
    }

    public StringFilter countryCode() {
        if (countryCode == null) {
            countryCode = new StringFilter();
        }
        return countryCode;
    }

    public void setCountryCode(StringFilter countryCode) {
        this.countryCode = countryCode;
    }

    public StringFilter getCountryName() {
        return countryName;
    }

    public StringFilter countryName() {
        if (countryName == null) {
            countryName = new StringFilter();
        }
        return countryName;
    }

    public void setCountryName(StringFilter countryName) {
        this.countryName = countryName;
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
        final CountryCriteria that = (CountryCriteria) o;
        return (
            Objects.equals(countryId, that.countryId) &&
            Objects.equals(countryCode, that.countryCode) &&
            Objects.equals(countryName, that.countryName) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(countryId, countryCode, countryName, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountryCriteria{" +
            (countryId != null ? "countryId=" + countryId + ", " : "") +
            (countryCode != null ? "countryCode=" + countryCode + ", " : "") +
            (countryName != null ? "countryName=" + countryName + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
