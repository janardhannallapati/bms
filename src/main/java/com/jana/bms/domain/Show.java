package com.jana.bms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Show.
 */
@Entity
@Table(name = "jhi_show")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Show implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "show_id", nullable = false)
    private Long showId;

    @NotNull
    @Column(name = "date", nullable = false)
    private LocalDate date;

    @NotNull
    @Column(name = "start_time", nullable = false)
    private Instant startTime;

    @NotNull
    @Column(name = "end_time", nullable = false)
    private Instant endTime;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "language", "actors", "genres" }, allowSetters = true)
    private Movie movie;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "theatre" }, allowSetters = true)
    private Screen screen;

    @ManyToMany
    @NotNull
    @JoinTable(
        name = "rel_jhi_show__seat",
        joinColumns = @JoinColumn(name = "jhi_show_show_id"),
        inverseJoinColumns = @JoinColumn(name = "seat_seat_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "screen", "shows" }, allowSetters = true)
    private Set<Seat> seats = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getShowId() {
        return this.showId;
    }

    public Show showId(Long showId) {
        this.setShowId(showId);
        return this;
    }

    public void setShowId(Long showId) {
        this.showId = showId;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Show date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Instant getStartTime() {
        return this.startTime;
    }

    public Show startTime(Instant startTime) {
        this.setStartTime(startTime);
        return this;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return this.endTime;
    }

    public Show endTime(Instant endTime) {
        this.setEndTime(endTime);
        return this;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public Movie getMovie() {
        return this.movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Show movie(Movie movie) {
        this.setMovie(movie);
        return this;
    }

    public Screen getScreen() {
        return this.screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public Show screen(Screen screen) {
        this.setScreen(screen);
        return this;
    }

    public Set<Seat> getSeats() {
        return this.seats;
    }

    public void setSeats(Set<Seat> seats) {
        this.seats = seats;
    }

    public Show seats(Set<Seat> seats) {
        this.setSeats(seats);
        return this;
    }

    public Show addSeat(Seat seat) {
        this.seats.add(seat);
        seat.getShows().add(this);
        return this;
    }

    public Show removeSeat(Seat seat) {
        this.seats.remove(seat);
        seat.getShows().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Show)) {
            return false;
        }
        return showId != null && showId.equals(((Show) o).showId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Show{" +
            "showId=" + getShowId() +
            ", date='" + getDate() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            "}";
    }
}
