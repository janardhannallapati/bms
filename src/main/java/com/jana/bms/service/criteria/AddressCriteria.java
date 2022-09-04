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
 * Criteria class for the {@link com.jana.bms.domain.Address} entity. This class is used
 * in {@link com.jana.bms.web.rest.AddressResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /addresses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class AddressCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter addressId;

    private StringFilter address;

    private StringFilter address2;

    private StringFilter district;

    private StringFilter postalCode;

    private StringFilter phone;

    private StringFilter location;

    private LongFilter cityId;

    private Boolean distinct;

    public AddressCriteria() {}

    public AddressCriteria(AddressCriteria other) {
        this.addressId = other.addressId == null ? null : other.addressId.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.address2 = other.address2 == null ? null : other.address2.copy();
        this.district = other.district == null ? null : other.district.copy();
        this.postalCode = other.postalCode == null ? null : other.postalCode.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.cityId = other.cityId == null ? null : other.cityId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AddressCriteria copy() {
        return new AddressCriteria(this);
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

    public StringFilter getAddress() {
        return address;
    }

    public StringFilter address() {
        if (address == null) {
            address = new StringFilter();
        }
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getAddress2() {
        return address2;
    }

    public StringFilter address2() {
        if (address2 == null) {
            address2 = new StringFilter();
        }
        return address2;
    }

    public void setAddress2(StringFilter address2) {
        this.address2 = address2;
    }

    public StringFilter getDistrict() {
        return district;
    }

    public StringFilter district() {
        if (district == null) {
            district = new StringFilter();
        }
        return district;
    }

    public void setDistrict(StringFilter district) {
        this.district = district;
    }

    public StringFilter getPostalCode() {
        return postalCode;
    }

    public StringFilter postalCode() {
        if (postalCode == null) {
            postalCode = new StringFilter();
        }
        return postalCode;
    }

    public void setPostalCode(StringFilter postalCode) {
        this.postalCode = postalCode;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getLocation() {
        return location;
    }

    public StringFilter location() {
        if (location == null) {
            location = new StringFilter();
        }
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public LongFilter getCityId() {
        return cityId;
    }

    public LongFilter cityId() {
        if (cityId == null) {
            cityId = new LongFilter();
        }
        return cityId;
    }

    public void setCityId(LongFilter cityId) {
        this.cityId = cityId;
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
        final AddressCriteria that = (AddressCriteria) o;
        return (
            Objects.equals(addressId, that.addressId) &&
            Objects.equals(address, that.address) &&
            Objects.equals(address2, that.address2) &&
            Objects.equals(district, that.district) &&
            Objects.equals(postalCode, that.postalCode) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(location, that.location) &&
            Objects.equals(cityId, that.cityId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressId, address, address2, district, postalCode, phone, location, cityId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AddressCriteria{" +
            (addressId != null ? "addressId=" + addressId + ", " : "") +
            (address != null ? "address=" + address + ", " : "") +
            (address2 != null ? "address2=" + address2 + ", " : "") +
            (district != null ? "district=" + district + ", " : "") +
            (postalCode != null ? "postalCode=" + postalCode + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (location != null ? "location=" + location + ", " : "") +
            (cityId != null ? "cityId=" + cityId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
