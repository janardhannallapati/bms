package com.jana.bms.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.jana.bms.domain.Address} entity.
 */
public class AddressDTO implements Serializable {

    @NotNull
    private Long addressId;

    @NotNull
    @Size(max = 50)
    private String address;

    @Size(max = 50)
    private String address2;

    @NotNull
    @Size(max = 20)
    private String district;

    @NotNull
    @Size(max = 10)
    private String postalCode;

    @NotNull
    @Size(max = 20)
    private String phone;

    @NotNull
    private String location;

    private CityDTO city;

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public CityDTO getCity() {
        return city;
    }

    public void setCity(CityDTO city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AddressDTO)) {
            return false;
        }

        AddressDTO addressDTO = (AddressDTO) o;
        if (this.addressId == null) {
            return false;
        }
        return Objects.equals(this.addressId, addressDTO.addressId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.addressId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AddressDTO{" +
            "addressId=" + getAddressId() +
            ", address='" + getAddress() + "'" +
            ", address2='" + getAddress2() + "'" +
            ", district='" + getDistrict() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", phone='" + getPhone() + "'" +
            ", location='" + getLocation() + "'" +
            ", city=" + getCity() +
            "}";
    }
}
