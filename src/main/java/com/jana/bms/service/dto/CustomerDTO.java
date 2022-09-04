package com.jana.bms.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.jana.bms.domain.Customer} entity.
 */
public class CustomerDTO implements Serializable {

    @NotNull
    private Long customerId;

    @NotNull
    @Size(max = 45)
    private String firstName;

    @NotNull
    @Size(max = 45)
    private String lastName;

    @Size(max = 50)
    private String email;

    private AddressDTO address;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomerDTO)) {
            return false;
        }

        CustomerDTO customerDTO = (CustomerDTO) o;
        if (this.customerId == null) {
            return false;
        }
        return Objects.equals(this.customerId, customerDTO.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.customerId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerDTO{" +
            "customerId=" + getCustomerId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            ", email='" + getEmail() + "'" +
            ", address=" + getAddress() +
            "}";
    }
}
