package com.jana.bms.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.jana.bms.domain.Show} entity.
 */
public class ShowDTO implements Serializable {

    @NotNull
    private Long showId;

    @NotNull
    private LocalDate date;

    @NotNull
    private Instant startTime;

    @NotNull
    private Instant endTime;

    private MovieDTO movie;

    private ScreenDTO screen;

    private Set<SeatDTO> seats = new HashSet<>();

    public Long getShowId() {
        return showId;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public MovieDTO getMovie() {
        return movie;
    }

    public void setMovie(MovieDTO movie) {
        this.movie = movie;
    }

    public ScreenDTO getScreen() {
        return screen;
    }

    public void setScreen(ScreenDTO screen) {
        this.screen = screen;
    }

    public Set<SeatDTO> getSeats() {
        return seats;
    }

    public void setSeats(Set<SeatDTO> seats) {
        this.seats = seats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShowDTO)) {
            return false;
        }

        ShowDTO showDTO = (ShowDTO) o;
        if (this.showId == null) {
            return false;
        }
        return Objects.equals(this.showId, showDTO.showId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.showId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShowDTO{" +
            "showId=" + getShowId() +
            ", date='" + getDate() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", movie=" + getMovie() +
            ", screen=" + getScreen() +
            ", seats=" + getSeats() +
            "}";
    }
}
