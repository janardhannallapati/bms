package com.jana.bms.service.dto;

import com.jana.bms.domain.enumeration.BookingStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.jana.bms.domain.Booking} entity.
 */
public class BookingDTO implements Serializable {

    @NotNull
    private Long bookingId;

    @NotNull
    private Instant timestamp;

    @NotNull
    private BookingStatus status;

    private CustomerDTO customer;

    private ShowDTO show;

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public ShowDTO getShow() {
        return show;
    }

    public void setShow(ShowDTO show) {
        this.show = show;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookingDTO)) {
            return false;
        }

        BookingDTO bookingDTO = (BookingDTO) o;
        if (this.bookingId == null) {
            return false;
        }
        return Objects.equals(this.bookingId, bookingDTO.bookingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.bookingId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingDTO{" +
            "bookingId=" + getBookingId() +
            ", timestamp='" + getTimestamp() + "'" +
            ", status='" + getStatus() + "'" +
            ", customer=" + getCustomer() +
            ", show=" + getShow() +
            "}";
    }
}
