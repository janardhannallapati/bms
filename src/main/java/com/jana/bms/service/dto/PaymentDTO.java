package com.jana.bms.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.jana.bms.domain.Payment} entity.
 */
public class PaymentDTO implements Serializable {

    @NotNull
    private Long paymentId;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private Instant timestamp;

    @Size(max = 45)
    private String couponId;

    @Size(max = 45)
    private String paymentMethod;

    private BookingDTO booking;

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BookingDTO getBooking() {
        return booking;
    }

    public void setBooking(BookingDTO booking) {
        this.booking = booking;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PaymentDTO)) {
            return false;
        }

        PaymentDTO paymentDTO = (PaymentDTO) o;
        if (this.paymentId == null) {
            return false;
        }
        return Objects.equals(this.paymentId, paymentDTO.paymentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.paymentId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentDTO{" +
            "paymentId=" + getPaymentId() +
            ", amount=" + getAmount() +
            ", timestamp='" + getTimestamp() + "'" +
            ", couponId='" + getCouponId() + "'" +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", booking=" + getBooking() +
            "}";
    }
}
