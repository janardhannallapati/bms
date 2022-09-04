package com.jana.bms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SeatDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SeatDTO.class);
        SeatDTO seatDTO1 = new SeatDTO();
        seatDTO1.setSeatId(1L);
        SeatDTO seatDTO2 = new SeatDTO();
        assertThat(seatDTO1).isNotEqualTo(seatDTO2);
        seatDTO2.setSeatId(seatDTO1.getSeatId());
        assertThat(seatDTO1).isEqualTo(seatDTO2);
        seatDTO2.setSeatId(2L);
        assertThat(seatDTO1).isNotEqualTo(seatDTO2);
        seatDTO1.setSeatId(null);
        assertThat(seatDTO1).isNotEqualTo(seatDTO2);
    }
}
