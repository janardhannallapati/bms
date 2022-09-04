package com.jana.bms.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.jana.bms.domain.Seat} entity.
 */
public class SeatDTO implements Serializable {

    @NotNull
    private Long seatId;

    @NotNull
    private Integer seatNumber;

    @Size(max = 10)
    private String seatDescr;

    @NotNull
    @Size(max = 50)
    private String type;

    private ScreenDTO screen;

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public String getSeatDescr() {
        return seatDescr;
    }

    public void setSeatDescr(String seatDescr) {
        this.seatDescr = seatDescr;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ScreenDTO getScreen() {
        return screen;
    }

    public void setScreen(ScreenDTO screen) {
        this.screen = screen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SeatDTO)) {
            return false;
        }

        SeatDTO seatDTO = (SeatDTO) o;
        if (this.seatId == null) {
            return false;
        }
        return Objects.equals(this.seatId, seatDTO.seatId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.seatId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SeatDTO{" +
            "seatId=" + getSeatId() +
            ", seatNumber=" + getSeatNumber() +
            ", seatDescr='" + getSeatDescr() + "'" +
            ", type='" + getType() + "'" +
            ", screen=" + getScreen() +
            "}";
    }
}
