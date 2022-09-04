package com.jana.bms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CountryDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CountryDTO.class);
        CountryDTO countryDTO1 = new CountryDTO();
        countryDTO1.setCountryId(1L);
        CountryDTO countryDTO2 = new CountryDTO();
        assertThat(countryDTO1).isNotEqualTo(countryDTO2);
        countryDTO2.setCountryId(countryDTO1.getCountryId());
        assertThat(countryDTO1).isEqualTo(countryDTO2);
        countryDTO2.setCountryId(2L);
        assertThat(countryDTO1).isNotEqualTo(countryDTO2);
        countryDTO1.setCountryId(null);
        assertThat(countryDTO1).isNotEqualTo(countryDTO2);
    }
}
