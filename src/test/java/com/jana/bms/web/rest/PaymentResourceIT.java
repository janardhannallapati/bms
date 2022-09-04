package com.jana.bms.web.rest;

import static com.jana.bms.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jana.bms.IntegrationTest;
import com.jana.bms.domain.Booking;
import com.jana.bms.domain.Payment;
import com.jana.bms.repository.PaymentRepository;
import com.jana.bms.service.criteria.PaymentCriteria;
import com.jana.bms.service.dto.PaymentDTO;
import com.jana.bms.service.mapper.PaymentMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link PaymentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaymentResourceIT {

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_AMOUNT = new BigDecimal(1 - 1);

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COUPON_ID = "AAAAAAAAAA";
    private static final String UPDATED_COUPON_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PAYMENT_METHOD = "AAAAAAAAAA";
    private static final String UPDATED_PAYMENT_METHOD = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/payments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{paymentId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentMapper paymentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaymentMockMvc;

    private Payment payment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createEntity(EntityManager em) {
        Payment payment = new Payment()
            .amount(DEFAULT_AMOUNT)
            .timestamp(DEFAULT_TIMESTAMP)
            .couponId(DEFAULT_COUPON_ID)
            .paymentMethod(DEFAULT_PAYMENT_METHOD);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        payment.setBooking(booking);
        return payment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Payment createUpdatedEntity(EntityManager em) {
        Payment payment = new Payment()
            .amount(UPDATED_AMOUNT)
            .timestamp(UPDATED_TIMESTAMP)
            .couponId(UPDATED_COUPON_ID)
            .paymentMethod(UPDATED_PAYMENT_METHOD);
        // Add required entity
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            booking = BookingResourceIT.createUpdatedEntity(em);
            em.persist(booking);
            em.flush();
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        payment.setBooking(booking);
        return payment;
    }

    @BeforeEach
    public void initTest() {
        payment = createEntity(em);
    }

    @Test
    @Transactional
    void createPayment() throws Exception {
        int databaseSizeBeforeCreate = paymentRepository.findAll().size();
        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);
        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isCreated());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate + 1);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testPayment.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testPayment.getCouponId()).isEqualTo(DEFAULT_COUPON_ID);
        assertThat(testPayment.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void createPaymentWithExistingId() throws Exception {
        // Create the Payment with an existing ID
        payment.setPaymentId(1L);
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        int databaseSizeBeforeCreate = paymentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setAmount(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = paymentRepository.findAll().size();
        // set the field null
        payment.setTimestamp(null);

        // Create the Payment, which fails.
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        restPaymentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isBadRequest());

        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPayments() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=paymentId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].paymentId").value(hasItem(payment.getPaymentId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].couponId").value(hasItem(DEFAULT_COUPON_ID)))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD)));
    }

    @Test
    @Transactional
    void getPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get the payment
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL_ID, payment.getPaymentId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.paymentId").value(payment.getPaymentId().intValue()))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.couponId").value(DEFAULT_COUPON_ID))
            .andExpect(jsonPath("$.paymentMethod").value(DEFAULT_PAYMENT_METHOD));
    }

    @Test
    @Transactional
    void getPaymentsByIdFiltering() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        Long id = payment.getPaymentId();

        defaultPaymentShouldBeFound("paymentId.equals=" + id);
        defaultPaymentShouldNotBeFound("paymentId.notEquals=" + id);

        defaultPaymentShouldBeFound("paymentId.greaterThanOrEqual=" + id);
        defaultPaymentShouldNotBeFound("paymentId.greaterThan=" + id);

        defaultPaymentShouldBeFound("paymentId.lessThanOrEqual=" + id);
        defaultPaymentShouldNotBeFound("paymentId.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount equals to DEFAULT_AMOUNT
        defaultPaymentShouldBeFound("amount.equals=" + DEFAULT_AMOUNT);

        // Get all the paymentList where amount equals to UPDATED_AMOUNT
        defaultPaymentShouldNotBeFound("amount.equals=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount in DEFAULT_AMOUNT or UPDATED_AMOUNT
        defaultPaymentShouldBeFound("amount.in=" + DEFAULT_AMOUNT + "," + UPDATED_AMOUNT);

        // Get all the paymentList where amount equals to UPDATED_AMOUNT
        defaultPaymentShouldNotBeFound("amount.in=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is not null
        defaultPaymentShouldBeFound("amount.specified=true");

        // Get all the paymentList where amount is null
        defaultPaymentShouldNotBeFound("amount.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is greater than or equal to DEFAULT_AMOUNT
        defaultPaymentShouldBeFound("amount.greaterThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the paymentList where amount is greater than or equal to UPDATED_AMOUNT
        defaultPaymentShouldNotBeFound("amount.greaterThanOrEqual=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is less than or equal to DEFAULT_AMOUNT
        defaultPaymentShouldBeFound("amount.lessThanOrEqual=" + DEFAULT_AMOUNT);

        // Get all the paymentList where amount is less than or equal to SMALLER_AMOUNT
        defaultPaymentShouldNotBeFound("amount.lessThanOrEqual=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is less than DEFAULT_AMOUNT
        defaultPaymentShouldNotBeFound("amount.lessThan=" + DEFAULT_AMOUNT);

        // Get all the paymentList where amount is less than UPDATED_AMOUNT
        defaultPaymentShouldBeFound("amount.lessThan=" + UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where amount is greater than DEFAULT_AMOUNT
        defaultPaymentShouldNotBeFound("amount.greaterThan=" + DEFAULT_AMOUNT);

        // Get all the paymentList where amount is greater than SMALLER_AMOUNT
        defaultPaymentShouldBeFound("amount.greaterThan=" + SMALLER_AMOUNT);
    }

    @Test
    @Transactional
    void getAllPaymentsByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where timestamp equals to DEFAULT_TIMESTAMP
        defaultPaymentShouldBeFound("timestamp.equals=" + DEFAULT_TIMESTAMP);

        // Get all the paymentList where timestamp equals to UPDATED_TIMESTAMP
        defaultPaymentShouldNotBeFound("timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    void getAllPaymentsByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where timestamp in DEFAULT_TIMESTAMP or UPDATED_TIMESTAMP
        defaultPaymentShouldBeFound("timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP);

        // Get all the paymentList where timestamp equals to UPDATED_TIMESTAMP
        defaultPaymentShouldNotBeFound("timestamp.in=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    void getAllPaymentsByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where timestamp is not null
        defaultPaymentShouldBeFound("timestamp.specified=true");

        // Get all the paymentList where timestamp is null
        defaultPaymentShouldNotBeFound("timestamp.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByCouponIdIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where couponId equals to DEFAULT_COUPON_ID
        defaultPaymentShouldBeFound("couponId.equals=" + DEFAULT_COUPON_ID);

        // Get all the paymentList where couponId equals to UPDATED_COUPON_ID
        defaultPaymentShouldNotBeFound("couponId.equals=" + UPDATED_COUPON_ID);
    }

    @Test
    @Transactional
    void getAllPaymentsByCouponIdIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where couponId in DEFAULT_COUPON_ID or UPDATED_COUPON_ID
        defaultPaymentShouldBeFound("couponId.in=" + DEFAULT_COUPON_ID + "," + UPDATED_COUPON_ID);

        // Get all the paymentList where couponId equals to UPDATED_COUPON_ID
        defaultPaymentShouldNotBeFound("couponId.in=" + UPDATED_COUPON_ID);
    }

    @Test
    @Transactional
    void getAllPaymentsByCouponIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where couponId is not null
        defaultPaymentShouldBeFound("couponId.specified=true");

        // Get all the paymentList where couponId is null
        defaultPaymentShouldNotBeFound("couponId.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByCouponIdContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where couponId contains DEFAULT_COUPON_ID
        defaultPaymentShouldBeFound("couponId.contains=" + DEFAULT_COUPON_ID);

        // Get all the paymentList where couponId contains UPDATED_COUPON_ID
        defaultPaymentShouldNotBeFound("couponId.contains=" + UPDATED_COUPON_ID);
    }

    @Test
    @Transactional
    void getAllPaymentsByCouponIdNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where couponId does not contain DEFAULT_COUPON_ID
        defaultPaymentShouldNotBeFound("couponId.doesNotContain=" + DEFAULT_COUPON_ID);

        // Get all the paymentList where couponId does not contain UPDATED_COUPON_ID
        defaultPaymentShouldBeFound("couponId.doesNotContain=" + UPDATED_COUPON_ID);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentMethod equals to DEFAULT_PAYMENT_METHOD
        defaultPaymentShouldBeFound("paymentMethod.equals=" + DEFAULT_PAYMENT_METHOD);

        // Get all the paymentList where paymentMethod equals to UPDATED_PAYMENT_METHOD
        defaultPaymentShouldNotBeFound("paymentMethod.equals=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentMethodIsInShouldWork() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentMethod in DEFAULT_PAYMENT_METHOD or UPDATED_PAYMENT_METHOD
        defaultPaymentShouldBeFound("paymentMethod.in=" + DEFAULT_PAYMENT_METHOD + "," + UPDATED_PAYMENT_METHOD);

        // Get all the paymentList where paymentMethod equals to UPDATED_PAYMENT_METHOD
        defaultPaymentShouldNotBeFound("paymentMethod.in=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentMethod is not null
        defaultPaymentShouldBeFound("paymentMethod.specified=true");

        // Get all the paymentList where paymentMethod is null
        defaultPaymentShouldNotBeFound("paymentMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentMethodContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentMethod contains DEFAULT_PAYMENT_METHOD
        defaultPaymentShouldBeFound("paymentMethod.contains=" + DEFAULT_PAYMENT_METHOD);

        // Get all the paymentList where paymentMethod contains UPDATED_PAYMENT_METHOD
        defaultPaymentShouldNotBeFound("paymentMethod.contains=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllPaymentsByPaymentMethodNotContainsSomething() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        // Get all the paymentList where paymentMethod does not contain DEFAULT_PAYMENT_METHOD
        defaultPaymentShouldNotBeFound("paymentMethod.doesNotContain=" + DEFAULT_PAYMENT_METHOD);

        // Get all the paymentList where paymentMethod does not contain UPDATED_PAYMENT_METHOD
        defaultPaymentShouldBeFound("paymentMethod.doesNotContain=" + UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void getAllPaymentsByBookingIsEqualToSomething() throws Exception {
        Booking booking;
        if (TestUtil.findAll(em, Booking.class).isEmpty()) {
            paymentRepository.saveAndFlush(payment);
            booking = BookingResourceIT.createEntity(em);
        } else {
            booking = TestUtil.findAll(em, Booking.class).get(0);
        }
        em.persist(booking);
        em.flush();
        payment.setBooking(booking);
        paymentRepository.saveAndFlush(payment);
        Long bookingId = booking.getBookingId();

        // Get all the paymentList where booking equals to bookingId
        defaultPaymentShouldBeFound("bookingId.equals=" + bookingId);

        // Get all the paymentList where booking equals to (bookingId + 1)
        defaultPaymentShouldNotBeFound("bookingId.equals=" + (bookingId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPaymentShouldBeFound(String filter) throws Exception {
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=paymentId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].paymentId").value(hasItem(payment.getPaymentId().intValue())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].couponId").value(hasItem(DEFAULT_COUPON_ID)))
            .andExpect(jsonPath("$.[*].paymentMethod").value(hasItem(DEFAULT_PAYMENT_METHOD)));

        // Check, that the count call also returns 1
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=paymentId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPaymentShouldNotBeFound(String filter) throws Exception {
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=paymentId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPaymentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=paymentId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPayment() throws Exception {
        // Get the payment
        restPaymentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment
        Payment updatedPayment = paymentRepository.findById(payment.getPaymentId()).get();
        // Disconnect from session so that the updates on updatedPayment are not directly saved in db
        em.detach(updatedPayment);
        updatedPayment
            .amount(UPDATED_AMOUNT)
            .timestamp(UPDATED_TIMESTAMP)
            .couponId(UPDATED_COUPON_ID)
            .paymentMethod(UPDATED_PAYMENT_METHOD);
        PaymentDTO paymentDTO = paymentMapper.toDto(updatedPayment);

        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentDTO.getPaymentId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testPayment.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testPayment.getCouponId()).isEqualTo(UPDATED_COUPON_ID);
        assertThat(testPayment.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void putNonExistingPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setPaymentId(count.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, paymentDTO.getPaymentId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setPaymentId(count.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setPaymentId(count.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(paymentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setPaymentId(payment.getPaymentId());

        partialUpdatedPayment.timestamp(UPDATED_TIMESTAMP);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getPaymentId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getAmount()).isEqualByComparingTo(DEFAULT_AMOUNT);
        assertThat(testPayment.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testPayment.getCouponId()).isEqualTo(DEFAULT_COUPON_ID);
        assertThat(testPayment.getPaymentMethod()).isEqualTo(DEFAULT_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void fullUpdatePaymentWithPatch() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();

        // Update the payment using partial update
        Payment partialUpdatedPayment = new Payment();
        partialUpdatedPayment.setPaymentId(payment.getPaymentId());

        partialUpdatedPayment
            .amount(UPDATED_AMOUNT)
            .timestamp(UPDATED_TIMESTAMP)
            .couponId(UPDATED_COUPON_ID)
            .paymentMethod(UPDATED_PAYMENT_METHOD);

        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPayment.getPaymentId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPayment))
            )
            .andExpect(status().isOk());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
        Payment testPayment = paymentList.get(paymentList.size() - 1);
        assertThat(testPayment.getAmount()).isEqualByComparingTo(UPDATED_AMOUNT);
        assertThat(testPayment.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testPayment.getCouponId()).isEqualTo(UPDATED_COUPON_ID);
        assertThat(testPayment.getPaymentMethod()).isEqualTo(UPDATED_PAYMENT_METHOD);
    }

    @Test
    @Transactional
    void patchNonExistingPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setPaymentId(count.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paymentDTO.getPaymentId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setPaymentId(count.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(paymentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPayment() throws Exception {
        int databaseSizeBeforeUpdate = paymentRepository.findAll().size();
        payment.setPaymentId(count.incrementAndGet());

        // Create the Payment
        PaymentDTO paymentDTO = paymentMapper.toDto(payment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaymentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(paymentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Payment in the database
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePayment() throws Exception {
        // Initialize the database
        paymentRepository.saveAndFlush(payment);

        int databaseSizeBeforeDelete = paymentRepository.findAll().size();

        // Delete the payment
        restPaymentMockMvc
            .perform(delete(ENTITY_API_URL_ID, payment.getPaymentId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Payment> paymentList = paymentRepository.findAll();
        assertThat(paymentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
