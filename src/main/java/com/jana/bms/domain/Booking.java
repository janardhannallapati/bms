package com.jana.bms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jana.bms.domain.enumeration.BookingStatus;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Booking.
 */
@Entity
@Table(name = "booking")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id", nullable = false)
    private Long bookingId;

    @NotNull
    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BookingStatus status;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "address" }, allowSetters = true)
    private Customer customer;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "movie", "screen", "seats" }, allowSetters = true)
    private Show show;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getBookingId() {
        return this.bookingId;
    }

    public Booking bookingId(Long bookingId) {
        this.setBookingId(bookingId);
        return this;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public Booking timestamp(Instant timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public BookingStatus getStatus() {
        return this.status;
    }

    public Booking status(BookingStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Booking customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public Show getShow() {
        return this.show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    public Booking show(Show show) {
        this.setShow(show);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Booking)) {
            return false;
        }
        return bookingId != null && bookingId.equals(((Booking) o).bookingId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Booking{" +
            "bookingId=" + getBookingId() +
            ", timestamp='" + getTimestamp() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
