package com.jana.bms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jana.bms.IntegrationTest;
import com.jana.bms.domain.Booking;
import com.jana.bms.domain.Customer;
import com.jana.bms.domain.Show;
import com.jana.bms.domain.enumeration.BookingStatus;
import com.jana.bms.repository.BookingRepository;
import com.jana.bms.service.criteria.BookingCriteria;
import com.jana.bms.service.dto.BookingDTO;
import com.jana.bms.service.mapper.BookingMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link BookingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BookingResourceIT {

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BookingStatus DEFAULT_STATUS = BookingStatus.RESERVED;
    private static final BookingStatus UPDATED_STATUS = BookingStatus.BOOKED;

    private static final String ENTITY_API_URL = "/api/bookings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{bookingId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingMapper bookingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookingMockMvc;

    private Booking booking;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Booking createEntity(EntityManager em) {
        Booking booking = new Booking().timestamp(DEFAULT_TIMESTAMP).status(DEFAULT_STATUS);
        // Add required entity
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customer = CustomerResourceIT.createEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        booking.setCustomer(customer);
        // Add required entity
        Show show;
        if (TestUtil.findAll(em, Show.class).isEmpty()) {
            show = ShowResourceIT.createEntity(em);
            em.persist(show);
            em.flush();
        } else {
            show = TestUtil.findAll(em, Show.class).get(0);
        }
        booking.setShow(show);
        return booking;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Booking createUpdatedEntity(EntityManager em) {
        Booking booking = new Booking().timestamp(UPDATED_TIMESTAMP).status(UPDATED_STATUS);
        // Add required entity
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            customer = CustomerResourceIT.createUpdatedEntity(em);
            em.persist(customer);
            em.flush();
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        booking.setCustomer(customer);
        // Add required entity
        Show show;
        if (TestUtil.findAll(em, Show.class).isEmpty()) {
            show = ShowResourceIT.createUpdatedEntity(em);
            em.persist(show);
            em.flush();
        } else {
            show = TestUtil.findAll(em, Show.class).get(0);
        }
        booking.setShow(show);
        return booking;
    }

    @BeforeEach
    public void initTest() {
        booking = createEntity(em);
    }

    @Test
    @Transactional
    void createBooking() throws Exception {
        int databaseSizeBeforeCreate = bookingRepository.findAll().size();
        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);
        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isCreated());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeCreate + 1);
        Booking testBooking = bookingList.get(bookingList.size() - 1);
        assertThat(testBooking.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testBooking.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createBookingWithExistingId() throws Exception {
        // Create the Booking with an existing ID
        booking.setBookingId(1L);
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        int databaseSizeBeforeCreate = bookingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setTimestamp(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = bookingRepository.findAll().size();
        // set the field null
        booking.setStatus(null);

        // Create the Booking, which fails.
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        restBookingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isBadRequest());

        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBookings() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList
        restBookingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=bookingId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].bookingId").value(hasItem(booking.getBookingId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get the booking
        restBookingMockMvc
            .perform(get(ENTITY_API_URL_ID, booking.getBookingId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.bookingId").value(booking.getBookingId().intValue()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getBookingsByIdFiltering() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        Long id = booking.getBookingId();

        defaultBookingShouldBeFound("bookingId.equals=" + id);
        defaultBookingShouldNotBeFound("bookingId.notEquals=" + id);

        defaultBookingShouldBeFound("bookingId.greaterThanOrEqual=" + id);
        defaultBookingShouldNotBeFound("bookingId.greaterThan=" + id);

        defaultBookingShouldBeFound("bookingId.lessThanOrEqual=" + id);
        defaultBookingShouldNotBeFound("bookingId.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBookingsByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where timestamp equals to DEFAULT_TIMESTAMP
        defaultBookingShouldBeFound("timestamp.equals=" + DEFAULT_TIMESTAMP);

        // Get all the bookingList where timestamp equals to UPDATED_TIMESTAMP
        defaultBookingShouldNotBeFound("timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    void getAllBookingsByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where timestamp in DEFAULT_TIMESTAMP or UPDATED_TIMESTAMP
        defaultBookingShouldBeFound("timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP);

        // Get all the bookingList where timestamp equals to UPDATED_TIMESTAMP
        defaultBookingShouldNotBeFound("timestamp.in=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    void getAllBookingsByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where timestamp is not null
        defaultBookingShouldBeFound("timestamp.specified=true");

        // Get all the bookingList where timestamp is null
        defaultBookingShouldNotBeFound("timestamp.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where status equals to DEFAULT_STATUS
        defaultBookingShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the bookingList where status equals to UPDATED_STATUS
        defaultBookingShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBookingsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultBookingShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the bookingList where status equals to UPDATED_STATUS
        defaultBookingShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBookingsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        // Get all the bookingList where status is not null
        defaultBookingShouldBeFound("status.specified=true");

        // Get all the bookingList where status is null
        defaultBookingShouldNotBeFound("status.specified=false");
    }

    @Test
    @Transactional
    void getAllBookingsByCustomerIsEqualToSomething() throws Exception {
        Customer customer;
        if (TestUtil.findAll(em, Customer.class).isEmpty()) {
            bookingRepository.saveAndFlush(booking);
            customer = CustomerResourceIT.createEntity(em);
        } else {
            customer = TestUtil.findAll(em, Customer.class).get(0);
        }
        em.persist(customer);
        em.flush();
        booking.setCustomer(customer);
        bookingRepository.saveAndFlush(booking);
        Long customerId = customer.getCustomerId();

        // Get all the bookingList where customer equals to customerId
        defaultBookingShouldBeFound("customerId.equals=" + customerId);

        // Get all the bookingList where customer equals to (customerId + 1)
        defaultBookingShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllBookingsByShowIsEqualToSomething() throws Exception {
        Show show;
        if (TestUtil.findAll(em, Show.class).isEmpty()) {
            bookingRepository.saveAndFlush(booking);
            show = ShowResourceIT.createEntity(em);
        } else {
            show = TestUtil.findAll(em, Show.class).get(0);
        }
        em.persist(show);
        em.flush();
        booking.setShow(show);
        bookingRepository.saveAndFlush(booking);
        Long showId = show.getShowId();

        // Get all the bookingList where show equals to showId
        defaultBookingShouldBeFound("showId.equals=" + showId);

        // Get all the bookingList where show equals to (showId + 1)
        defaultBookingShouldNotBeFound("showId.equals=" + (showId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBookingShouldBeFound(String filter) throws Exception {
        restBookingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=bookingId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].bookingId").value(hasItem(booking.getBookingId().intValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));

        // Check, that the count call also returns 1
        restBookingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=bookingId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBookingShouldNotBeFound(String filter) throws Exception {
        restBookingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=bookingId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBookingMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=bookingId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBooking() throws Exception {
        // Get the booking
        restBookingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

        // Update the booking
        Booking updatedBooking = bookingRepository.findById(booking.getBookingId()).get();
        // Disconnect from session so that the updates on updatedBooking are not directly saved in db
        em.detach(updatedBooking);
        updatedBooking.timestamp(UPDATED_TIMESTAMP).status(UPDATED_STATUS);
        BookingDTO bookingDTO = bookingMapper.toDto(updatedBooking);

        restBookingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookingDTO.getBookingId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookingDTO))
            )
            .andExpect(status().isOk());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
        Booking testBooking = bookingList.get(bookingList.size() - 1);
        assertThat(testBooking.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testBooking.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingBooking() throws Exception {
        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();
        booking.setBookingId(count.incrementAndGet());

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookingDTO.getBookingId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBooking() throws Exception {
        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();
        booking.setBookingId(count.incrementAndGet());

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBooking() throws Exception {
        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();
        booking.setBookingId(count.incrementAndGet());

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookingWithPatch() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

        // Update the booking using partial update
        Booking partialUpdatedBooking = new Booking();
        partialUpdatedBooking.setBookingId(booking.getBookingId());

        partialUpdatedBooking.status(UPDATED_STATUS);

        restBookingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBooking.getBookingId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBooking))
            )
            .andExpect(status().isOk());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
        Booking testBooking = bookingList.get(bookingList.size() - 1);
        assertThat(testBooking.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testBooking.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateBookingWithPatch() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();

        // Update the booking using partial update
        Booking partialUpdatedBooking = new Booking();
        partialUpdatedBooking.setBookingId(booking.getBookingId());

        partialUpdatedBooking.timestamp(UPDATED_TIMESTAMP).status(UPDATED_STATUS);

        restBookingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBooking.getBookingId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBooking))
            )
            .andExpect(status().isOk());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
        Booking testBooking = bookingList.get(bookingList.size() - 1);
        assertThat(testBooking.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testBooking.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingBooking() throws Exception {
        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();
        booking.setBookingId(count.incrementAndGet());

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookingDTO.getBookingId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBooking() throws Exception {
        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();
        booking.setBookingId(count.incrementAndGet());

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBooking() throws Exception {
        int databaseSizeBeforeUpdate = bookingRepository.findAll().size();
        booking.setBookingId(count.incrementAndGet());

        // Create the Booking
        BookingDTO bookingDTO = bookingMapper.toDto(booking);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookingMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bookingDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Booking in the database
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBooking() throws Exception {
        // Initialize the database
        bookingRepository.saveAndFlush(booking);

        int databaseSizeBeforeDelete = bookingRepository.findAll().size();

        // Delete the booking
        restBookingMockMvc
            .perform(delete(ENTITY_API_URL_ID, booking.getBookingId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Booking> bookingList = bookingRepository.findAll();
        assertThat(bookingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
