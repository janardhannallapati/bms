package com.jana.bms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jana.bms.IntegrationTest;
import com.jana.bms.domain.Screen;
import com.jana.bms.domain.Theatre;
import com.jana.bms.repository.ScreenRepository;
import com.jana.bms.service.criteria.ScreenCriteria;
import com.jana.bms.service.dto.ScreenDTO;
import com.jana.bms.service.mapper.ScreenMapper;
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
 * Integration tests for the {@link ScreenResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScreenResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_TOTAL_SEATS = 1;
    private static final Integer UPDATED_TOTAL_SEATS = 2;
    private static final Integer SMALLER_TOTAL_SEATS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/screens";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{screenId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ScreenRepository screenRepository;

    @Autowired
    private ScreenMapper screenMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScreenMockMvc;

    private Screen screen;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Screen createEntity(EntityManager em) {
        Screen screen = new Screen().name(DEFAULT_NAME).totalSeats(DEFAULT_TOTAL_SEATS);
        // Add required entity
        Theatre theatre;
        if (TestUtil.findAll(em, Theatre.class).isEmpty()) {
            theatre = TheatreResourceIT.createEntity(em);
            em.persist(theatre);
            em.flush();
        } else {
            theatre = TestUtil.findAll(em, Theatre.class).get(0);
        }
        screen.setTheatre(theatre);
        return screen;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Screen createUpdatedEntity(EntityManager em) {
        Screen screen = new Screen().name(UPDATED_NAME).totalSeats(UPDATED_TOTAL_SEATS);
        // Add required entity
        Theatre theatre;
        if (TestUtil.findAll(em, Theatre.class).isEmpty()) {
            theatre = TheatreResourceIT.createUpdatedEntity(em);
            em.persist(theatre);
            em.flush();
        } else {
            theatre = TestUtil.findAll(em, Theatre.class).get(0);
        }
        screen.setTheatre(theatre);
        return screen;
    }

    @BeforeEach
    public void initTest() {
        screen = createEntity(em);
    }

    @Test
    @Transactional
    void createScreen() throws Exception {
        int databaseSizeBeforeCreate = screenRepository.findAll().size();
        // Create the Screen
        ScreenDTO screenDTO = screenMapper.toDto(screen);
        restScreenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(screenDTO)))
            .andExpect(status().isCreated());

        // Validate the Screen in the database
        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeCreate + 1);
        Screen testScreen = screenList.get(screenList.size() - 1);
        assertThat(testScreen.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testScreen.getTotalSeats()).isEqualTo(DEFAULT_TOTAL_SEATS);
    }

    @Test
    @Transactional
    void createScreenWithExistingId() throws Exception {
        // Create the Screen with an existing ID
        screen.setScreenId(1L);
        ScreenDTO screenDTO = screenMapper.toDto(screen);

        int databaseSizeBeforeCreate = screenRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScreenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(screenDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Screen in the database
        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = screenRepository.findAll().size();
        // set the field null
        screen.setName(null);

        // Create the Screen, which fails.
        ScreenDTO screenDTO = screenMapper.toDto(screen);

        restScreenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(screenDTO)))
            .andExpect(status().isBadRequest());

        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTotalSeatsIsRequired() throws Exception {
        int databaseSizeBeforeTest = screenRepository.findAll().size();
        // set the field null
        screen.setTotalSeats(null);

        // Create the Screen, which fails.
        ScreenDTO screenDTO = screenMapper.toDto(screen);

        restScreenMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(screenDTO)))
            .andExpect(status().isBadRequest());

        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllScreens() throws Exception {
        // Initialize the database
        screenRepository.saveAndFlush(screen);

        // Get all the screenList
        restScreenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=screenId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].screenId").value(hasItem(screen.getScreenId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].totalSeats").value(hasItem(DEFAULT_TOTAL_SEATS)));
    }

    @Test
    @Transactional
    void getScreen() throws Exception {
        // Initialize the database
        screenRepository.saveAndFlush(screen);

        // Get the screen
        restScreenMockMvc
            .perform(get(ENTITY_API_URL_ID, screen.getScreenId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.screenId").value(screen.getScreenId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.totalSeats").value(DEFAULT_TOTAL_SEATS));
    }

    @Test
    @Transactional
    void getScreensByIdFiltering() throws Exception {
        // Initialize the database
        screenRepository.saveAndFlush(screen);

        Long id = screen.getScreenId();

        defaultScreenShouldBeFound("screenId.equals=" + id);
        defaultScreenShouldNotBeFound("screenId.notEquals=" + id);

        defaultScreenShouldBeFound("screenId.greaterThanOrEqual=" + id);
        defaultScreenShouldNotBeFound("screenId.greaterThan=" + id);

        defaultScreenShouldBeFound("screenId.lessThanOrEqual=" + id);
        defaultScreenShouldNotBeFound("screenId.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllScreensByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        screenRepository.saveAndFlush(screen);

        // Get all the screenList where name equals to DEFAULT_NAME
        defaultScreenShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the screenList where name equals to UPDATED_NAME
        defaultScreenShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllScreensByNameIsInShouldWork() throws Exception {
        // Initialize the database
        screenRepository.saveAndFlush(screen);

        // Get all the screenList where name in DEFAULT_NAME or UPDATED_NAME
        defaultScreenShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the screenList where name equals to UPDATED_NAME
        defaultScreenShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllScreensByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        screenRepository.saveAndFlush(screen);

        // Get all the screenList where name is not null
        defaultScreenShouldBeFound("name.specified=true");

        // Get all the screenList where name is null
        defaultScreenShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllScreensByNameContainsSomething() throws Exception {
        // Initialize the database
        screenRepository.saveAndFlush(screen);

        // Get all the screenList where name contains DEFAULT_NAME
        defaultScreenShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the screenList where name contains UPDATED_NAME
        defaultScreenShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllScreensByNameNotContainsSomething() throws Exception {
        // Initialize the database
        screenRepository.saveAndFlush(screen);

        // Get all the screenList where name does not contain DEFAULT_NAME
        defaultScreenShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the screenList where name does not contain UPDATED_NAME
        defaultScreenShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllScreensByTotalSeatsIsEqualToSomething() throws Exception {
        // Initialize the database
        screenRepository.saveAndFlush(screen);

        // Get all the screenList where totalSeats equals to DEFAULT_TOTAL_SEATS
        defaultScreenShouldBeFound("totalSeats.equals=" + DEFAULT_TOTAL_SEATS);

        // Get all the screenList where totalSeats equals to UPDATED_TOTAL_SEATS
        defaultScreenShouldNotBeFound("totalSeats.equals=" + UPDATED_TOTAL_SEATS);
    }

    @Test
    @Transactional
    void getAllScreensByTotalSeatsIsInShouldWork() throws Exception {
        // Initialize the database
        screenRepository.saveAndFlush(screen);

        // Get all the screenList where totalSeats in DEFAULT_TOTAL_SEATS or UPDATED_TOTAL_SEATS
        defaultScreenShouldBeFound("totalSeats.in=" + DEFAULT_TOTAL_SEATS + "," + UPDATED_TOTAL_SEATS);

        // Get all the screenList where totalSeats equals to UPDATED_TOTAL_SEATS
        defaultScreenShouldNotBeFound("totalSeats.in=" + UPDATED_TOTAL_SEATS);
    }

    @Test
    @Transactional
    void getAllScreensByTotalSeatsIsNullOrNotNull() throws Exception {
        // Initialize the database
        screenRepository.saveAndFlush(screen);

        // Get all the screenList where totalSeats is not null
        defaultScreenShouldBeFound("totalSeats.specified=true");

        // Get all the screenList where totalSeats is null
        defaultScreenShouldNotBeFound("totalSeats.specified=false");
    }

    @Test
    @Transactional
    void getAllScreensByTotalSeatsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        screenRepository.saveAndFlush(screen);

        // Get all the screenList where totalSeats is greater than or equal to DEFAULT_TOTAL_SEATS
        defaultScreenShouldBeFound("totalSeats.greaterThanOrEqual=" + DEFAULT_TOTAL_SEATS);

        // Get all the screenList where totalSeats is greater than or equal to UPDATED_TOTAL_SEATS
        defaultScreenShouldNotBeFound("totalSeats.greaterThanOrEqual=" + UPDATED_TOTAL_SEATS);
    }

    @Test
    @Transactional
    void getAllScreensByTotalSeatsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        screenRepository.saveAndFlush(screen);

        // Get all the screenList where totalSeats is less than or equal to DEFAULT_TOTAL_SEATS
        defaultScreenShouldBeFound("totalSeats.lessThanOrEqual=" + DEFAULT_TOTAL_SEATS);

        // Get all the screenList where totalSeats is less than or equal to SMALLER_TOTAL_SEATS
        defaultScreenShouldNotBeFound("totalSeats.lessThanOrEqual=" + SMALLER_TOTAL_SEATS);
    }

    @Test
    @Transactional
    void getAllScreensByTotalSeatsIsLessThanSomething() throws Exception {
        // Initialize the database
        screenRepository.saveAndFlush(screen);

        // Get all the screenList where totalSeats is less than DEFAULT_TOTAL_SEATS
        defaultScreenShouldNotBeFound("totalSeats.lessThan=" + DEFAULT_TOTAL_SEATS);

        // Get all the screenList where totalSeats is less than UPDATED_TOTAL_SEATS
        defaultScreenShouldBeFound("totalSeats.lessThan=" + UPDATED_TOTAL_SEATS);
    }

    @Test
    @Transactional
    void getAllScreensByTotalSeatsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        screenRepository.saveAndFlush(screen);

        // Get all the screenList where totalSeats is greater than DEFAULT_TOTAL_SEATS
        defaultScreenShouldNotBeFound("totalSeats.greaterThan=" + DEFAULT_TOTAL_SEATS);

        // Get all the screenList where totalSeats is greater than SMALLER_TOTAL_SEATS
        defaultScreenShouldBeFound("totalSeats.greaterThan=" + SMALLER_TOTAL_SEATS);
    }

    @Test
    @Transactional
    void getAllScreensByTheatreIsEqualToSomething() throws Exception {
        Theatre theatre;
        if (TestUtil.findAll(em, Theatre.class).isEmpty()) {
            screenRepository.saveAndFlush(screen);
            theatre = TheatreResourceIT.createEntity(em);
        } else {
            theatre = TestUtil.findAll(em, Theatre.class).get(0);
        }
        em.persist(theatre);
        em.flush();
        screen.setTheatre(theatre);
        screenRepository.saveAndFlush(screen);
        Long theatreId = theatre.getTheatreId();

        // Get all the screenList where theatre equals to theatreId
        defaultScreenShouldBeFound("theatreId.equals=" + theatreId);

        // Get all the screenList where theatre equals to (theatreId + 1)
        defaultScreenShouldNotBeFound("theatreId.equals=" + (theatreId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultScreenShouldBeFound(String filter) throws Exception {
        restScreenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=screenId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].screenId").value(hasItem(screen.getScreenId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].totalSeats").value(hasItem(DEFAULT_TOTAL_SEATS)));

        // Check, that the count call also returns 1
        restScreenMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=screenId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultScreenShouldNotBeFound(String filter) throws Exception {
        restScreenMockMvc
            .perform(get(ENTITY_API_URL + "?sort=screenId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restScreenMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=screenId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingScreen() throws Exception {
        // Get the screen
        restScreenMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewScreen() throws Exception {
        // Initialize the database
        screenRepository.saveAndFlush(screen);

        int databaseSizeBeforeUpdate = screenRepository.findAll().size();

        // Update the screen
        Screen updatedScreen = screenRepository.findById(screen.getScreenId()).get();
        // Disconnect from session so that the updates on updatedScreen are not directly saved in db
        em.detach(updatedScreen);
        updatedScreen.name(UPDATED_NAME).totalSeats(UPDATED_TOTAL_SEATS);
        ScreenDTO screenDTO = screenMapper.toDto(updatedScreen);

        restScreenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, screenDTO.getScreenId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(screenDTO))
            )
            .andExpect(status().isOk());

        // Validate the Screen in the database
        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeUpdate);
        Screen testScreen = screenList.get(screenList.size() - 1);
        assertThat(testScreen.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testScreen.getTotalSeats()).isEqualTo(UPDATED_TOTAL_SEATS);
    }

    @Test
    @Transactional
    void putNonExistingScreen() throws Exception {
        int databaseSizeBeforeUpdate = screenRepository.findAll().size();
        screen.setScreenId(count.incrementAndGet());

        // Create the Screen
        ScreenDTO screenDTO = screenMapper.toDto(screen);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScreenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, screenDTO.getScreenId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(screenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Screen in the database
        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScreen() throws Exception {
        int databaseSizeBeforeUpdate = screenRepository.findAll().size();
        screen.setScreenId(count.incrementAndGet());

        // Create the Screen
        ScreenDTO screenDTO = screenMapper.toDto(screen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScreenMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(screenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Screen in the database
        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScreen() throws Exception {
        int databaseSizeBeforeUpdate = screenRepository.findAll().size();
        screen.setScreenId(count.incrementAndGet());

        // Create the Screen
        ScreenDTO screenDTO = screenMapper.toDto(screen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScreenMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(screenDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Screen in the database
        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScreenWithPatch() throws Exception {
        // Initialize the database
        screenRepository.saveAndFlush(screen);

        int databaseSizeBeforeUpdate = screenRepository.findAll().size();

        // Update the screen using partial update
        Screen partialUpdatedScreen = new Screen();
        partialUpdatedScreen.setScreenId(screen.getScreenId());

        partialUpdatedScreen.totalSeats(UPDATED_TOTAL_SEATS);

        restScreenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScreen.getScreenId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScreen))
            )
            .andExpect(status().isOk());

        // Validate the Screen in the database
        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeUpdate);
        Screen testScreen = screenList.get(screenList.size() - 1);
        assertThat(testScreen.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testScreen.getTotalSeats()).isEqualTo(UPDATED_TOTAL_SEATS);
    }

    @Test
    @Transactional
    void fullUpdateScreenWithPatch() throws Exception {
        // Initialize the database
        screenRepository.saveAndFlush(screen);

        int databaseSizeBeforeUpdate = screenRepository.findAll().size();

        // Update the screen using partial update
        Screen partialUpdatedScreen = new Screen();
        partialUpdatedScreen.setScreenId(screen.getScreenId());

        partialUpdatedScreen.name(UPDATED_NAME).totalSeats(UPDATED_TOTAL_SEATS);

        restScreenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScreen.getScreenId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedScreen))
            )
            .andExpect(status().isOk());

        // Validate the Screen in the database
        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeUpdate);
        Screen testScreen = screenList.get(screenList.size() - 1);
        assertThat(testScreen.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testScreen.getTotalSeats()).isEqualTo(UPDATED_TOTAL_SEATS);
    }

    @Test
    @Transactional
    void patchNonExistingScreen() throws Exception {
        int databaseSizeBeforeUpdate = screenRepository.findAll().size();
        screen.setScreenId(count.incrementAndGet());

        // Create the Screen
        ScreenDTO screenDTO = screenMapper.toDto(screen);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScreenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, screenDTO.getScreenId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(screenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Screen in the database
        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScreen() throws Exception {
        int databaseSizeBeforeUpdate = screenRepository.findAll().size();
        screen.setScreenId(count.incrementAndGet());

        // Create the Screen
        ScreenDTO screenDTO = screenMapper.toDto(screen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScreenMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(screenDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Screen in the database
        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScreen() throws Exception {
        int databaseSizeBeforeUpdate = screenRepository.findAll().size();
        screen.setScreenId(count.incrementAndGet());

        // Create the Screen
        ScreenDTO screenDTO = screenMapper.toDto(screen);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScreenMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(screenDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Screen in the database
        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScreen() throws Exception {
        // Initialize the database
        screenRepository.saveAndFlush(screen);

        int databaseSizeBeforeDelete = screenRepository.findAll().size();

        // Delete the screen
        restScreenMockMvc
            .perform(delete(ENTITY_API_URL_ID, screen.getScreenId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Screen> screenList = screenRepository.findAll();
        assertThat(screenList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
