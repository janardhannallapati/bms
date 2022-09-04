package com.jana.bms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(City.class);
        City city1 = new City();
        city1.setCityId(1L);
        City city2 = new City();
        city2.setCityId(city1.getCityId());
        assertThat(city1).isEqualTo(city2);
        city2.setCityId(2L);
        assertThat(city1).isNotEqualTo(city2);
        city1.setCityId(null);
        assertThat(city1).isNotEqualTo(city2);
    }
}
