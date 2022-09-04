package com.jana.bms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShowTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Show.class);
        Show show1 = new Show();
        show1.setShowId(1L);
        Show show2 = new Show();
        show2.setShowId(show1.getShowId());
        assertThat(show1).isEqualTo(show2);
        show2.setShowId(2L);
        assertThat(show1).isNotEqualTo(show2);
        show1.setShowId(null);
        assertThat(show1).isNotEqualTo(show2);
    }
}
