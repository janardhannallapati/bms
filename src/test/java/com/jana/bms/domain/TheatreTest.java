package com.jana.bms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TheatreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Theatre.class);
        Theatre theatre1 = new Theatre();
        theatre1.setTheatreId(1L);
        Theatre theatre2 = new Theatre();
        theatre2.setTheatreId(theatre1.getTheatreId());
        assertThat(theatre1).isEqualTo(theatre2);
        theatre2.setTheatreId(2L);
        assertThat(theatre1).isNotEqualTo(theatre2);
        theatre1.setTheatreId(null);
        assertThat(theatre1).isNotEqualTo(theatre2);
    }
}
