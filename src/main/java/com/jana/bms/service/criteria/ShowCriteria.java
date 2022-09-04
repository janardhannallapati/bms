package com.jana.bms.service.criteria;

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
import tech.jhipster.service.filter.LocalDateFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.jana.bms.domain.Show} entity. This class is used
 * in {@link com.jana.bms.web.rest.ShowResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /shows?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class ShowCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter showId;

    private LocalDateFilter date;

    private InstantFilter startTime;

    private InstantFilter endTime;

    private LongFilter movieId;

    private LongFilter screenId;

    private LongFilter seatId;

    private Boolean distinct;

    public ShowCriteria() {}

    public ShowCriteria(ShowCriteria other) {
        this.showId = other.showId == null ? null : other.showId.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.startTime = other.startTime == null ? null : other.startTime.copy();
        this.endTime = other.endTime == null ? null : other.endTime.copy();
        this.movieId = other.movieId == null ? null : other.movieId.copy();
        this.screenId = other.screenId == null ? null : other.screenId.copy();
        this.seatId = other.seatId == null ? null : other.seatId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ShowCriteria copy() {
        return new ShowCriteria(this);
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

    public LocalDateFilter getDate() {
        return date;
    }

    public LocalDateFilter date() {
        if (date == null) {
            date = new LocalDateFilter();
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public InstantFilter getStartTime() {
        return startTime;
    }

    public InstantFilter startTime() {
        if (startTime == null) {
            startTime = new InstantFilter();
        }
        return startTime;
    }

    public void setStartTime(InstantFilter startTime) {
        this.startTime = startTime;
    }

    public InstantFilter getEndTime() {
        return endTime;
    }

    public InstantFilter endTime() {
        if (endTime == null) {
            endTime = new InstantFilter();
        }
        return endTime;
    }

    public void setEndTime(InstantFilter endTime) {
        this.endTime = endTime;
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
        final ShowCriteria that = (ShowCriteria) o;
        return (
            Objects.equals(showId, that.showId) &&
            Objects.equals(date, that.date) &&
            Objects.equals(startTime, that.startTime) &&
            Objects.equals(endTime, that.endTime) &&
            Objects.equals(movieId, that.movieId) &&
            Objects.equals(screenId, that.screenId) &&
            Objects.equals(seatId, that.seatId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(showId, date, startTime, endTime, movieId, screenId, seatId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShowCriteria{" +
            (showId != null ? "showId=" + showId + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (startTime != null ? "startTime=" + startTime + ", " : "") +
            (endTime != null ? "endTime=" + endTime + ", " : "") +
            (movieId != null ? "movieId=" + movieId + ", " : "") +
            (screenId != null ? "screenId=" + screenId + ", " : "") +
            (seatId != null ? "seatId=" + seatId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
