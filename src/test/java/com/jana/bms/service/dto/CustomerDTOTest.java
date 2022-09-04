package com.jana.bms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CustomerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CustomerDTO.class);
        CustomerDTO customerDTO1 = new CustomerDTO();
        customerDTO1.setCustomerId(1L);
        CustomerDTO customerDTO2 = new CustomerDTO();
        assertThat(customerDTO1).isNotEqualTo(customerDTO2);
        customerDTO2.setCustomerId(customerDTO1.getCustomerId());
        assertThat(customerDTO1).isEqualTo(customerDTO2);
        customerDTO2.setCustomerId(2L);
        assertThat(customerDTO1).isNotEqualTo(customerDTO2);
        customerDTO1.setCustomerId(null);
        assertThat(customerDTO1).isNotEqualTo(customerDTO2);
    }
}
