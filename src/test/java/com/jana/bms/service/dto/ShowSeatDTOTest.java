package com.jana.bms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShowSeatDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShowSeatDTO.class);
        ShowSeatDTO showSeatDTO1 = new ShowSeatDTO();
        showSeatDTO1.setShowSeatId(1L);
        ShowSeatDTO showSeatDTO2 = new ShowSeatDTO();
        assertThat(showSeatDTO1).isNotEqualTo(showSeatDTO2);
        showSeatDTO2.setShowSeatId(showSeatDTO1.getShowSeatId());
        assertThat(showSeatDTO1).isEqualTo(showSeatDTO2);
        showSeatDTO2.setShowSeatId(2L);
        assertThat(showSeatDTO1).isNotEqualTo(showSeatDTO2);
        showSeatDTO1.setShowSeatId(null);
        assertThat(showSeatDTO1).isNotEqualTo(showSeatDTO2);
    }
}
