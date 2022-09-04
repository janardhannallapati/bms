package com.jana.bms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jana.bms.domain.enumeration.SeatStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ShowSeat.
 */
@Entity
@Table(name = "show_seat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ShowSeat implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "show_seat_id", nullable = false)
    private Long showSeatId;

    @NotNull
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SeatStatus status;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "screen", "shows" }, allowSetters = true)
    private Seat seat;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "movie", "screen", "seats" }, allowSetters = true)
    private Show show;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "customer", "show" }, allowSetters = true)
    private Booking booking;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getShowSeatId() {
        return this.showSeatId;
    }

    public ShowSeat showSeatId(Long showSeatId) {
        this.setShowSeatId(showSeatId);
        return this;
    }

    public void setShowSeatId(Long showSeatId) {
        this.showSeatId = showSeatId;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public ShowSeat price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public SeatStatus getStatus() {
        return this.status;
    }

    public ShowSeat status(SeatStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public Seat getSeat() {
        return this.seat;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }

    public ShowSeat seat(Seat seat) {
        this.setSeat(seat);
        return this;
    }

    public Show getShow() {
        return this.show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public ShowSeat show(Show show) {
        this.setShow(show);
        return this;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public ShowSeat booking(Booking booking) {
        this.setBooking(booking);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShowSeat)) {
            return false;
        }
        return showSeatId != null && showSeatId.equals(((ShowSeat) o).showSeatId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShowSeat{" +
            "showSeatId=" + getShowSeatId() +
            ", price=" + getPrice() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
