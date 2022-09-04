package com.jana.bms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id", nullable = false)
    private Long paymentId;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @Size(max = 45)
    @Column(name = "coupon_id", length = 45)
    private String couponId;

    @Size(max = 45)
    @Column(name = "payment_method", length = 45)
    private String paymentMethod;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "customer", "show" }, allowSetters = true)
    private Booking booking;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getPaymentId() {
        return this.paymentId;
    }

    public Payment paymentId(Long paymentId) {
        this.setPaymentId(paymentId);
        return this;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public Payment amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getTimestamp() {
        return this.timestamp;
    }

    public Payment timestamp(Instant timestamp) {
        this.setTimestamp(timestamp);
        return this;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getCouponId() {
        return this.couponId;
    }

    public Payment couponId(String couponId) {
        this.setCouponId(couponId);
        return this;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    public Payment paymentMethod(String paymentMethod) {
        this.setPaymentMethod(paymentMethod);
        return this;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Booking getBooking() {
        return this.booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Payment booking(Booking booking) {
        this.setBooking(booking);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Payment)) {
            return false;
        }
        return paymentId != null && paymentId.equals(((Payment) o).paymentId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Payment{" +
            "paymentId=" + getPaymentId() +
            ", amount=" + getAmount() +
            ", timestamp='" + getTimestamp() + "'" +
            ", couponId='" + getCouponId() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            "}";
    }
}
