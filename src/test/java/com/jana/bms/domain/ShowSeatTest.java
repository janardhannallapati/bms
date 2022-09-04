package com.jana.bms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShowSeatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShowSeat.class);
        ShowSeat showSeat1 = new ShowSeat();
        showSeat1.setShowSeatId(1L);
        ShowSeat showSeat2 = new ShowSeat();
        showSeat2.setShowSeatId(showSeat1.getShowSeatId());
        assertThat(showSeat1).isEqualTo(showSeat2);
        showSeat2.setShowSeatId(2L);
        assertThat(showSeat1).isNotEqualTo(showSeat2);
        showSeat1.setShowSeatId(null);
        assertThat(showSeat1).isNotEqualTo(showSeat2);
    }
}
