package com.jana.bms.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.jana.bms.domain.Country} entity.
 */
public class CountryDTO implements Serializable {

    @NotNull
    private Long countryId;

    @NotNull
    @Size(max = 3)
    private String countryCode;

    @NotNull
    @Size(max = 50)
    private String countryName;

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CountryDTO)) {
            return false;
        }

        CountryDTO countryDTO = (CountryDTO) o;
        if (this.countryId == null) {
            return false;
        }
        return Objects.equals(this.countryId, countryDTO.countryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.countryId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountryDTO{" +
            "countryId=" + getCountryId() +
            ", countryCode='" + getCountryCode() + "'" +
            ", countryName='" + getCountryName() + "'" +
            "}";
    }
}
