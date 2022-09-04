package com.jana.bms.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.jana.bms.domain.City} entity.
 */
public class CityDTO implements Serializable {

    @NotNull
    private Long cityId;

    @NotNull
    @Size(max = 50)
    private String cityName;

    private CountryDTO country;

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public CountryDTO getCountry() {
        return country;
    }

    public void setCountry(CountryDTO country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CityDTO)) {
            return false;
        }

        CityDTO cityDTO = (CityDTO) o;
        if (this.cityId == null) {
            return false;
        }
        return Objects.equals(this.cityId, cityDTO.cityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.cityId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CityDTO{" +
            "cityId=" + getCityId() +
            ", cityName='" + getCityName() + "'" +
            ", country=" + getCountry() +
            "}";
    }
}
