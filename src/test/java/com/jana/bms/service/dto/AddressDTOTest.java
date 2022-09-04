package com.jana.bms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AddressDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AddressDTO.class);
        AddressDTO addressDTO1 = new AddressDTO();
        addressDTO1.setAddressId(1L);
        AddressDTO addressDTO2 = new AddressDTO();
        assertThat(addressDTO1).isNotEqualTo(addressDTO2);
        addressDTO2.setAddressId(addressDTO1.getAddressId());
        assertThat(addressDTO1).isEqualTo(addressDTO2);
        addressDTO2.setAddressId(2L);
        assertThat(addressDTO1).isNotEqualTo(addressDTO2);
        addressDTO1.setAddressId(null);
        assertThat(addressDTO1).isNotEqualTo(addressDTO2);
    }
}
