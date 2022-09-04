package com.jana.bms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CityDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CityDTO.class);
        CityDTO cityDTO1 = new CityDTO();
        cityDTO1.setCityId(1L);
        CityDTO cityDTO2 = new CityDTO();
        assertThat(cityDTO1).isNotEqualTo(cityDTO2);
        cityDTO2.setCityId(cityDTO1.getCityId());
        assertThat(cityDTO1).isEqualTo(cityDTO2);
        cityDTO2.setCityId(2L);
        assertThat(cityDTO1).isNotEqualTo(cityDTO2);
        cityDTO1.setCityId(null);
        assertThat(cityDTO1).isNotEqualTo(cityDTO2);
    }
}
