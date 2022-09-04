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
 * Criteria class for the {@link com.jana.bms.domain.Genre} entity. This class is used
 * in {@link com.jana.bms.web.rest.GenreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /genres?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class GenreCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter genreId;

    private StringFilter name;

    private LongFilter movieId;

    private Boolean distinct;

    public GenreCriteria() {}

    public GenreCriteria(GenreCriteria other) {
        this.genreId = other.genreId == null ? null : other.genreId.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.movieId = other.movieId == null ? null : other.movieId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public GenreCriteria copy() {
        return new GenreCriteria(this);
    }

    public LongFilter getGenreId() {
        return genreId;
    }

    public LongFilter genreId() {
        if (genreId == null) {
            genreId = new LongFilter();
        }
        return genreId;
    }

    public void setGenreId(LongFilter genreId) {
        this.genreId = genreId;
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
        final GenreCriteria that = (GenreCriteria) o;
        return (
            Objects.equals(genreId, that.genreId) &&
            Objects.equals(name, that.name) &&
            Objects.equals(movieId, that.movieId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(genreId, name, movieId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GenreCriteria{" +
            (genreId != null ? "genreId=" + genreId + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (movieId != null ? "movieId=" + movieId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
