package com.jana.bms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SeatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Seat.class);
        Seat seat1 = new Seat();
        seat1.setSeatId(1L);
        Seat seat2 = new Seat();
        seat2.setSeatId(seat1.getSeatId());
        assertThat(seat1).isEqualTo(seat2);
        seat2.setSeatId(2L);
        assertThat(seat1).isNotEqualTo(seat2);
        seat1.setSeatId(null);
        assertThat(seat1).isNotEqualTo(seat2);
    }
}
