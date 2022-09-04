package com.jana.bms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class GenreDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GenreDTO.class);
        GenreDTO genreDTO1 = new GenreDTO();
        genreDTO1.setGenreId(1L);
        GenreDTO genreDTO2 = new GenreDTO();
        assertThat(genreDTO1).isNotEqualTo(genreDTO2);
        genreDTO2.setGenreId(genreDTO1.getGenreId());
        assertThat(genreDTO1).isEqualTo(genreDTO2);
        genreDTO2.setGenreId(2L);
        assertThat(genreDTO1).isNotEqualTo(genreDTO2);
        genreDTO1.setGenreId(null);
        assertThat(genreDTO1).isNotEqualTo(genreDTO2);
    }
}
