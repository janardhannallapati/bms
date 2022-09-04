package com.jana.bms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jana.bms.IntegrationTest;
import com.jana.bms.domain.Screen;
import com.jana.bms.domain.Seat;
import com.jana.bms.domain.Show;
import com.jana.bms.repository.SeatRepository;
import com.jana.bms.service.criteria.SeatCriteria;
import com.jana.bms.service.dto.SeatDTO;
import com.jana.bms.service.mapper.SeatMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SeatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SeatResourceIT {

    private static final Integer DEFAULT_SEAT_NUMBER = 1;
    private static final Integer UPDATED_SEAT_NUMBER = 2;
    private static final Integer SMALLER_SEAT_NUMBER = 1 - 1;

    private static final String DEFAULT_SEAT_DESCR = "AAAAAAAAAA";
    private static final String UPDATED_SEAT_DESCR = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/seats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{seatId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SeatRepository seatRepository;

    @Autowired
    private SeatMapper seatMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSeatMockMvc;

    private Seat seat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Seat createEntity(EntityManager em) {
        Seat seat = new Seat().seatNumber(DEFAULT_SEAT_NUMBER).seatDescr(DEFAULT_SEAT_DESCR).type(DEFAULT_TYPE);
        // Add required entity
        Screen screen;
        if (TestUtil.findAll(em, Screen.class).isEmpty()) {
            screen = ScreenResourceIT.createEntity(em);
            em.persist(screen);
            em.flush();
        } else {
            screen = TestUtil.findAll(em, Screen.class).get(0);
        }
        seat.setScreen(screen);
        return seat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Seat createUpdatedEntity(EntityManager em) {
        Seat seat = new Seat().seatNumber(UPDATED_SEAT_NUMBER).seatDescr(UPDATED_SEAT_DESCR).type(UPDATED_TYPE);
        // Add required entity
        Screen screen;
        if (TestUtil.findAll(em, Screen.class).isEmpty()) {
            screen = ScreenResourceIT.createUpdatedEntity(em);
            em.persist(screen);
            em.flush();
        } else {
            screen = TestUtil.findAll(em, Screen.class).get(0);
        }
        seat.setScreen(screen);
        return seat;
    }

    @BeforeEach
    public void initTest() {
        seat = createEntity(em);
    }

    @Test
    @Transactional
    void createSeat() throws Exception {
        int databaseSizeBeforeCreate = seatRepository.findAll().size();
        // Create the Seat
        SeatDTO seatDTO = seatMapper.toDto(seat);
        restSeatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seatDTO)))
            .andExpect(status().isCreated());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeCreate + 1);
        Seat testSeat = seatList.get(seatList.size() - 1);
        assertThat(testSeat.getSeatNumber()).isEqualTo(DEFAULT_SEAT_NUMBER);
        assertThat(testSeat.getSeatDescr()).isEqualTo(DEFAULT_SEAT_DESCR);
        assertThat(testSeat.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createSeatWithExistingId() throws Exception {
        // Create the Seat with an existing ID
        seat.setSeatId(1L);
        SeatDTO seatDTO = seatMapper.toDto(seat);

        int databaseSizeBeforeCreate = seatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSeatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSeatNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = seatRepository.findAll().size();
        // set the field null
        seat.setSeatNumber(null);

        // Create the Seat, which fails.
        SeatDTO seatDTO = seatMapper.toDto(seat);

        restSeatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seatDTO)))
            .andExpect(status().isBadRequest());

        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = seatRepository.findAll().size();
        // set the field null
        seat.setType(null);

        // Create the Seat, which fails.
        SeatDTO seatDTO = seatMapper.toDto(seat);

        restSeatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seatDTO)))
            .andExpect(status().isBadRequest());

        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSeats() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList
        restSeatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=seatId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].seatId").value(hasItem(seat.getSeatId().intValue())))
            .andExpect(jsonPath("$.[*].seatNumber").value(hasItem(DEFAULT_SEAT_NUMBER)))
            .andExpect(jsonPath("$.[*].seatDescr").value(hasItem(DEFAULT_SEAT_DESCR)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    void getSeat() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get the seat
        restSeatMockMvc
            .perform(get(ENTITY_API_URL_ID, seat.getSeatId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.seatId").value(seat.getSeatId().intValue()))
            .andExpect(jsonPath("$.seatNumber").value(DEFAULT_SEAT_NUMBER))
            .andExpect(jsonPath("$.seatDescr").value(DEFAULT_SEAT_DESCR))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getSeatsByIdFiltering() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        Long id = seat.getSeatId();

        defaultSeatShouldBeFound("seatId.equals=" + id);
        defaultSeatShouldNotBeFound("seatId.notEquals=" + id);

        defaultSeatShouldBeFound("seatId.greaterThanOrEqual=" + id);
        defaultSeatShouldNotBeFound("seatId.greaterThan=" + id);

        defaultSeatShouldBeFound("seatId.lessThanOrEqual=" + id);
        defaultSeatShouldNotBeFound("seatId.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSeatsBySeatNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where seatNumber equals to DEFAULT_SEAT_NUMBER
        defaultSeatShouldBeFound("seatNumber.equals=" + DEFAULT_SEAT_NUMBER);

        // Get all the seatList where seatNumber equals to UPDATED_SEAT_NUMBER
        defaultSeatShouldNotBeFound("seatNumber.equals=" + UPDATED_SEAT_NUMBER);
    }

    @Test
    @Transactional
    void getAllSeatsBySeatNumberIsInShouldWork() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where seatNumber in DEFAULT_SEAT_NUMBER or UPDATED_SEAT_NUMBER
        defaultSeatShouldBeFound("seatNumber.in=" + DEFAULT_SEAT_NUMBER + "," + UPDATED_SEAT_NUMBER);

        // Get all the seatList where seatNumber equals to UPDATED_SEAT_NUMBER
        defaultSeatShouldNotBeFound("seatNumber.in=" + UPDATED_SEAT_NUMBER);
    }

    @Test
    @Transactional
    void getAllSeatsBySeatNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where seatNumber is not null
        defaultSeatShouldBeFound("seatNumber.specified=true");

        // Get all the seatList where seatNumber is null
        defaultSeatShouldNotBeFound("seatNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllSeatsBySeatNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where seatNumber is greater than or equal to DEFAULT_SEAT_NUMBER
        defaultSeatShouldBeFound("seatNumber.greaterThanOrEqual=" + DEFAULT_SEAT_NUMBER);

        // Get all the seatList where seatNumber is greater than or equal to UPDATED_SEAT_NUMBER
        defaultSeatShouldNotBeFound("seatNumber.greaterThanOrEqual=" + UPDATED_SEAT_NUMBER);
    }

    @Test
    @Transactional
    void getAllSeatsBySeatNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where seatNumber is less than or equal to DEFAULT_SEAT_NUMBER
        defaultSeatShouldBeFound("seatNumber.lessThanOrEqual=" + DEFAULT_SEAT_NUMBER);

        // Get all the seatList where seatNumber is less than or equal to SMALLER_SEAT_NUMBER
        defaultSeatShouldNotBeFound("seatNumber.lessThanOrEqual=" + SMALLER_SEAT_NUMBER);
    }

    @Test
    @Transactional
    void getAllSeatsBySeatNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where seatNumber is less than DEFAULT_SEAT_NUMBER
        defaultSeatShouldNotBeFound("seatNumber.lessThan=" + DEFAULT_SEAT_NUMBER);

        // Get all the seatList where seatNumber is less than UPDATED_SEAT_NUMBER
        defaultSeatShouldBeFound("seatNumber.lessThan=" + UPDATED_SEAT_NUMBER);
    }

    @Test
    @Transactional
    void getAllSeatsBySeatNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where seatNumber is greater than DEFAULT_SEAT_NUMBER
        defaultSeatShouldNotBeFound("seatNumber.greaterThan=" + DEFAULT_SEAT_NUMBER);

        // Get all the seatList where seatNumber is greater than SMALLER_SEAT_NUMBER
        defaultSeatShouldBeFound("seatNumber.greaterThan=" + SMALLER_SEAT_NUMBER);
    }

    @Test
    @Transactional
    void getAllSeatsBySeatDescrIsEqualToSomething() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where seatDescr equals to DEFAULT_SEAT_DESCR
        defaultSeatShouldBeFound("seatDescr.equals=" + DEFAULT_SEAT_DESCR);

        // Get all the seatList where seatDescr equals to UPDATED_SEAT_DESCR
        defaultSeatShouldNotBeFound("seatDescr.equals=" + UPDATED_SEAT_DESCR);
    }

    @Test
    @Transactional
    void getAllSeatsBySeatDescrIsInShouldWork() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where seatDescr in DEFAULT_SEAT_DESCR or UPDATED_SEAT_DESCR
        defaultSeatShouldBeFound("seatDescr.in=" + DEFAULT_SEAT_DESCR + "," + UPDATED_SEAT_DESCR);

        // Get all the seatList where seatDescr equals to UPDATED_SEAT_DESCR
        defaultSeatShouldNotBeFound("seatDescr.in=" + UPDATED_SEAT_DESCR);
    }

    @Test
    @Transactional
    void getAllSeatsBySeatDescrIsNullOrNotNull() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where seatDescr is not null
        defaultSeatShouldBeFound("seatDescr.specified=true");

        // Get all the seatList where seatDescr is null
        defaultSeatShouldNotBeFound("seatDescr.specified=false");
    }

    @Test
    @Transactional
    void getAllSeatsBySeatDescrContainsSomething() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where seatDescr contains DEFAULT_SEAT_DESCR
        defaultSeatShouldBeFound("seatDescr.contains=" + DEFAULT_SEAT_DESCR);

        // Get all the seatList where seatDescr contains UPDATED_SEAT_DESCR
        defaultSeatShouldNotBeFound("seatDescr.contains=" + UPDATED_SEAT_DESCR);
    }

    @Test
    @Transactional
    void getAllSeatsBySeatDescrNotContainsSomething() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where seatDescr does not contain DEFAULT_SEAT_DESCR
        defaultSeatShouldNotBeFound("seatDescr.doesNotContain=" + DEFAULT_SEAT_DESCR);

        // Get all the seatList where seatDescr does not contain UPDATED_SEAT_DESCR
        defaultSeatShouldBeFound("seatDescr.doesNotContain=" + UPDATED_SEAT_DESCR);
    }

    @Test
    @Transactional
    void getAllSeatsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where type equals to DEFAULT_TYPE
        defaultSeatShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the seatList where type equals to UPDATED_TYPE
        defaultSeatShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSeatsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultSeatShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the seatList where type equals to UPDATED_TYPE
        defaultSeatShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSeatsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where type is not null
        defaultSeatShouldBeFound("type.specified=true");

        // Get all the seatList where type is null
        defaultSeatShouldNotBeFound("type.specified=false");
    }

    @Test
    @Transactional
    void getAllSeatsByTypeContainsSomething() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where type contains DEFAULT_TYPE
        defaultSeatShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the seatList where type contains UPDATED_TYPE
        defaultSeatShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSeatsByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        // Get all the seatList where type does not contain DEFAULT_TYPE
        defaultSeatShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the seatList where type does not contain UPDATED_TYPE
        defaultSeatShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllSeatsByScreenIsEqualToSomething() throws Exception {
        Screen screen;
        if (TestUtil.findAll(em, Screen.class).isEmpty()) {
            seatRepository.saveAndFlush(seat);
            screen = ScreenResourceIT.createEntity(em);
        } else {
            screen = TestUtil.findAll(em, Screen.class).get(0);
        }
        em.persist(screen);
        em.flush();
        seat.setScreen(screen);
        seatRepository.saveAndFlush(seat);
        Long screenId = screen.getScreenId();

        // Get all the seatList where screen equals to screenId
        defaultSeatShouldBeFound("screenId.equals=" + screenId);

        // Get all the seatList where screen equals to (screenId + 1)
        defaultSeatShouldNotBeFound("screenId.equals=" + (screenId + 1));
    }

    @Test
    @Transactional
    void getAllSeatsByShowIsEqualToSomething() throws Exception {
        Show show;
        if (TestUtil.findAll(em, Show.class).isEmpty()) {
            seatRepository.saveAndFlush(seat);
            show = ShowResourceIT.createEntity(em);
        } else {
            show = TestUtil.findAll(em, Show.class).get(0);
        }
        em.persist(show);
        em.flush();
        seat.addShow(show);
        seatRepository.saveAndFlush(seat);
        Long showId = show.getShowId();

        // Get all the seatList where show equals to showId
        defaultSeatShouldBeFound("showId.equals=" + showId);

        // Get all the seatList where show equals to (showId + 1)
        defaultSeatShouldNotBeFound("showId.equals=" + (showId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSeatShouldBeFound(String filter) throws Exception {
        restSeatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=seatId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].seatId").value(hasItem(seat.getSeatId().intValue())))
            .andExpect(jsonPath("$.[*].seatNumber").value(hasItem(DEFAULT_SEAT_NUMBER)))
            .andExpect(jsonPath("$.[*].seatDescr").value(hasItem(DEFAULT_SEAT_DESCR)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));

        // Check, that the count call also returns 1
        restSeatMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=seatId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSeatShouldNotBeFound(String filter) throws Exception {
        restSeatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=seatId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSeatMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=seatId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSeat() throws Exception {
        // Get the seat
        restSeatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSeat() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        int databaseSizeBeforeUpdate = seatRepository.findAll().size();

        // Update the seat
        Seat updatedSeat = seatRepository.findById(seat.getSeatId()).get();
        // Disconnect from session so that the updates on updatedSeat are not directly saved in db
        em.detach(updatedSeat);
        updatedSeat.seatNumber(UPDATED_SEAT_NUMBER).seatDescr(UPDATED_SEAT_DESCR).type(UPDATED_TYPE);
        SeatDTO seatDTO = seatMapper.toDto(updatedSeat);

        restSeatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, seatDTO.getSeatId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seatDTO))
            )
            .andExpect(status().isOk());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
        Seat testSeat = seatList.get(seatList.size() - 1);
        assertThat(testSeat.getSeatNumber()).isEqualTo(UPDATED_SEAT_NUMBER);
        assertThat(testSeat.getSeatDescr()).isEqualTo(UPDATED_SEAT_DESCR);
        assertThat(testSeat.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingSeat() throws Exception {
        int databaseSizeBeforeUpdate = seatRepository.findAll().size();
        seat.setSeatId(count.incrementAndGet());

        // Create the Seat
        SeatDTO seatDTO = seatMapper.toDto(seat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, seatDTO.getSeatId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSeat() throws Exception {
        int databaseSizeBeforeUpdate = seatRepository.findAll().size();
        seat.setSeatId(count.incrementAndGet());

        // Create the Seat
        SeatDTO seatDTO = seatMapper.toDto(seat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(seatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSeat() throws Exception {
        int databaseSizeBeforeUpdate = seatRepository.findAll().size();
        seat.setSeatId(count.incrementAndGet());

        // Create the Seat
        SeatDTO seatDTO = seatMapper.toDto(seat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(seatDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSeatWithPatch() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        int databaseSizeBeforeUpdate = seatRepository.findAll().size();

        // Update the seat using partial update
        Seat partialUpdatedSeat = new Seat();
        partialUpdatedSeat.setSeatId(seat.getSeatId());

        partialUpdatedSeat.seatNumber(UPDATED_SEAT_NUMBER).seatDescr(UPDATED_SEAT_DESCR).type(UPDATED_TYPE);

        restSeatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeat.getSeatId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeat))
            )
            .andExpect(status().isOk());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
        Seat testSeat = seatList.get(seatList.size() - 1);
        assertThat(testSeat.getSeatNumber()).isEqualTo(UPDATED_SEAT_NUMBER);
        assertThat(testSeat.getSeatDescr()).isEqualTo(UPDATED_SEAT_DESCR);
        assertThat(testSeat.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateSeatWithPatch() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        int databaseSizeBeforeUpdate = seatRepository.findAll().size();

        // Update the seat using partial update
        Seat partialUpdatedSeat = new Seat();
        partialUpdatedSeat.setSeatId(seat.getSeatId());

        partialUpdatedSeat.seatNumber(UPDATED_SEAT_NUMBER).seatDescr(UPDATED_SEAT_DESCR).type(UPDATED_TYPE);

        restSeatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSeat.getSeatId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSeat))
            )
            .andExpect(status().isOk());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
        Seat testSeat = seatList.get(seatList.size() - 1);
        assertThat(testSeat.getSeatNumber()).isEqualTo(UPDATED_SEAT_NUMBER);
        assertThat(testSeat.getSeatDescr()).isEqualTo(UPDATED_SEAT_DESCR);
        assertThat(testSeat.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingSeat() throws Exception {
        int databaseSizeBeforeUpdate = seatRepository.findAll().size();
        seat.setSeatId(count.incrementAndGet());

        // Create the Seat
        SeatDTO seatDTO = seatMapper.toDto(seat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSeatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, seatDTO.getSeatId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSeat() throws Exception {
        int databaseSizeBeforeUpdate = seatRepository.findAll().size();
        seat.setSeatId(count.incrementAndGet());

        // Create the Seat
        SeatDTO seatDTO = seatMapper.toDto(seat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(seatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSeat() throws Exception {
        int databaseSizeBeforeUpdate = seatRepository.findAll().size();
        seat.setSeatId(count.incrementAndGet());

        // Create the Seat
        SeatDTO seatDTO = seatMapper.toDto(seat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSeatMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(seatDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Seat in the database
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSeat() throws Exception {
        // Initialize the database
        seatRepository.saveAndFlush(seat);

        int databaseSizeBeforeDelete = seatRepository.findAll().size();

        // Delete the seat
        restSeatMockMvc
            .perform(delete(ENTITY_API_URL_ID, seat.getSeatId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Seat> seatList = seatRepository.findAll();
        assertThat(seatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
