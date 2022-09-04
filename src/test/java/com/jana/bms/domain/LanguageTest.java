package com.jana.bms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LanguageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Language.class);
        Language language1 = new Language();
        language1.setLanguageId(1L);
        Language language2 = new Language();
        language2.setLanguageId(language1.getLanguageId());
        assertThat(language1).isEqualTo(language2);
        language2.setLanguageId(2L);
        assertThat(language1).isNotEqualTo(language2);
        language1.setLanguageId(null);
        assertThat(language1).isNotEqualTo(language2);
    }
}
