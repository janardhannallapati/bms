package com.jana.bms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TheatreDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TheatreDTO.class);
        TheatreDTO theatreDTO1 = new TheatreDTO();
        theatreDTO1.setTheatreId(1L);
        TheatreDTO theatreDTO2 = new TheatreDTO();
        assertThat(theatreDTO1).isNotEqualTo(theatreDTO2);
        theatreDTO2.setTheatreId(theatreDTO1.getTheatreId());
        assertThat(theatreDTO1).isEqualTo(theatreDTO2);
        theatreDTO2.setTheatreId(2L);
        assertThat(theatreDTO1).isNotEqualTo(theatreDTO2);
        theatreDTO1.setTheatreId(null);
        assertThat(theatreDTO1).isNotEqualTo(theatreDTO2);
    }
}
