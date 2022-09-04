package com.jana.bms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LanguageDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(LanguageDTO.class);
        LanguageDTO languageDTO1 = new LanguageDTO();
        languageDTO1.setLanguageId(1L);
        LanguageDTO languageDTO2 = new LanguageDTO();
        assertThat(languageDTO1).isNotEqualTo(languageDTO2);
        languageDTO2.setLanguageId(languageDTO1.getLanguageId());
        assertThat(languageDTO1).isEqualTo(languageDTO2);
        languageDTO2.setLanguageId(2L);
        assertThat(languageDTO1).isNotEqualTo(languageDTO2);
        languageDTO1.setLanguageId(null);
        assertThat(languageDTO1).isNotEqualTo(languageDTO2);
    }
}
