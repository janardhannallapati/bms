package com.jana.bms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScreenTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Screen.class);
        Screen screen1 = new Screen();
        screen1.setScreenId(1L);
        Screen screen2 = new Screen();
        screen2.setScreenId(screen1.getScreenId());
        assertThat(screen1).isEqualTo(screen2);
        screen2.setScreenId(2L);
        assertThat(screen1).isNotEqualTo(screen2);
        screen1.setScreenId(null);
        assertThat(screen1).isNotEqualTo(screen2);
    }
}
