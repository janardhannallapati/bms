package com.jana.bms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScreenDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScreenDTO.class);
        ScreenDTO screenDTO1 = new ScreenDTO();
        screenDTO1.setScreenId(1L);
        ScreenDTO screenDTO2 = new ScreenDTO();
        assertThat(screenDTO1).isNotEqualTo(screenDTO2);
        screenDTO2.setScreenId(screenDTO1.getScreenId());
        assertThat(screenDTO1).isEqualTo(screenDTO2);
        screenDTO2.setScreenId(2L);
        assertThat(screenDTO1).isNotEqualTo(screenDTO2);
        screenDTO1.setScreenId(null);
        assertThat(screenDTO1).isNotEqualTo(screenDTO2);
    }
}
