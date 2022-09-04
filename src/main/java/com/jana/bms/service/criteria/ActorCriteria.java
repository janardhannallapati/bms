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
 * Criteria class for the {@link com.jana.bms.domain.Actor} entity. This class is used
 * in {@link com.jana.bms.web.rest.ActorResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /actors?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class ActorCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter actorId;

    private StringFilter firstName;

    private StringFilter lastName;

    private LongFilter movieId;

    private Boolean distinct;

    public ActorCriteria() {}

    public ActorCriteria(ActorCriteria other) {
        this.actorId = other.actorId == null ? null : other.actorId.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.movieId = other.movieId == null ? null : other.movieId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ActorCriteria copy() {
        return new ActorCriteria(this);
    }

    public LongFilter getActorId() {
        return actorId;
    }

    public LongFilter actorId() {
        if (actorId == null) {
            actorId = new LongFilter();
        }
        return actorId;
    }

    public void setActorId(LongFilter actorId) {
        this.actorId = actorId;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public LongFilter getMovieId() {
        return movieId;
    }

    public LongFilter movieId() {
        if (movieId == null) {
            movieId = new LongFilter();
        }
        return movieId;
    }

    public void setMovieId(LongFilter movieId) {
        this.movieId = movieId;
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
        final ActorCriteria that = (ActorCriteria) o;
        return (
            Objects.equals(actorId, that.actorId) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(movieId, that.movieId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(actorId, firstName, lastName, movieId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActorCriteria{" +
            (actorId != null ? "actorId=" + actorId + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (movieId != null ? "movieId=" + movieId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
