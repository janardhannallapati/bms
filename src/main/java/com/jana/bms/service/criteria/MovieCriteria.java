package com.jana.bms.service.criteria;

import com.jana.bms.domain.enumeration.Rating;
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
 * Criteria class for the {@link com.jana.bms.domain.Movie} entity. This class is used
 * in {@link com.jana.bms.web.rest.MovieResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /movies?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class MovieCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Rating
     */
    public static class RatingFilter extends Filter<Rating> {

        public RatingFilter() {}

        public RatingFilter(RatingFilter filter) {
            super(filter);
        }

        @Override
        public RatingFilter copy() {
            return new RatingFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter movieId;

    private StringFilter title;

    private StringFilter description;

    private IntegerFilter releaseYear;

    private IntegerFilter length;

    private RatingFilter rating;

    private LongFilter languageId;

    private LongFilter actorId;

    private LongFilter genreId;

    private Boolean distinct;

    public MovieCriteria() {}

    public MovieCriteria(MovieCriteria other) {
        this.movieId = other.movieId == null ? null : other.movieId.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.releaseYear = other.releaseYear == null ? null : other.releaseYear.copy();
        this.length = other.length == null ? null : other.length.copy();
        this.rating = other.rating == null ? null : other.rating.copy();
        this.languageId = other.languageId == null ? null : other.languageId.copy();
        this.actorId = other.actorId == null ? null : other.actorId.copy();
        this.genreId = other.genreId == null ? null : other.genreId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MovieCriteria copy() {
        return new MovieCriteria(this);
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

    public StringFilter getTitle() {
        return title;
    }

    public StringFilter title() {
        if (title == null) {
            title = new StringFilter();
        }
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getDescription() {
        return description;
    }

    public StringFilter description() {
        if (description == null) {
            description = new StringFilter();
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getReleaseYear() {
        return releaseYear;
    }

    public IntegerFilter releaseYear() {
        if (releaseYear == null) {
            releaseYear = new IntegerFilter();
        }
        return releaseYear;
    }

    public void setReleaseYear(IntegerFilter releaseYear) {
        this.releaseYear = releaseYear;
    }

    public IntegerFilter getLength() {
        return length;
    }

    public IntegerFilter length() {
        if (length == null) {
            length = new IntegerFilter();
        }
        return length;
    }

    public void setLength(IntegerFilter length) {
        this.length = length;
    }

    public RatingFilter getRating() {
        return rating;
    }

    public RatingFilter rating() {
        if (rating == null) {
            rating = new RatingFilter();
        }
        return rating;
    }

    public void setRating(RatingFilter rating) {
        this.rating = rating;
    }

    public LongFilter getLanguageId() {
        return languageId;
    }

    public LongFilter languageId() {
        if (languageId == null) {
            languageId = new LongFilter();
        }
        return languageId;
    }

    public void setLanguageId(LongFilter languageId) {
        this.languageId = languageId;
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
        final MovieCriteria that = (MovieCriteria) o;
        return (
            Objects.equals(movieId, that.movieId) &&
            Objects.equals(title, that.title) &&
            Objects.equals(description, that.description) &&
            Objects.equals(releaseYear, that.releaseYear) &&
            Objects.equals(length, that.length) &&
            Objects.equals(rating, that.rating) &&
            Objects.equals(languageId, that.languageId) &&
            Objects.equals(actorId, that.actorId) &&
            Objects.equals(genreId, that.genreId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(movieId, title, description, releaseYear, length, rating, languageId, actorId, genreId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MovieCriteria{" +
            (movieId != null ? "movieId=" + movieId + ", " : "") +
            (title != null ? "title=" + title + ", " : "") +
            (description != null ? "description=" + description + ", " : "") +
            (releaseYear != null ? "releaseYear=" + releaseYear + ", " : "") +
            (length != null ? "length=" + length + ", " : "") +
            (rating != null ? "rating=" + rating + ", " : "") +
            (languageId != null ? "languageId=" + languageId + ", " : "") +
            (actorId != null ? "actorId=" + actorId + ", " : "") +
            (genreId != null ? "genreId=" + genreId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
