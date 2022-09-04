package com.jana.bms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AddressTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Address.class);
        Address address1 = new Address();
        address1.setAddressId(1L);
        Address address2 = new Address();
        address2.setAddressId(address1.getAddressId());
        assertThat(address1).isEqualTo(address2);
        address2.setAddressId(2L);
        assertThat(address1).isNotEqualTo(address2);
        address1.setAddressId(null);
        assertThat(address1).isNotEqualTo(address2);
    }
}
