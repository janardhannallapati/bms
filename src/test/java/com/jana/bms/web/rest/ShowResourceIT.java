package com.jana.bms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jana.bms.IntegrationTest;
import com.jana.bms.domain.Movie;
import com.jana.bms.domain.Screen;
import com.jana.bms.domain.Seat;
import com.jana.bms.domain.Show;
import com.jana.bms.repository.ShowRepository;
import com.jana.bms.service.ShowService;
import com.jana.bms.service.criteria.ShowCriteria;
import com.jana.bms.service.dto.ShowDTO;
import com.jana.bms.service.mapper.ShowMapper;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link ShowResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ShowResourceIT {

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATE = LocalDate.ofEpochDay(-1L);

    private static final Instant DEFAULT_START_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/shows";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{showId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ShowRepository showRepository;

    @Mock
    private ShowRepository showRepositoryMock;

    @Autowired
    private ShowMapper showMapper;

    @Mock
    private ShowService showServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShowMockMvc;

    private Show show;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Show createEntity(EntityManager em) {
        Show show = new Show().date(DEFAULT_DATE).startTime(DEFAULT_START_TIME).endTime(DEFAULT_END_TIME);
        // Add required entity
        Movie movie;
        if (TestUtil.findAll(em, Movie.class).isEmpty()) {
            movie = MovieResourceIT.createEntity(em);
            em.persist(movie);
            em.flush();
        } else {
            movie = TestUtil.findAll(em, Movie.class).get(0);
        }
        show.setMovie(movie);
        // Add required entity
        Screen screen;
        if (TestUtil.findAll(em, Screen.class).isEmpty()) {
            screen = ScreenResourceIT.createEntity(em);
            em.persist(screen);
            em.flush();
        } else {
            screen = TestUtil.findAll(em, Screen.class).get(0);
        }
        show.setScreen(screen);
        // Add required entity
        Seat seat;
        if (TestUtil.findAll(em, Seat.class).isEmpty()) {
            seat = SeatResourceIT.createEntity(em);
            em.persist(seat);
            em.flush();
        } else {
            seat = TestUtil.findAll(em, Seat.class).get(0);
        }
        show.getSeats().add(seat);
        return show;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Show createUpdatedEntity(EntityManager em) {
        Show show = new Show().date(UPDATED_DATE).startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);
        // Add required entity
        Movie movie;
        if (TestUtil.findAll(em, Movie.class).isEmpty()) {
            movie = MovieResourceIT.createUpdatedEntity(em);
            em.persist(movie);
            em.flush();
        } else {
            movie = TestUtil.findAll(em, Movie.class).get(0);
        }
        show.setMovie(movie);
        // Add required entity
        Screen screen;
        if (TestUtil.findAll(em, Screen.class).isEmpty()) {
            screen = ScreenResourceIT.createUpdatedEntity(em);
            em.persist(screen);
            em.flush();
        } else {
            screen = TestUtil.findAll(em, Screen.class).get(0);
        }
        show.setScreen(screen);
        // Add required entity
        Seat seat;
        if (TestUtil.findAll(em, Seat.class).isEmpty()) {
            seat = SeatResourceIT.createUpdatedEntity(em);
            em.persist(seat);
            em.flush();
        } else {
            seat = TestUtil.findAll(em, Seat.class).get(0);
        }
        show.getSeats().add(seat);
        return show;
    }

    @BeforeEach
    public void initTest() {
        show = createEntity(em);
    }

    @Test
    @Transactional
    void createShow() throws Exception {
        int databaseSizeBeforeCreate = showRepository.findAll().size();
        // Create the Show
        ShowDTO showDTO = showMapper.toDto(show);
        restShowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(showDTO)))
            .andExpect(status().isCreated());

        // Validate the Show in the database
        List<Show> showList = showRepository.findAll();
        assertThat(showList).hasSize(databaseSizeBeforeCreate + 1);
        Show testShow = showList.get(showList.size() - 1);
        assertThat(testShow.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testShow.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testShow.getEndTime()).isEqualTo(DEFAULT_END_TIME);
    }

    @Test
    @Transactional
    void createShowWithExistingId() throws Exception {
        // Create the Show with an existing ID
        show.setShowId(1L);
        ShowDTO showDTO = showMapper.toDto(show);

        int databaseSizeBeforeCreate = showRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(showDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Show in the database
        List<Show> showList = showRepository.findAll();
        assertThat(showList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = showRepository.findAll().size();
        // set the field null
        show.setDate(null);

        // Create the Show, which fails.
        ShowDTO showDTO = showMapper.toDto(show);

        restShowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(showDTO)))
            .andExpect(status().isBadRequest());

        List<Show> showList = showRepository.findAll();
        assertThat(showList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = showRepository.findAll().size();
        // set the field null
        show.setStartTime(null);

        // Create the Show, which fails.
        ShowDTO showDTO = showMapper.toDto(show);

        restShowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(showDTO)))
            .andExpect(status().isBadRequest());

        List<Show> showList = showRepository.findAll();
        assertThat(showList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndTimeIsRequired() throws Exception {
        int databaseSizeBeforeTest = showRepository.findAll().size();
        // set the field null
        show.setEndTime(null);

        // Create the Show, which fails.
        ShowDTO showDTO = showMapper.toDto(show);

        restShowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(showDTO)))
            .andExpect(status().isBadRequest());

        List<Show> showList = showRepository.findAll();
        assertThat(showList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShows() throws Exception {
        // Initialize the database
        showRepository.saveAndFlush(show);

        // Get all the showList
        restShowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=showId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].showId").value(hasItem(show.getShowId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShowsWithEagerRelationshipsIsEnabled() throws Exception {
        when(showServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShowMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(showServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllShowsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(showServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restShowMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(showRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getShow() throws Exception {
        // Initialize the database
        showRepository.saveAndFlush(show);

        // Get the show
        restShowMockMvc
            .perform(get(ENTITY_API_URL_ID, show.getShowId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.showId").value(show.getShowId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()))
            .andExpect(jsonPath("$.startTime").value(DEFAULT_START_TIME.toString()))
            .andExpect(jsonPath("$.endTime").value(DEFAULT_END_TIME.toString()));
    }

    @Test
    @Transactional
    void getShowsByIdFiltering() throws Exception {
        // Initialize the database
        showRepository.saveAndFlush(show);

        Long id = show.getShowId();

        defaultShowShouldBeFound("showId.equals=" + id);
        defaultShowShouldNotBeFound("showId.notEquals=" + id);

        defaultShowShouldBeFound("showId.greaterThanOrEqual=" + id);
        defaultShowShouldNotBeFound("showId.greaterThan=" + id);

        defaultShowShouldBeFound("showId.lessThanOrEqual=" + id);
        defaultShowShouldNotBeFound("showId.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShowsByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        showRepository.saveAndFlush(show);

        // Get all the showList where date equals to DEFAULT_DATE
        defaultShowShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the showList where date equals to UPDATED_DATE
        defaultShowShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllShowsByDateIsInShouldWork() throws Exception {
        // Initialize the database
        showRepository.saveAndFlush(show);

        // Get all the showList where date in DEFAULT_DATE or UPDATED_DATE
        defaultShowShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the showList where date equals to UPDATED_DATE
        defaultShowShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllShowsByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        showRepository.saveAndFlush(show);

        // Get all the showList where date is not null
        defaultShowShouldBeFound("date.specified=true");

        // Get all the showList where date is null
        defaultShowShouldNotBeFound("date.specified=false");
    }

    @Test
    @Transactional
    void getAllShowsByDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        showRepository.saveAndFlush(show);

        // Get all the showList where date is greater than or equal to DEFAULT_DATE
        defaultShowShouldBeFound("date.greaterThanOrEqual=" + DEFAULT_DATE);

        // Get all the showList where date is greater than or equal to UPDATED_DATE
        defaultShowShouldNotBeFound("date.greaterThanOrEqual=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllShowsByDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        showRepository.saveAndFlush(show);

        // Get all the showList where date is less than or equal to DEFAULT_DATE
        defaultShowShouldBeFound("date.lessThanOrEqual=" + DEFAULT_DATE);

        // Get all the showList where date is less than or equal to SMALLER_DATE
        defaultShowShouldNotBeFound("date.lessThanOrEqual=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllShowsByDateIsLessThanSomething() throws Exception {
        // Initialize the database
        showRepository.saveAndFlush(show);

        // Get all the showList where date is less than DEFAULT_DATE
        defaultShowShouldNotBeFound("date.lessThan=" + DEFAULT_DATE);

        // Get all the showList where date is less than UPDATED_DATE
        defaultShowShouldBeFound("date.lessThan=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllShowsByDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        showRepository.saveAndFlush(show);

        // Get all the showList where date is greater than DEFAULT_DATE
        defaultShowShouldNotBeFound("date.greaterThan=" + DEFAULT_DATE);

        // Get all the showList where date is greater than SMALLER_DATE
        defaultShowShouldBeFound("date.greaterThan=" + SMALLER_DATE);
    }

    @Test
    @Transactional
    void getAllShowsByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        showRepository.saveAndFlush(show);

        // Get all the showList where startTime equals to DEFAULT_START_TIME
        defaultShowShouldBeFound("startTime.equals=" + DEFAULT_START_TIME);

        // Get all the showList where startTime equals to UPDATED_START_TIME
        defaultShowShouldNotBeFound("startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllShowsByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        showRepository.saveAndFlush(show);

        // Get all the showList where startTime in DEFAULT_START_TIME or UPDATED_START_TIME
        defaultShowShouldBeFound("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME);

        // Get all the showList where startTime equals to UPDATED_START_TIME
        defaultShowShouldNotBeFound("startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllShowsByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        showRepository.saveAndFlush(show);

        // Get all the showList where startTime is not null
        defaultShowShouldBeFound("startTime.specified=true");

        // Get all the showList where startTime is null
        defaultShowShouldNotBeFound("startTime.specified=false");
    }

    @Test
    @Transactional
    void getAllShowsByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        showRepository.saveAndFlush(show);

        // Get all the showList where endTime equals to DEFAULT_END_TIME
        defaultShowShouldBeFound("endTime.equals=" + DEFAULT_END_TIME);

        // Get all the showList where endTime equals to UPDATED_END_TIME
        defaultShowShouldNotBeFound("endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllShowsByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        showRepository.saveAndFlush(show);

        // Get all the showList where endTime in DEFAULT_END_TIME or UPDATED_END_TIME
        defaultShowShouldBeFound("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME);

        // Get all the showList where endTime equals to UPDATED_END_TIME
        defaultShowShouldNotBeFound("endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllShowsByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        showRepository.saveAndFlush(show);

        // Get all the showList where endTime is not null
        defaultShowShouldBeFound("endTime.specified=true");

        // Get all the showList where endTime is null
        defaultShowShouldNotBeFound("endTime.specified=false");
    }

    @Test
    @Transactional
    void getAllShowsByMovieIsEqualToSomething() throws Exception {
        Movie movie;
        if (TestUtil.findAll(em, Movie.class).isEmpty()) {
            showRepository.saveAndFlush(show);
            movie = MovieResourceIT.createEntity(em);
        } else {
            movie = TestUtil.findAll(em, Movie.class).get(0);
        }
        em.persist(movie);
        em.flush();
        show.setMovie(movie);
        showRepository.saveAndFlush(show);
        Long movieId = movie.getMovieId();

        // Get all the showList where movie equals to movieId
        defaultShowShouldBeFound("movieId.equals=" + movieId);

        // Get all the showList where movie equals to (movieId + 1)
        defaultShowShouldNotBeFound("movieId.equals=" + (movieId + 1));
    }

    @Test
    @Transactional
    void getAllShowsByScreenIsEqualToSomething() throws Exception {
        Screen screen;
        if (TestUtil.findAll(em, Screen.class).isEmpty()) {
            showRepository.saveAndFlush(show);
            screen = ScreenResourceIT.createEntity(em);
        } else {
            screen = TestUtil.findAll(em, Screen.class).get(0);
        }
        em.persist(screen);
        em.flush();
        show.setScreen(screen);
        showRepository.saveAndFlush(show);
        Long screenId = screen.getScreenId();

        // Get all the showList where screen equals to screenId
        defaultShowShouldBeFound("screenId.equals=" + screenId);

        // Get all the showList where screen equals to (screenId + 1)
        defaultShowShouldNotBeFound("screenId.equals=" + (screenId + 1));
    }

    @Test
    @Transactional
    void getAllShowsBySeatIsEqualToSomething() throws Exception {
        Seat seat;
        if (TestUtil.findAll(em, Seat.class).isEmpty()) {
            showRepository.saveAndFlush(show);
            seat = SeatResourceIT.createEntity(em);
        } else {
            seat = TestUtil.findAll(em, Seat.class).get(0);
        }
        em.persist(seat);
        em.flush();
        show.addSeat(seat);
        showRepository.saveAndFlush(show);
        Long seatId = seat.getSeatId();

        // Get all the showList where seat equals to seatId
        defaultShowShouldBeFound("seatId.equals=" + seatId);

        // Get all the showList where seat equals to (seatId + 1)
        defaultShowShouldNotBeFound("seatId.equals=" + (seatId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShowShouldBeFound(String filter) throws Exception {
        restShowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=showId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].showId").value(hasItem(show.getShowId().intValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(DEFAULT_START_TIME.toString())))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(DEFAULT_END_TIME.toString())));

        // Check, that the count call also returns 1
        restShowMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=showId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShowShouldNotBeFound(String filter) throws Exception {
        restShowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=showId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShowMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=showId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShow() throws Exception {
        // Get the show
        restShowMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewShow() throws Exception {
        // Initialize the database
        showRepository.saveAndFlush(show);

        int databaseSizeBeforeUpdate = showRepository.findAll().size();

        // Update the show
        Show updatedShow = showRepository.findById(show.getShowId()).get();
        // Disconnect from session so that the updates on updatedShow are not directly saved in db
        em.detach(updatedShow);
        updatedShow.date(UPDATED_DATE).startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);
        ShowDTO showDTO = showMapper.toDto(updatedShow);

        restShowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, showDTO.getShowId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(showDTO))
            )
            .andExpect(status().isOk());

        // Validate the Show in the database
        List<Show> showList = showRepository.findAll();
        assertThat(showList).hasSize(databaseSizeBeforeUpdate);
        Show testShow = showList.get(showList.size() - 1);
        assertThat(testShow.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testShow.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testShow.getEndTime()).isEqualTo(UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void putNonExistingShow() throws Exception {
        int databaseSizeBeforeUpdate = showRepository.findAll().size();
        show.setShowId(count.incrementAndGet());

        // Create the Show
        ShowDTO showDTO = showMapper.toDto(show);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, showDTO.getShowId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(showDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Show in the database
        List<Show> showList = showRepository.findAll();
        assertThat(showList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShow() throws Exception {
        int databaseSizeBeforeUpdate = showRepository.findAll().size();
        show.setShowId(count.incrementAndGet());

        // Create the Show
        ShowDTO showDTO = showMapper.toDto(show);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(showDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Show in the database
        List<Show> showList = showRepository.findAll();
        assertThat(showList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShow() throws Exception {
        int databaseSizeBeforeUpdate = showRepository.findAll().size();
        show.setShowId(count.incrementAndGet());

        // Create the Show
        ShowDTO showDTO = showMapper.toDto(show);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShowMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(showDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Show in the database
        List<Show> showList = showRepository.findAll();
        assertThat(showList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShowWithPatch() throws Exception {
        // Initialize the database
        showRepository.saveAndFlush(show);

        int databaseSizeBeforeUpdate = showRepository.findAll().size();

        // Update the show using partial update
        Show partialUpdatedShow = new Show();
        partialUpdatedShow.setShowId(show.getShowId());

        partialUpdatedShow.endTime(UPDATED_END_TIME);

        restShowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShow.getShowId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShow))
            )
            .andExpect(status().isOk());

        // Validate the Show in the database
        List<Show> showList = showRepository.findAll();
        assertThat(showList).hasSize(databaseSizeBeforeUpdate);
        Show testShow = showList.get(showList.size() - 1);
        assertThat(testShow.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testShow.getStartTime()).isEqualTo(DEFAULT_START_TIME);
        assertThat(testShow.getEndTime()).isEqualTo(UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void fullUpdateShowWithPatch() throws Exception {
        // Initialize the database
        showRepository.saveAndFlush(show);

        int databaseSizeBeforeUpdate = showRepository.findAll().size();

        // Update the show using partial update
        Show partialUpdatedShow = new Show();
        partialUpdatedShow.setShowId(show.getShowId());

        partialUpdatedShow.date(UPDATED_DATE).startTime(UPDATED_START_TIME).endTime(UPDATED_END_TIME);

        restShowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShow.getShowId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShow))
            )
            .andExpect(status().isOk());

        // Validate the Show in the database
        List<Show> showList = showRepository.findAll();
        assertThat(showList).hasSize(databaseSizeBeforeUpdate);
        Show testShow = showList.get(showList.size() - 1);
        assertThat(testShow.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testShow.getStartTime()).isEqualTo(UPDATED_START_TIME);
        assertThat(testShow.getEndTime()).isEqualTo(UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void patchNonExistingShow() throws Exception {
        int databaseSizeBeforeUpdate = showRepository.findAll().size();
        show.setShowId(count.incrementAndGet());

        // Create the Show
        ShowDTO showDTO = showMapper.toDto(show);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, showDTO.getShowId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(showDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Show in the database
        List<Show> showList = showRepository.findAll();
        assertThat(showList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShow() throws Exception {
        int databaseSizeBeforeUpdate = showRepository.findAll().size();
        show.setShowId(count.incrementAndGet());

        // Create the Show
        ShowDTO showDTO = showMapper.toDto(show);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(showDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Show in the database
        List<Show> showList = showRepository.findAll();
        assertThat(showList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShow() throws Exception {
        int databaseSizeBeforeUpdate = showRepository.findAll().size();
        show.setShowId(count.incrementAndGet());

        // Create the Show
        ShowDTO showDTO = showMapper.toDto(show);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShowMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(showDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Show in the database
        List<Show> showList = showRepository.findAll();
        assertThat(showList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShow() throws Exception {
        // Initialize the database
        showRepository.saveAndFlush(show);

        int databaseSizeBeforeDelete = showRepository.findAll().size();

        // Delete the show
        restShowMockMvc
            .perform(delete(ENTITY_API_URL_ID, show.getShowId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Show> showList = showRepository.findAll();
        assertThat(showList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
