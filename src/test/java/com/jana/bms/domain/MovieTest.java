package com.jana.bms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MovieTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Movie.class);
        Movie movie1 = new Movie();
        movie1.setMovieId(1L);
        Movie movie2 = new Movie();
        movie2.setMovieId(movie1.getMovieId());
        assertThat(movie1).isEqualTo(movie2);
        movie2.setMovieId(2L);
        assertThat(movie1).isNotEqualTo(movie2);
        movie1.setMovieId(null);
        assertThat(movie1).isNotEqualTo(movie2);
    }
}
