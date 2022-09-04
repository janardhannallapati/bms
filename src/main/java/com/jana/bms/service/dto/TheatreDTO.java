package com.jana.bms.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.jana.bms.domain.Theatre} entity.
 */
public class TheatreDTO implements Serializable {

    @NotNull
    private Long theatreId;

    @Size(max = 45)
    private String theatreName;

    @NotNull
    private Integer noOfScreens;

    public Long getTheatreId() {
        return theatreId;
    }

    public void setTheatreId(Long theatreId) {
        this.theatreId = theatreId;
    }

    public String getTheatreName() {
        return theatreName;
    }

    public void setTheatreName(String theatreName) {
        this.theatreName = theatreName;
    }

    public Integer getNoOfScreens() {
        return noOfScreens;
    }

    public void setNoOfScreens(Integer noOfScreens) {
        this.noOfScreens = noOfScreens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TheatreDTO)) {
            return false;
        }

        TheatreDTO theatreDTO = (TheatreDTO) o;
        if (this.theatreId == null) {
            return false;
        }
        return Objects.equals(this.theatreId, theatreDTO.theatreId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.theatreId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TheatreDTO{" +
            "theatreId=" + getTheatreId() +
            ", theatreName='" + getTheatreName() + "'" +
            ", noOfScreens=" + getNoOfScreens() +
            "}";
    }
}
