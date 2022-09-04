package com.jana.bms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PaymentDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PaymentDTO.class);
        PaymentDTO paymentDTO1 = new PaymentDTO();
        paymentDTO1.setPaymentId(1L);
        PaymentDTO paymentDTO2 = new PaymentDTO();
        assertThat(paymentDTO1).isNotEqualTo(paymentDTO2);
        paymentDTO2.setPaymentId(paymentDTO1.getPaymentId());
        assertThat(paymentDTO1).isEqualTo(paymentDTO2);
        paymentDTO2.setPaymentId(2L);
        assertThat(paymentDTO1).isNotEqualTo(paymentDTO2);
        paymentDTO1.setPaymentId(null);
        assertThat(paymentDTO1).isNotEqualTo(paymentDTO2);
    }
}
