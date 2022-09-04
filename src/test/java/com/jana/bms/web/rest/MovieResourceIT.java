package com.jana.bms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jana.bms.IntegrationTest;
import com.jana.bms.domain.Actor;
import com.jana.bms.domain.Genre;
import com.jana.bms.domain.Language;
import com.jana.bms.domain.Movie;
import com.jana.bms.domain.enumeration.Rating;
import com.jana.bms.repository.MovieRepository;
import com.jana.bms.service.MovieService;
import com.jana.bms.service.criteria.MovieCriteria;
import com.jana.bms.service.dto.MovieDTO;
import com.jana.bms.service.mapper.MovieMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link MovieResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MovieResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_RELEASE_YEAR = 1870;
    private static final Integer UPDATED_RELEASE_YEAR = 1871;
    private static final Integer SMALLER_RELEASE_YEAR = 1870 - 1;

    private static final Integer DEFAULT_LENGTH = 1;
    private static final Integer UPDATED_LENGTH = 2;
    private static final Integer SMALLER_LENGTH = 1 - 1;

    private static final Rating DEFAULT_RATING = Rating.G;
    private static final Rating UPDATED_RATING = Rating.PG;

    private static final String ENTITY_API_URL = "/api/movies";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{movieId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private MovieRepository movieRepository;

    @Mock
    private MovieRepository movieRepositoryMock;

    @Autowired
    private MovieMapper movieMapper;

    @Mock
    private MovieService movieServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMovieMockMvc;

    private Movie movie;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Movie createEntity(EntityManager em) {
        Movie movie = new Movie()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .releaseYear(DEFAULT_RELEASE_YEAR)
            .length(DEFAULT_LENGTH)
            .rating(DEFAULT_RATING);
        // Add required entity
        Language language;
        if (TestUtil.findAll(em, Language.class).isEmpty()) {
            language = LanguageResourceIT.createEntity(em);
            em.persist(language);
            em.flush();
        } else {
            language = TestUtil.findAll(em, Language.class).get(0);
        }
        movie.setLanguage(language);
        // Add required entity
        Actor actor;
        if (TestUtil.findAll(em, Actor.class).isEmpty()) {
            actor = ActorResourceIT.createEntity(em);
            em.persist(actor);
            em.flush();
        } else {
            actor = TestUtil.findAll(em, Actor.class).get(0);
        }
        movie.getActors().add(actor);
        // Add required entity
        Genre genre;
        if (TestUtil.findAll(em, Genre.class).isEmpty()) {
            genre = GenreResourceIT.createEntity(em);
            em.persist(genre);
            em.flush();
        } else {
            genre = TestUtil.findAll(em, Genre.class).get(0);
        }
        movie.getGenres().add(genre);
        return movie;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Movie createUpdatedEntity(EntityManager em) {
        Movie movie = new Movie()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .releaseYear(UPDATED_RELEASE_YEAR)
            .length(UPDATED_LENGTH)
            .rating(UPDATED_RATING);
        // Add required entity
        Language language;
        if (TestUtil.findAll(em, Language.class).isEmpty()) {
            language = LanguageResourceIT.createUpdatedEntity(em);
            em.persist(language);
            em.flush();
        } else {
            language = TestUtil.findAll(em, Language.class).get(0);
        }
        movie.setLanguage(language);
        // Add required entity
        Actor actor;
        if (TestUtil.findAll(em, Actor.class).isEmpty()) {
            actor = ActorResourceIT.createUpdatedEntity(em);
            em.persist(actor);
            em.flush();
        } else {
            actor = TestUtil.findAll(em, Actor.class).get(0);
        }
        movie.getActors().add(actor);
        // Add required entity
        Genre genre;
        if (TestUtil.findAll(em, Genre.class).isEmpty()) {
            genre = GenreResourceIT.createUpdatedEntity(em);
            em.persist(genre);
            em.flush();
        } else {
            genre = TestUtil.findAll(em, Genre.class).get(0);
        }
        movie.getGenres().add(genre);
        return movie;
    }

    @BeforeEach
    public void initTest() {
        movie = createEntity(em);
    }

    @Test
    @Transactional
    void createMovie() throws Exception {
        int databaseSizeBeforeCreate = movieRepository.findAll().size();
        // Create the Movie
        MovieDTO movieDTO = movieMapper.toDto(movie);
        restMovieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isCreated());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeCreate + 1);
        Movie testMovie = movieList.get(movieList.size() - 1);
        assertThat(testMovie.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMovie.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMovie.getReleaseYear()).isEqualTo(DEFAULT_RELEASE_YEAR);
        assertThat(testMovie.getLength()).isEqualTo(DEFAULT_LENGTH);
        assertThat(testMovie.getRating()).isEqualTo(DEFAULT_RATING);
    }

    @Test
    @Transactional
    void createMovieWithExistingId() throws Exception {
        // Create the Movie with an existing ID
        movie.setMovieId(1L);
        MovieDTO movieDTO = movieMapper.toDto(movie);

        int databaseSizeBeforeCreate = movieRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMovieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = movieRepository.findAll().size();
        // set the field null
        movie.setTitle(null);

        // Create the Movie, which fails.
        MovieDTO movieDTO = movieMapper.toDto(movie);

        restMovieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isBadRequest());

        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRatingIsRequired() throws Exception {
        int databaseSizeBeforeTest = movieRepository.findAll().size();
        // set the field null
        movie.setRating(null);

        // Create the Movie, which fails.
        MovieDTO movieDTO = movieMapper.toDto(movie);

        restMovieMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isBadRequest());

        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMovies() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList
        restMovieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=movieId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].movieId").value(hasItem(movie.getMovieId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].releaseYear").value(hasItem(DEFAULT_RELEASE_YEAR)))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMoviesWithEagerRelationshipsIsEnabled() throws Exception {
        when(movieServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMovieMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(movieServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMoviesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(movieServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMovieMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(movieRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMovie() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get the movie
        restMovieMockMvc
            .perform(get(ENTITY_API_URL_ID, movie.getMovieId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.movieId").value(movie.getMovieId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.releaseYear").value(DEFAULT_RELEASE_YEAR))
            .andExpect(jsonPath("$.length").value(DEFAULT_LENGTH))
            .andExpect(jsonPath("$.rating").value(DEFAULT_RATING.toString()));
    }

    @Test
    @Transactional
    void getMoviesByIdFiltering() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        Long id = movie.getMovieId();

        defaultMovieShouldBeFound("movieId.equals=" + id);
        defaultMovieShouldNotBeFound("movieId.notEquals=" + id);

        defaultMovieShouldBeFound("movieId.greaterThanOrEqual=" + id);
        defaultMovieShouldNotBeFound("movieId.greaterThan=" + id);

        defaultMovieShouldBeFound("movieId.lessThanOrEqual=" + id);
        defaultMovieShouldNotBeFound("movieId.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMoviesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where title equals to DEFAULT_TITLE
        defaultMovieShouldBeFound("title.equals=" + DEFAULT_TITLE);

        // Get all the movieList where title equals to UPDATED_TITLE
        defaultMovieShouldNotBeFound("title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMoviesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where title in DEFAULT_TITLE or UPDATED_TITLE
        defaultMovieShouldBeFound("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE);

        // Get all the movieList where title equals to UPDATED_TITLE
        defaultMovieShouldNotBeFound("title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMoviesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where title is not null
        defaultMovieShouldBeFound("title.specified=true");

        // Get all the movieList where title is null
        defaultMovieShouldNotBeFound("title.specified=false");
    }

    @Test
    @Transactional
    void getAllMoviesByTitleContainsSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where title contains DEFAULT_TITLE
        defaultMovieShouldBeFound("title.contains=" + DEFAULT_TITLE);

        // Get all the movieList where title contains UPDATED_TITLE
        defaultMovieShouldNotBeFound("title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMoviesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where title does not contain DEFAULT_TITLE
        defaultMovieShouldNotBeFound("title.doesNotContain=" + DEFAULT_TITLE);

        // Get all the movieList where title does not contain UPDATED_TITLE
        defaultMovieShouldBeFound("title.doesNotContain=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMoviesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where description equals to DEFAULT_DESCRIPTION
        defaultMovieShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the movieList where description equals to UPDATED_DESCRIPTION
        defaultMovieShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMoviesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultMovieShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the movieList where description equals to UPDATED_DESCRIPTION
        defaultMovieShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMoviesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where description is not null
        defaultMovieShouldBeFound("description.specified=true");

        // Get all the movieList where description is null
        defaultMovieShouldNotBeFound("description.specified=false");
    }

    @Test
    @Transactional
    void getAllMoviesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where description contains DEFAULT_DESCRIPTION
        defaultMovieShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the movieList where description contains UPDATED_DESCRIPTION
        defaultMovieShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMoviesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where description does not contain DEFAULT_DESCRIPTION
        defaultMovieShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the movieList where description does not contain UPDATED_DESCRIPTION
        defaultMovieShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMoviesByReleaseYearIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where releaseYear equals to DEFAULT_RELEASE_YEAR
        defaultMovieShouldBeFound("releaseYear.equals=" + DEFAULT_RELEASE_YEAR);

        // Get all the movieList where releaseYear equals to UPDATED_RELEASE_YEAR
        defaultMovieShouldNotBeFound("releaseYear.equals=" + UPDATED_RELEASE_YEAR);
    }

    @Test
    @Transactional
    void getAllMoviesByReleaseYearIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where releaseYear in DEFAULT_RELEASE_YEAR or UPDATED_RELEASE_YEAR
        defaultMovieShouldBeFound("releaseYear.in=" + DEFAULT_RELEASE_YEAR + "," + UPDATED_RELEASE_YEAR);

        // Get all the movieList where releaseYear equals to UPDATED_RELEASE_YEAR
        defaultMovieShouldNotBeFound("releaseYear.in=" + UPDATED_RELEASE_YEAR);
    }

    @Test
    @Transactional
    void getAllMoviesByReleaseYearIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where releaseYear is not null
        defaultMovieShouldBeFound("releaseYear.specified=true");

        // Get all the movieList where releaseYear is null
        defaultMovieShouldNotBeFound("releaseYear.specified=false");
    }

    @Test
    @Transactional
    void getAllMoviesByReleaseYearIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where releaseYear is greater than or equal to DEFAULT_RELEASE_YEAR
        defaultMovieShouldBeFound("releaseYear.greaterThanOrEqual=" + DEFAULT_RELEASE_YEAR);

        // Get all the movieList where releaseYear is greater than or equal to (DEFAULT_RELEASE_YEAR + 1)
        defaultMovieShouldNotBeFound("releaseYear.greaterThanOrEqual=" + (DEFAULT_RELEASE_YEAR + 1));
    }

    @Test
    @Transactional
    void getAllMoviesByReleaseYearIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where releaseYear is less than or equal to DEFAULT_RELEASE_YEAR
        defaultMovieShouldBeFound("releaseYear.lessThanOrEqual=" + DEFAULT_RELEASE_YEAR);

        // Get all the movieList where releaseYear is less than or equal to SMALLER_RELEASE_YEAR
        defaultMovieShouldNotBeFound("releaseYear.lessThanOrEqual=" + SMALLER_RELEASE_YEAR);
    }

    @Test
    @Transactional
    void getAllMoviesByReleaseYearIsLessThanSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where releaseYear is less than DEFAULT_RELEASE_YEAR
        defaultMovieShouldNotBeFound("releaseYear.lessThan=" + DEFAULT_RELEASE_YEAR);

        // Get all the movieList where releaseYear is less than (DEFAULT_RELEASE_YEAR + 1)
        defaultMovieShouldBeFound("releaseYear.lessThan=" + (DEFAULT_RELEASE_YEAR + 1));
    }

    @Test
    @Transactional
    void getAllMoviesByReleaseYearIsGreaterThanSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where releaseYear is greater than DEFAULT_RELEASE_YEAR
        defaultMovieShouldNotBeFound("releaseYear.greaterThan=" + DEFAULT_RELEASE_YEAR);

        // Get all the movieList where releaseYear is greater than SMALLER_RELEASE_YEAR
        defaultMovieShouldBeFound("releaseYear.greaterThan=" + SMALLER_RELEASE_YEAR);
    }

    @Test
    @Transactional
    void getAllMoviesByLengthIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where length equals to DEFAULT_LENGTH
        defaultMovieShouldBeFound("length.equals=" + DEFAULT_LENGTH);

        // Get all the movieList where length equals to UPDATED_LENGTH
        defaultMovieShouldNotBeFound("length.equals=" + UPDATED_LENGTH);
    }

    @Test
    @Transactional
    void getAllMoviesByLengthIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where length in DEFAULT_LENGTH or UPDATED_LENGTH
        defaultMovieShouldBeFound("length.in=" + DEFAULT_LENGTH + "," + UPDATED_LENGTH);

        // Get all the movieList where length equals to UPDATED_LENGTH
        defaultMovieShouldNotBeFound("length.in=" + UPDATED_LENGTH);
    }

    @Test
    @Transactional
    void getAllMoviesByLengthIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where length is not null
        defaultMovieShouldBeFound("length.specified=true");

        // Get all the movieList where length is null
        defaultMovieShouldNotBeFound("length.specified=false");
    }

    @Test
    @Transactional
    void getAllMoviesByLengthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where length is greater than or equal to DEFAULT_LENGTH
        defaultMovieShouldBeFound("length.greaterThanOrEqual=" + DEFAULT_LENGTH);

        // Get all the movieList where length is greater than or equal to UPDATED_LENGTH
        defaultMovieShouldNotBeFound("length.greaterThanOrEqual=" + UPDATED_LENGTH);
    }

    @Test
    @Transactional
    void getAllMoviesByLengthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where length is less than or equal to DEFAULT_LENGTH
        defaultMovieShouldBeFound("length.lessThanOrEqual=" + DEFAULT_LENGTH);

        // Get all the movieList where length is less than or equal to SMALLER_LENGTH
        defaultMovieShouldNotBeFound("length.lessThanOrEqual=" + SMALLER_LENGTH);
    }

    @Test
    @Transactional
    void getAllMoviesByLengthIsLessThanSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where length is less than DEFAULT_LENGTH
        defaultMovieShouldNotBeFound("length.lessThan=" + DEFAULT_LENGTH);

        // Get all the movieList where length is less than UPDATED_LENGTH
        defaultMovieShouldBeFound("length.lessThan=" + UPDATED_LENGTH);
    }

    @Test
    @Transactional
    void getAllMoviesByLengthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where length is greater than DEFAULT_LENGTH
        defaultMovieShouldNotBeFound("length.greaterThan=" + DEFAULT_LENGTH);

        // Get all the movieList where length is greater than SMALLER_LENGTH
        defaultMovieShouldBeFound("length.greaterThan=" + SMALLER_LENGTH);
    }

    @Test
    @Transactional
    void getAllMoviesByRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where rating equals to DEFAULT_RATING
        defaultMovieShouldBeFound("rating.equals=" + DEFAULT_RATING);

        // Get all the movieList where rating equals to UPDATED_RATING
        defaultMovieShouldNotBeFound("rating.equals=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllMoviesByRatingIsInShouldWork() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where rating in DEFAULT_RATING or UPDATED_RATING
        defaultMovieShouldBeFound("rating.in=" + DEFAULT_RATING + "," + UPDATED_RATING);

        // Get all the movieList where rating equals to UPDATED_RATING
        defaultMovieShouldNotBeFound("rating.in=" + UPDATED_RATING);
    }

    @Test
    @Transactional
    void getAllMoviesByRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        // Get all the movieList where rating is not null
        defaultMovieShouldBeFound("rating.specified=true");

        // Get all the movieList where rating is null
        defaultMovieShouldNotBeFound("rating.specified=false");
    }

    @Test
    @Transactional
    void getAllMoviesByLanguageIsEqualToSomething() throws Exception {
        Language language;
        if (TestUtil.findAll(em, Language.class).isEmpty()) {
            movieRepository.saveAndFlush(movie);
            language = LanguageResourceIT.createEntity(em);
        } else {
            language = TestUtil.findAll(em, Language.class).get(0);
        }
        em.persist(language);
        em.flush();
        movie.setLanguage(language);
        movieRepository.saveAndFlush(movie);
        Long languageId = language.getLanguageId();

        // Get all the movieList where language equals to languageId
        defaultMovieShouldBeFound("languageId.equals=" + languageId);

        // Get all the movieList where language equals to (languageId + 1)
        defaultMovieShouldNotBeFound("languageId.equals=" + (languageId + 1));
    }

    @Test
    @Transactional
    void getAllMoviesByActorIsEqualToSomething() throws Exception {
        Actor actor;
        if (TestUtil.findAll(em, Actor.class).isEmpty()) {
            movieRepository.saveAndFlush(movie);
            actor = ActorResourceIT.createEntity(em);
        } else {
            actor = TestUtil.findAll(em, Actor.class).get(0);
        }
        em.persist(actor);
        em.flush();
        movie.addActor(actor);
        movieRepository.saveAndFlush(movie);
        Long actorId = actor.getActorId();

        // Get all the movieList where actor equals to actorId
        defaultMovieShouldBeFound("actorId.equals=" + actorId);

        // Get all the movieList where actor equals to (actorId + 1)
        defaultMovieShouldNotBeFound("actorId.equals=" + (actorId + 1));
    }

    @Test
    @Transactional
    void getAllMoviesByGenreIsEqualToSomething() throws Exception {
        Genre genre;
        if (TestUtil.findAll(em, Genre.class).isEmpty()) {
            movieRepository.saveAndFlush(movie);
            genre = GenreResourceIT.createEntity(em);
        } else {
            genre = TestUtil.findAll(em, Genre.class).get(0);
        }
        em.persist(genre);
        em.flush();
        movie.addGenre(genre);
        movieRepository.saveAndFlush(movie);
        Long genreId = genre.getGenreId();

        // Get all the movieList where genre equals to genreId
        defaultMovieShouldBeFound("genreId.equals=" + genreId);

        // Get all the movieList where genre equals to (genreId + 1)
        defaultMovieShouldNotBeFound("genreId.equals=" + (genreId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMovieShouldBeFound(String filter) throws Exception {
        restMovieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=movieId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].movieId").value(hasItem(movie.getMovieId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].releaseYear").value(hasItem(DEFAULT_RELEASE_YEAR)))
            .andExpect(jsonPath("$.[*].length").value(hasItem(DEFAULT_LENGTH)))
            .andExpect(jsonPath("$.[*].rating").value(hasItem(DEFAULT_RATING.toString())));

        // Check, that the count call also returns 1
        restMovieMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=movieId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMovieShouldNotBeFound(String filter) throws Exception {
        restMovieMockMvc
            .perform(get(ENTITY_API_URL + "?sort=movieId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMovieMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=movieId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMovie() throws Exception {
        // Get the movie
        restMovieMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewMovie() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        int databaseSizeBeforeUpdate = movieRepository.findAll().size();

        // Update the movie
        Movie updatedMovie = movieRepository.findById(movie.getMovieId()).get();
        // Disconnect from session so that the updates on updatedMovie are not directly saved in db
        em.detach(updatedMovie);
        updatedMovie
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .releaseYear(UPDATED_RELEASE_YEAR)
            .length(UPDATED_LENGTH)
            .rating(UPDATED_RATING);
        MovieDTO movieDTO = movieMapper.toDto(updatedMovie);

        restMovieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, movieDTO.getMovieId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movieDTO))
            )
            .andExpect(status().isOk());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
        Movie testMovie = movieList.get(movieList.size() - 1);
        assertThat(testMovie.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMovie.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMovie.getReleaseYear()).isEqualTo(UPDATED_RELEASE_YEAR);
        assertThat(testMovie.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testMovie.getRating()).isEqualTo(UPDATED_RATING);
    }

    @Test
    @Transactional
    void putNonExistingMovie() throws Exception {
        int databaseSizeBeforeUpdate = movieRepository.findAll().size();
        movie.setMovieId(count.incrementAndGet());

        // Create the Movie
        MovieDTO movieDTO = movieMapper.toDto(movie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, movieDTO.getMovieId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMovie() throws Exception {
        int databaseSizeBeforeUpdate = movieRepository.findAll().size();
        movie.setMovieId(count.incrementAndGet());

        // Create the Movie
        MovieDTO movieDTO = movieMapper.toDto(movie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovieMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(movieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMovie() throws Exception {
        int databaseSizeBeforeUpdate = movieRepository.findAll().size();
        movie.setMovieId(count.incrementAndGet());

        // Create the Movie
        MovieDTO movieDTO = movieMapper.toDto(movie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovieMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMovieWithPatch() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        int databaseSizeBeforeUpdate = movieRepository.findAll().size();

        // Update the movie using partial update
        Movie partialUpdatedMovie = new Movie();
        partialUpdatedMovie.setMovieId(movie.getMovieId());

        partialUpdatedMovie.releaseYear(UPDATED_RELEASE_YEAR).length(UPDATED_LENGTH).rating(UPDATED_RATING);

        restMovieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMovie.getMovieId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMovie))
            )
            .andExpect(status().isOk());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
        Movie testMovie = movieList.get(movieList.size() - 1);
        assertThat(testMovie.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testMovie.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testMovie.getReleaseYear()).isEqualTo(UPDATED_RELEASE_YEAR);
        assertThat(testMovie.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testMovie.getRating()).isEqualTo(UPDATED_RATING);
    }

    @Test
    @Transactional
    void fullUpdateMovieWithPatch() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        int databaseSizeBeforeUpdate = movieRepository.findAll().size();

        // Update the movie using partial update
        Movie partialUpdatedMovie = new Movie();
        partialUpdatedMovie.setMovieId(movie.getMovieId());

        partialUpdatedMovie
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .releaseYear(UPDATED_RELEASE_YEAR)
            .length(UPDATED_LENGTH)
            .rating(UPDATED_RATING);

        restMovieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMovie.getMovieId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedMovie))
            )
            .andExpect(status().isOk());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
        Movie testMovie = movieList.get(movieList.size() - 1);
        assertThat(testMovie.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testMovie.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testMovie.getReleaseYear()).isEqualTo(UPDATED_RELEASE_YEAR);
        assertThat(testMovie.getLength()).isEqualTo(UPDATED_LENGTH);
        assertThat(testMovie.getRating()).isEqualTo(UPDATED_RATING);
    }

    @Test
    @Transactional
    void patchNonExistingMovie() throws Exception {
        int databaseSizeBeforeUpdate = movieRepository.findAll().size();
        movie.setMovieId(count.incrementAndGet());

        // Create the Movie
        MovieDTO movieDTO = movieMapper.toDto(movie);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMovieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, movieDTO.getMovieId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(movieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMovie() throws Exception {
        int databaseSizeBeforeUpdate = movieRepository.findAll().size();
        movie.setMovieId(count.incrementAndGet());

        // Create the Movie
        MovieDTO movieDTO = movieMapper.toDto(movie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovieMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(movieDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMovie() throws Exception {
        int databaseSizeBeforeUpdate = movieRepository.findAll().size();
        movie.setMovieId(count.incrementAndGet());

        // Create the Movie
        MovieDTO movieDTO = movieMapper.toDto(movie);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMovieMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(movieDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Movie in the database
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMovie() throws Exception {
        // Initialize the database
        movieRepository.saveAndFlush(movie);

        int databaseSizeBeforeDelete = movieRepository.findAll().size();

        // Delete the movie
        restMovieMockMvc
            .perform(delete(ENTITY_API_URL_ID, movie.getMovieId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Movie> movieList = movieRepository.findAll();
        assertThat(movieList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
