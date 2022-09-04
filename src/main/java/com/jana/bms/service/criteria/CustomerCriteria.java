package com.jana.bms.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.jana.bms.domain.Customer} entity. This class is used
 * in {@link com.jana.bms.web.rest.CustomerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /customers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class CustomerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter customerId;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter email;

    private LongFilter addressId;

    private Boolean distinct;

    public CustomerCriteria() {}

    public CustomerCriteria(CustomerCriteria other) {
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.email = other.email == null ? null : other.email.copy();
        this.addressId = other.addressId == null ? null : other.addressId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public CustomerCriteria copy() {
        return new CustomerCriteria(this);
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public LongFilter customerId() {
        if (customerId == null) {
            customerId = new LongFilter();
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public StringFilter firstName() {
        if (firstName == null) {
            firstName = new StringFilter();
        }
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public StringFilter lastName() {
        if (lastName == null) {
            lastName = new StringFilter();
        }
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getEmail() {
        return email;
    }

    public StringFilter email() {
        if (email == null) {
            email = new StringFilter();
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public LongFilter getAddressId() {
        return addressId;
    }

    public LongFilter addressId() {
        if (addressId == null) {
            addressId = new LongFilter();
        }
        return addressId;
    }

    public void setAddressId(LongFilter addressId) {
        this.addressId = addressId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CustomerCriteria that = (CustomerCriteria) o;
        return (
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(addressId, that.addressId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, firstName, lastName, email, addressId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CustomerCriteria{" +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (firstName != null ? "firstName=" + firstName + ", " : "") +
            (lastName != null ? "lastName=" + lastName + ", " : "") +
            (email != null ? "email=" + email + ", " : "") +
            (addressId != null ? "addressId=" + addressId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
