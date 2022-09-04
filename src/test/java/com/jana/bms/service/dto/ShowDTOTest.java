package com.jana.bms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShowDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShowDTO.class);
        ShowDTO showDTO1 = new ShowDTO();
        showDTO1.setShowId(1L);
        ShowDTO showDTO2 = new ShowDTO();
        assertThat(showDTO1).isNotEqualTo(showDTO2);
        showDTO2.setShowId(showDTO1.getShowId());
        assertThat(showDTO1).isEqualTo(showDTO2);
        showDTO2.setShowId(2L);
        assertThat(showDTO1).isNotEqualTo(showDTO2);
        showDTO1.setShowId(null);
        assertThat(showDTO1).isNotEqualTo(showDTO2);
    }
}
