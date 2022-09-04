package com.jana.bms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MovieDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MovieDTO.class);
        MovieDTO movieDTO1 = new MovieDTO();
        movieDTO1.setMovieId(1L);
        MovieDTO movieDTO2 = new MovieDTO();
        assertThat(movieDTO1).isNotEqualTo(movieDTO2);
        movieDTO2.setMovieId(movieDTO1.getMovieId());
        assertThat(movieDTO1).isEqualTo(movieDTO2);
        movieDTO2.setMovieId(2L);
        assertThat(movieDTO1).isNotEqualTo(movieDTO2);
        movieDTO1.setMovieId(null);
        assertThat(movieDTO1).isNotEqualTo(movieDTO2);
    }
}
