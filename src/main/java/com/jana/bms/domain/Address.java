package com.jana.bms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Address.
 */
@Entity
@Table(name = "address")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", nullable = false)
    private Long addressId;

    @NotNull
    @Size(max = 50)
    @Column(name = "address", length = 50, nullable = false)
    private String address;

    @Size(max = 50)
    @Column(name = "address_2", length = 50)
    private String address2;

    @NotNull
    @Size(max = 20)
    @Column(name = "district", length = 20, nullable = false)
    private String district;

    @NotNull
    @Size(max = 10)
    @Column(name = "postal_code", length = 10, nullable = false)
    private String postalCode;

    @NotNull
    @Size(max = 20)
    @Column(name = "phone", length = 20, nullable = false)
    private String phone;

    @NotNull
    @Column(name = "location", nullable = false)
    private String location;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "country" }, allowSetters = true)
    private City city;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getAddressId() {
        return this.addressId;
    }

    public Address addressId(Long addressId) {
        this.setAddressId(addressId);
        return this;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getAddress() {
        return this.address;
    }

    public Address address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress2() {
        return this.address2;
    }

    public Address address2(String address2) {
        this.setAddress2(address2);
        return this;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getDistrict() {
        return this.district;
    }

    public Address district(String district) {
        this.setDistrict(district);
        return this;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public Address postalCode(String postalCode) {
        this.setPostalCode(postalCode);
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return this.phone;
    }

    public Address phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return this.location;
    }

    public Address location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public City getCity() {
        return this.city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Address city(City city) {
        this.setCity(city);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        return addressId != null && addressId.equals(((Address) o).addressId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Address{" +
            "addressId=" + getAddressId() +
            ", address='" + getAddress() + "'" +
            ", address2='" + getAddress2() + "'" +
            ", district='" + getDistrict() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", phone='" + getPhone() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
