package com.jana.bms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Seat.
 */
@Entity
@Table(name = "seat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Seat implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id", nullable = false)
    private Long seatId;

    @NotNull
    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @Size(max = 10)
    @Column(name = "seat_descr", length = 10)
    private String seatDescr;

    @NotNull
    @Size(max = 50)
    @Column(name = "type", length = 50, nullable = false)
    private String type;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "theatre" }, allowSetters = true)
    private Screen screen;

    @ManyToMany(mappedBy = "seats")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "movie", "screen", "seats" }, allowSetters = true)
    private Set<Show> shows = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getSeatId() {
        return this.seatId;
    }

    public Seat seatId(Long seatId) {
        this.setSeatId(seatId);
        return this;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public Integer getSeatNumber() {
        return this.seatNumber;
    }

    public Seat seatNumber(Integer seatNumber) {
        this.setSeatNumber(seatNumber);
        return this;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSeatDescr() {
        return this.seatDescr;
    }

    public Seat seatDescr(String seatDescr) {
        this.setSeatDescr(seatDescr);
        return this;
    }

    public void setSeatDescr(String seatDescr) {
        this.seatDescr = seatDescr;
    }

    public String getType() {
        return this.type;
    }

    public Seat type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Screen getScreen() {
        return this.screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public Seat screen(Screen screen) {
        this.setScreen(screen);
        return this;
    }

    public Set<Show> getShows() {
        return this.shows;
    }

    public void setShows(Set<Show> shows) {
        if (this.shows != null) {
            this.shows.forEach(i -> i.removeSeat(this));
        }
        if (shows != null) {
            shows.forEach(i -> i.addSeat(this));
        }
        this.shows = shows;
    }

    public Seat shows(Set<Show> shows) {
        this.setShows(shows);
        return this;
    }

    public Seat addShow(Show show) {
        this.shows.add(show);
        show.getSeats().add(this);
        return this;
    }

    public Seat removeShow(Show show) {
        this.shows.remove(show);
        show.getSeats().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Seat)) {
            return false;
        }
        return seatId != null && seatId.equals(((Seat) o).seatId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Seat{" +
            "seatId=" + getSeatId() +
            ", seatNumber=" + getSeatNumber() +
            ", seatDescr='" + getSeatDescr() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
