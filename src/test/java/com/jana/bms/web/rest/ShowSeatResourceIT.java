package com.jana.bms.web.rest;

import static com.jana.bms.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jana.bms.IntegrationTest;
import com.jana.bms.domain.Booking;
import com.jana.bms.domain.Seat;
import com.jana.bms.domain.Show;
import com.jana.bms.domain.ShowSeat;
import com.jana.bms.domain.enumeration.SeatStatus;
import com.jana.bms.repository.ShowSeatRepository;
import com.jana.bms.service.criteria.ShowSeatCriteria;
import com.jana.bms.service.dto.ShowSeatDTO;
import com.jana.bms.service.mapper.ShowSeatMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link ShowSeatResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ShowSeatResourceIT {

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRICE = new BigDecimal(1 - 1);

    private static final SeatStatus DEFAULT_STATUS = SeatStatus.AVAIALBLE;
    private static final SeatStatus UPDATED_STATUS = SeatStatus.FULL;

    private static final String ENTITY_API_URL = "/api/show-seats";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{showSeatId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ShowSeatRepository showSeatRepository;

    @Autowired
    private ShowSeatMapper showSeatMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShowSeatMockMvc;

    private ShowSeat showSeat;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShowSeat createEntity(EntityManager em) {
        ShowSeat showSeat = new ShowSeat().price(DEFAULT_PRICE).status(DEFAULT_STATUS);
        // Add required entity
        Seat seat;
        if (TestUtil.findAll(em, Seat.class).isEmpty()) {
            seat = SeatResourceIT.createEntity(em);
            em.persist(seat);
            em.flush();
        } else {
            seat = TestUtil.findAll(em, Seat.class).get(0);
        }
        showSeat.setSeat(seat);
        // Add required entity
        Show show;
        if (TestUtil.findAll(em, Show.class).isEmpty()) {
            show = ShowResourceIT.createEntity(em);
            em.persist(show);
            em.flush();
        } else {
            show = TestUtil.findAll(em, Show.class).get(0);
        }
        showSeat.setShow(show);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        showSeat.setBooking(booking);
        return showSeat;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShowSeat createUpdatedEntity(EntityManager em) {
        ShowSeat showSeat = new ShowSeat().price(UPDATED_PRICE).status(UPDATED_STATUS);
        // Add required entity
        Seat seat;
        if (TestUtil.findAll(em, Seat.class).isEmpty()) {
            seat = SeatResourceIT.createUpdatedEntity(em);
            em.persist(seat);
            em.flush();
        } else {
            seat = TestUtil.findAll(em, Seat.class).get(0);
        }
        showSeat.setSeat(seat);
        // Add required entity
        Show show;
        if (TestUtil.findAll(em, Show.class).isEmpty()) {
            show = ShowResourceIT.createUpdatedEntity(em);
            em.persist(show);
            em.flush();
        } else {
            show = TestUtil.findAll(em, Show.class).get(0);
        }
        showSeat.setShow(show);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createUpdatedEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        showSeat.setBooking(booking);
        return showSeat;
    }

    @BeforeEach
    public void initTest() {
        showSeat = createEntity(em);
    }

    @Test
    @Transactional
    void createShowSeat() throws Exception {
        int databaseSizeBeforeCreate = showSeatRepository.findAll().size();
        // Create the ShowSeat
        ShowSeatDTO showSeatDTO = showSeatMapper.toDto(showSeat);
        restShowSeatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(showSeatDTO)))
            .andExpect(status().isCreated());

        // Validate the ShowSeat in the database
        List<ShowSeat> showSeatList = showSeatRepository.findAll();
        assertThat(showSeatList).hasSize(databaseSizeBeforeCreate + 1);
        ShowSeat testShowSeat = showSeatList.get(showSeatList.size() - 1);
        assertThat(testShowSeat.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testShowSeat.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createShowSeatWithExistingId() throws Exception {
        // Create the ShowSeat with an existing ID
        showSeat.setShowSeatId(1L);
        ShowSeatDTO showSeatDTO = showSeatMapper.toDto(showSeat);

        int databaseSizeBeforeCreate = showSeatRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restShowSeatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(showSeatDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShowSeat in the database
        List<ShowSeat> showSeatList = showSeatRepository.findAll();
        assertThat(showSeatList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = showSeatRepository.findAll().size();
        // set the field null
        showSeat.setPrice(null);

        // Create the ShowSeat, which fails.
        ShowSeatDTO showSeatDTO = showSeatMapper.toDto(showSeat);

        restShowSeatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(showSeatDTO)))
            .andExpect(status().isBadRequest());

        List<ShowSeat> showSeatList = showSeatRepository.findAll();
        assertThat(showSeatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = showSeatRepository.findAll().size();
        // set the field null
        showSeat.setStatus(null);

        // Create the ShowSeat, which fails.
        ShowSeatDTO showSeatDTO = showSeatMapper.toDto(showSeat);

        restShowSeatMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(showSeatDTO)))
            .andExpect(status().isBadRequest());

        List<ShowSeat> showSeatList = showSeatRepository.findAll();
        assertThat(showSeatList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllShowSeats() throws Exception {
        // Initialize the database
        showSeatRepository.saveAndFlush(showSeat);

        // Get all the showSeatList
        restShowSeatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=showSeatId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].showSeatId").value(hasItem(showSeat.getShowSeatId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getShowSeat() throws Exception {
        // Initialize the database
        showSeatRepository.saveAndFlush(showSeat);

        // Get the showSeat
        restShowSeatMockMvc
            .perform(get(ENTITY_API_URL_ID, showSeat.getShowSeatId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.showSeatId").value(showSeat.getShowSeatId().intValue()))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getShowSeatsByIdFiltering() throws Exception {
        // Initialize the database
        showSeatRepository.saveAndFlush(showSeat);

        Long id = showSeat.getShowSeatId();

        defaultShowSeatShouldBeFound("showSeatId.equals=" + id);
        defaultShowSeatShouldNotBeFound("showSeatId.notEquals=" + id);

        defaultShowSeatShouldBeFound("showSeatId.greaterThanOrEqual=" + id);
        defaultShowSeatShouldNotBeFound("showSeatId.greaterThan=" + id);

        defaultShowSeatShouldBeFound("showSeatId.lessThanOrEqual=" + id);
        defaultShowSeatShouldNotBeFound("showSeatId.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllShowSeatsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        showSeatRepository.saveAndFlush(showSeat);

        // Get all the showSeatList where price equals to DEFAULT_PRICE
        defaultShowSeatShouldBeFound("price.equals=" + DEFAULT_PRICE);

        // Get all the showSeatList where price equals to UPDATED_PRICE
        defaultShowSeatShouldNotBeFound("price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllShowSeatsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        showSeatRepository.saveAndFlush(showSeat);

        // Get all the showSeatList where price in DEFAULT_PRICE or UPDATED_PRICE
        defaultShowSeatShouldBeFound("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE);

        // Get all the showSeatList where price equals to UPDATED_PRICE
        defaultShowSeatShouldNotBeFound("price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllShowSeatsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        showSeatRepository.saveAndFlush(showSeat);

        // Get all the showSeatList where price is not null
        defaultShowSeatShouldBeFound("price.specified=true");

        // Get all the showSeatList where price is null
        defaultShowSeatShouldNotBeFound("price.specified=false");
    }

    @Test
    @Transactional
    void getAllShowSeatsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        showSeatRepository.saveAndFlush(showSeat);

        // Get all the showSeatList where price is greater than or equal to DEFAULT_PRICE
        defaultShowSeatShouldBeFound("price.greaterThanOrEqual=" + DEFAULT_PRICE);

        // Get all the showSeatList where price is greater than or equal to UPDATED_PRICE
        defaultShowSeatShouldNotBeFound("price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllShowSeatsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        showSeatRepository.saveAndFlush(showSeat);

        // Get all the showSeatList where price is less than or equal to DEFAULT_PRICE
        defaultShowSeatShouldBeFound("price.lessThanOrEqual=" + DEFAULT_PRICE);

        // Get all the showSeatList where price is less than or equal to SMALLER_PRICE
        defaultShowSeatShouldNotBeFound("price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllShowSeatsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        showSeatRepository.saveAndFlush(showSeat);

        // Get all the showSeatList where price is less than DEFAULT_PRICE
        defaultShowSeatShouldNotBeFound("price.lessThan=" + DEFAULT_PRICE);

        // Get all the showSeatList where price is less than UPDATED_PRICE
        defaultShowSeatShouldBeFound("price.lessThan=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllShowSeatsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        showSeatRepository.saveAndFlush(showSeat);

        // Get all the showSeatList where price is greater than DEFAULT_PRICE
        defaultShowSeatShouldNotBeFound("price.greaterThan=" + DEFAULT_PRICE);

        // Get all the showSeatList where price is greater than SMALLER_PRICE
        defaultShowSeatShouldBeFound("price.greaterThan=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllShowSeatsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        showSeatRepository.saveAndFlush(showSeat);

        // Get all the showSeatList where status equals to DEFAULT_STATUS
        defaultShowSeatShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the showSeatList where status equals to UPDATED_STATUS
        defaultShowSeatShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllShowSeatsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        showSeatRepository.saveAndFlush(showSeat);

        // Get all the showSeatList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultShowSeatShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the showSeatList where status equals to UPDATED_STATUS
        defaultShowSeatShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllShowSeatsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        showSeatRepository.saveAndFlush(showSeat);

        // Get all the showSeatList where status is not null
        defaultShowSeatShouldBeFound("status.specified=true");

        // Get all the showSeatList where status is null
        defaultShowSeatShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllShowSeatsBySeatIsEqualToSomething() throws Exception {
        Seat seat;
        if (TestUtil.findAll(em, Seat.class).isEmpty()) {
            showSeatRepository.saveAndFlush(showSeat);
            seat = SeatResourceIT.createEntity(em);
        } else {
            seat = TestUtil.findAll(em, Seat.class).get(0);
        }
        em.persist(seat);
        em.flush();
        showSeat.setSeat(seat);
        showSeatRepository.saveAndFlush(showSeat);
        Long seatId = seat.getSeatId();

        // Get all the showSeatList where seat equals to seatId
        defaultShowSeatShouldBeFound("seatId.equals=" + seatId);

        // Get all the showSeatList where seat equals to (seatId + 1)
        defaultShowSeatShouldNotBeFound("seatId.equals=" + (seatId + 1));
    }

    @Test
    @Transactional
    void getAllShowSeatsByShowIsEqualToSomething() throws Exception {
        Show show;
        if (TestUtil.findAll(em, Show.class).isEmpty()) {
            showSeatRepository.saveAndFlush(showSeat);
            show = ShowResourceIT.createEntity(em);
        } else {
            show = TestUtil.findAll(em, Show.class).get(0);
        }
        em.persist(show);
        em.flush();
        showSeat.setShow(show);
        showSeatRepository.saveAndFlush(showSeat);
        Long showId = show.getShowId();

        // Get all the showSeatList where show equals to showId
        defaultShowSeatShouldBeFound("showId.equals=" + showId);

        // Get all the showSeatList where show equals to (showId + 1)
        defaultShowSeatShouldNotBeFound("showId.equals=" + (showId + 1));
    }

    @Test
    @Transactional
    void getAllShowSeatsByBookingIsEqualToSomething() throws Exception {
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            showSeatRepository.saveAndFlush(showSeat);
            booking = BookingResourceIT.createEntity(em);
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        em.persist(booking);
        em.flush();
        showSeat.setBooking(booking);
        showSeatRepository.saveAndFlush(showSeat);
        Long bookingId = booking.getBookingId();

        // Get all the showSeatList where booking equals to bookingId
        defaultShowSeatShouldBeFound("bookingId.equals=" + bookingId);

        // Get all the showSeatList where booking equals to (bookingId + 1)
        defaultShowSeatShouldNotBeFound("bookingId.equals=" + (bookingId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShowSeatShouldBeFound(String filter) throws Exception {
        restShowSeatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=showSeatId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].showSeatId").value(hasItem(showSeat.getShowSeatId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restShowSeatMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=showSeatId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShowSeatShouldNotBeFound(String filter) throws Exception {
        restShowSeatMockMvc
            .perform(get(ENTITY_API_URL + "?sort=showSeatId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShowSeatMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=showSeatId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingShowSeat() throws Exception {
        // Get the showSeat
        restShowSeatMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewShowSeat() throws Exception {
        // Initialize the database
        showSeatRepository.saveAndFlush(showSeat);

        int databaseSizeBeforeUpdate = showSeatRepository.findAll().size();

        // Update the showSeat
        ShowSeat updatedShowSeat = showSeatRepository.findById(showSeat.getShowSeatId()).get();
        // Disconnect from session so that the updates on updatedShowSeat are not directly saved in db
        em.detach(updatedShowSeat);
        updatedShowSeat.price(UPDATED_PRICE).status(UPDATED_STATUS);
        ShowSeatDTO showSeatDTO = showSeatMapper.toDto(updatedShowSeat);

        restShowSeatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, showSeatDTO.getShowSeatId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(showSeatDTO))
            )
            .andExpect(status().isOk());

        // Validate the ShowSeat in the database
        List<ShowSeat> showSeatList = showSeatRepository.findAll();
        assertThat(showSeatList).hasSize(databaseSizeBeforeUpdate);
        ShowSeat testShowSeat = showSeatList.get(showSeatList.size() - 1);
        assertThat(testShowSeat.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testShowSeat.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingShowSeat() throws Exception {
        int databaseSizeBeforeUpdate = showSeatRepository.findAll().size();
        showSeat.setShowSeatId(count.incrementAndGet());

        // Create the ShowSeat
        ShowSeatDTO showSeatDTO = showSeatMapper.toDto(showSeat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShowSeatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, showSeatDTO.getShowSeatId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(showSeatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShowSeat in the database
        List<ShowSeat> showSeatList = showSeatRepository.findAll();
        assertThat(showSeatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchShowSeat() throws Exception {
        int databaseSizeBeforeUpdate = showSeatRepository.findAll().size();
        showSeat.setShowSeatId(count.incrementAndGet());

        // Create the ShowSeat
        ShowSeatDTO showSeatDTO = showSeatMapper.toDto(showSeat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShowSeatMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(showSeatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShowSeat in the database
        List<ShowSeat> showSeatList = showSeatRepository.findAll();
        assertThat(showSeatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamShowSeat() throws Exception {
        int databaseSizeBeforeUpdate = showSeatRepository.findAll().size();
        showSeat.setShowSeatId(count.incrementAndGet());

        // Create the ShowSeat
        ShowSeatDTO showSeatDTO = showSeatMapper.toDto(showSeat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShowSeatMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(showSeatDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShowSeat in the database
        List<ShowSeat> showSeatList = showSeatRepository.findAll();
        assertThat(showSeatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateShowSeatWithPatch() throws Exception {
        // Initialize the database
        showSeatRepository.saveAndFlush(showSeat);

        int databaseSizeBeforeUpdate = showSeatRepository.findAll().size();

        // Update the showSeat using partial update
        ShowSeat partialUpdatedShowSeat = new ShowSeat();
        partialUpdatedShowSeat.setShowSeatId(showSeat.getShowSeatId());

        partialUpdatedShowSeat.status(UPDATED_STATUS);

        restShowSeatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShowSeat.getShowSeatId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShowSeat))
            )
            .andExpect(status().isOk());

        // Validate the ShowSeat in the database
        List<ShowSeat> showSeatList = showSeatRepository.findAll();
        assertThat(showSeatList).hasSize(databaseSizeBeforeUpdate);
        ShowSeat testShowSeat = showSeatList.get(showSeatList.size() - 1);
        assertThat(testShowSeat.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testShowSeat.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateShowSeatWithPatch() throws Exception {
        // Initialize the database
        showSeatRepository.saveAndFlush(showSeat);

        int databaseSizeBeforeUpdate = showSeatRepository.findAll().size();

        // Update the showSeat using partial update
        ShowSeat partialUpdatedShowSeat = new ShowSeat();
        partialUpdatedShowSeat.setShowSeatId(showSeat.getShowSeatId());

        partialUpdatedShowSeat.price(UPDATED_PRICE).status(UPDATED_STATUS);

        restShowSeatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedShowSeat.getShowSeatId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedShowSeat))
            )
            .andExpect(status().isOk());

        // Validate the ShowSeat in the database
        List<ShowSeat> showSeatList = showSeatRepository.findAll();
        assertThat(showSeatList).hasSize(databaseSizeBeforeUpdate);
        ShowSeat testShowSeat = showSeatList.get(showSeatList.size() - 1);
        assertThat(testShowSeat.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testShowSeat.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingShowSeat() throws Exception {
        int databaseSizeBeforeUpdate = showSeatRepository.findAll().size();
        showSeat.setShowSeatId(count.incrementAndGet());

        // Create the ShowSeat
        ShowSeatDTO showSeatDTO = showSeatMapper.toDto(showSeat);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShowSeatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, showSeatDTO.getShowSeatId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(showSeatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShowSeat in the database
        List<ShowSeat> showSeatList = showSeatRepository.findAll();
        assertThat(showSeatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchShowSeat() throws Exception {
        int databaseSizeBeforeUpdate = showSeatRepository.findAll().size();
        showSeat.setShowSeatId(count.incrementAndGet());

        // Create the ShowSeat
        ShowSeatDTO showSeatDTO = showSeatMapper.toDto(showSeat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShowSeatMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(showSeatDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ShowSeat in the database
        List<ShowSeat> showSeatList = showSeatRepository.findAll();
        assertThat(showSeatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamShowSeat() throws Exception {
        int databaseSizeBeforeUpdate = showSeatRepository.findAll().size();
        showSeat.setShowSeatId(count.incrementAndGet());

        // Create the ShowSeat
        ShowSeatDTO showSeatDTO = showSeatMapper.toDto(showSeat);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restShowSeatMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(showSeatDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ShowSeat in the database
        List<ShowSeat> showSeatList = showSeatRepository.findAll();
        assertThat(showSeatList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteShowSeat() throws Exception {
        // Initialize the database
        showSeatRepository.saveAndFlush(showSeat);

        int databaseSizeBeforeDelete = showSeatRepository.findAll().size();

        // Delete the showSeat
        restShowSeatMockMvc
            .perform(delete(ENTITY_API_URL_ID, showSeat.getShowSeatId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShowSeat> showSeatList = showSeatRepository.findAll();
        assertThat(showSeatList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
