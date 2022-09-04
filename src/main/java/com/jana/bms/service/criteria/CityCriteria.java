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
 * Criteria class for the {@link com.jana.bms.domain.City} entity. This class is used
 * in {@link com.jana.bms.web.rest.CityResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /cities?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class CityCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter cityId;

    private StringFilter cityName;

    private LongFilter countryId;

    private Boolean distinct;

    public CityCriteria() {}

    public CityCriteria(CityCriteria other) {
        this.cityId = other.cityId == null ? null : other.cityId.copy();
        this.cityName = other.cityName == null ? null : other.cityName.copy();
        this.countryId = other.countryId == null ? null : other.countryId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CityCriteria copy() {
        return new CityCriteria(this);
    }

    public LongFilter getCityId() {
        return cityId;
    }

    public LongFilter cityId() {
        if (cityId == null) {
            cityId = new LongFilter();
        }
        return cityId;
    }

    public void setCityId(LongFilter cityId) {
        this.cityId = cityId;
    }

    public StringFilter getCityName() {
        return cityName;
    }

    public StringFilter cityName() {
        if (cityName == null) {
            cityName = new StringFilter();
        }
        return cityName;
    }

    public void setCityName(StringFilter cityName) {
        this.cityName = cityName;
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
        final CityCriteria that = (CityCriteria) o;
        return (
            Objects.equals(cityId, that.cityId) &&
            Objects.equals(cityName, that.cityName) &&
            Objects.equals(countryId, that.countryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(cityId, cityName, countryId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CityCriteria{" +
            (cityId != null ? "cityId=" + cityId + ", " : "") +
            (cityName != null ? "cityName=" + cityName + ", " : "") +
            (countryId != null ? "countryId=" + countryId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
