package com.jana.bms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Payment.class);
        Payment payment1 = new Payment();
        payment1.setPaymentId(1L);
        Payment payment2 = new Payment();
        payment2.setPaymentId(payment1.getPaymentId());
        assertThat(payment1).isEqualTo(payment2);
        payment2.setPaymentId(2L);
        assertThat(payment1).isNotEqualTo(payment2);
        payment1.setPaymentId(null);
        assertThat(payment1).isNotEqualTo(payment2);
    }
}
