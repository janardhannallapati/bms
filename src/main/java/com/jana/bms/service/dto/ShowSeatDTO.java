package com.jana.bms.service.dto;

import com.jana.bms.domain.enumeration.SeatStatus;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.jana.bms.domain.ShowSeat} entity.
 */
public class ShowSeatDTO implements Serializable {

    @NotNull
    private Long showSeatId;

    @NotNull
    private BigDecimal price;

    @NotNull
    private SeatStatus status;

    private SeatDTO seat;

    private ShowDTO show;

    private BookingDTO booking;

    public Long getShowSeatId() {
        return showSeatId;
    }

    public void setShowSeatId(Long showSeatId) {
        this.showSeatId = showSeatId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public SeatStatus getStatus() {
        return status;
    }

    public void setStatus(SeatStatus status) {
        this.status = status;
    }

    public SeatDTO getSeat() {
        return seat;
    }

    public void setSeat(SeatDTO seat) {
        this.seat = seat;
    }

    public ShowDTO getShow() {
        return show;
    }

    public void setShow(ShowDTO show) {
        this.show = show;
    }

    public BookingDTO getBooking() {
        return booking;
    }

    public void setBooking(BookingDTO booking) {
        this.booking = booking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShowSeatDTO)) {
            return false;
        }

        ShowSeatDTO showSeatDTO = (ShowSeatDTO) o;
        if (this.showSeatId == null) {
            return false;
        }
        return Objects.equals(this.showSeatId, showSeatDTO.showSeatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.showSeatId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShowSeatDTO{" +
            "showSeatId=" + getShowSeatId() +
            ", price=" + getPrice() +
            ", status='" + getStatus() + "'" +
            ", seat=" + getSeat() +
            ", show=" + getShow() +
            ", booking=" + getBooking() +
            "}";
    }
}
