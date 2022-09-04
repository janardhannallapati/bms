package com.jana.bms.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.jana.bms.domain.Screen} entity.
 */
public class ScreenDTO implements Serializable {

    @NotNull
    private Long screenId;

    @NotNull
    @Size(max = 45)
    private String name;

    @NotNull
    private Integer totalSeats;

    private TheatreDTO theatre;

    public Long getScreenId() {
        return screenId;
    }

    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public TheatreDTO getTheatre() {
        return theatre;
    }

    public void setTheatre(TheatreDTO theatre) {
        this.theatre = theatre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ScreenDTO)) {
            return false;
        }

        ScreenDTO screenDTO = (ScreenDTO) o;
        if (this.screenId == null) {
            return false;
        }
        return Objects.equals(this.screenId, screenDTO.screenId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.screenId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ScreenDTO{" +
            "screenId=" + getScreenId() +
            ", name='" + getName() + "'" +
            ", totalSeats=" + getTotalSeats() +
            ", theatre=" + getTheatre() +
            "}";
    }
}
