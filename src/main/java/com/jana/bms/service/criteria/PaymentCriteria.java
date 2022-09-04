package com.jana.bms.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.jana.bms.domain.Payment} entity. This class is used
 * in {@link com.jana.bms.web.rest.PaymentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class PaymentCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter paymentId;

    private BigDecimalFilter amount;

    private InstantFilter timestamp;

    private StringFilter couponId;

    private StringFilter paymentMethod;

    private LongFilter bookingId;

    private Boolean distinct;

    public PaymentCriteria() {}

    public PaymentCriteria(PaymentCriteria other) {
        this.paymentId = other.paymentId == null ? null : other.paymentId.copy();
        this.amount = other.amount == null ? null : other.amount.copy();
        this.timestamp = other.timestamp == null ? null : other.timestamp.copy();
        this.couponId = other.couponId == null ? null : other.couponId.copy();
        this.paymentMethod = other.paymentMethod == null ? null : other.paymentMethod.copy();
        this.bookingId = other.bookingId == null ? null : other.bookingId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public PaymentCriteria copy() {
        return new PaymentCriteria(this);
    }

    public LongFilter getPaymentId() {
        return paymentId;
    }

    public LongFilter paymentId() {
        if (paymentId == null) {
            paymentId = new LongFilter();
        }
        return paymentId;
    }

    public void setPaymentId(LongFilter paymentId) {
        this.paymentId = paymentId;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public BigDecimalFilter amount() {
        if (amount == null) {
            amount = new BigDecimalFilter();
        }
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public InstantFilter getTimestamp() {
        return timestamp;
    }

    public InstantFilter timestamp() {
        if (timestamp == null) {
            timestamp = new InstantFilter();
        }
        return timestamp;
    }

    public void setTimestamp(InstantFilter timestamp) {
        this.timestamp = timestamp;
    }

    public StringFilter getCouponId() {
        return couponId;
    }

    public StringFilter couponId() {
        if (couponId == null) {
            couponId = new StringFilter();
        }
        return couponId;
    }

    public void setCouponId(StringFilter couponId) {
        this.couponId = couponId;
    }

    public StringFilter getPaymentMethod() {
        return paymentMethod;
    }

    public StringFilter paymentMethod() {
        if (paymentMethod == null) {
            paymentMethod = new StringFilter();
        }
        return paymentMethod;
    }

    public void setPaymentMethod(StringFilter paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LongFilter getBookingId() {
        return bookingId;
    }

    public LongFilter bookingId() {
        if (bookingId == null) {
            bookingId = new LongFilter();
        }
        return bookingId;
    }

    public void setBookingId(LongFilter bookingId) {
        this.bookingId = bookingId;
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
        final PaymentCriteria that = (PaymentCriteria) o;
        return (
            Objects.equals(paymentId, that.paymentId) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(timestamp, that.timestamp) &&
            Objects.equals(couponId, that.couponId) &&
            Objects.equals(paymentMethod, that.paymentMethod) &&
            Objects.equals(bookingId, that.bookingId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(paymentId, amount, timestamp, couponId, paymentMethod, bookingId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentCriteria{" +
            (paymentId != null ? "paymentId=" + paymentId + ", " : "") +
            (amount != null ? "amount=" + amount + ", " : "") +
            (timestamp != null ? "timestamp=" + timestamp + ", " : "") +
            (couponId != null ? "couponId=" + couponId + ", " : "") +
            (paymentMethod != null ? "paymentMethod=" + paymentMethod + ", " : "") +
            (bookingId != null ? "bookingId=" + bookingId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
