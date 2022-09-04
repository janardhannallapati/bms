package com.jana.bms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jana.bms.IntegrationTest;
import com.jana.bms.domain.Theatre;
import com.jana.bms.repository.TheatreRepository;
import com.jana.bms.service.criteria.TheatreCriteria;
import com.jana.bms.service.dto.TheatreDTO;
import com.jana.bms.service.mapper.TheatreMapper;
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
 * Integration tests for the {@link TheatreResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TheatreResourceIT {

    private static final String DEFAULT_THEATRE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_THEATRE_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_NO_OF_SCREENS = 1;
    private static final Integer UPDATED_NO_OF_SCREENS = 2;
    private static final Integer SMALLER_NO_OF_SCREENS = 1 - 1;

    private static final String ENTITY_API_URL = "/api/theatres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{theatreId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TheatreRepository theatreRepository;

    @Autowired
    private TheatreMapper theatreMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTheatreMockMvc;

    private Theatre theatre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Theatre createEntity(EntityManager em) {
        Theatre theatre = new Theatre().theatreName(DEFAULT_THEATRE_NAME).noOfScreens(DEFAULT_NO_OF_SCREENS);
        return theatre;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Theatre createUpdatedEntity(EntityManager em) {
        Theatre theatre = new Theatre().theatreName(UPDATED_THEATRE_NAME).noOfScreens(UPDATED_NO_OF_SCREENS);
        return theatre;
    }

    @BeforeEach
    public void initTest() {
        theatre = createEntity(em);
    }

    @Test
    @Transactional
    void createTheatre() throws Exception {
        int databaseSizeBeforeCreate = theatreRepository.findAll().size();
        // Create the Theatre
        TheatreDTO theatreDTO = theatreMapper.toDto(theatre);
        restTheatreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(theatreDTO)))
            .andExpect(status().isCreated());

        // Validate the Theatre in the database
        List<Theatre> theatreList = theatreRepository.findAll();
        assertThat(theatreList).hasSize(databaseSizeBeforeCreate + 1);
        Theatre testTheatre = theatreList.get(theatreList.size() - 1);
        assertThat(testTheatre.getTheatreName()).isEqualTo(DEFAULT_THEATRE_NAME);
        assertThat(testTheatre.getNoOfScreens()).isEqualTo(DEFAULT_NO_OF_SCREENS);
    }

    @Test
    @Transactional
    void createTheatreWithExistingId() throws Exception {
        // Create the Theatre with an existing ID
        theatre.setTheatreId(1L);
        TheatreDTO theatreDTO = theatreMapper.toDto(theatre);

        int databaseSizeBeforeCreate = theatreRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTheatreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(theatreDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Theatre in the database
        List<Theatre> theatreList = theatreRepository.findAll();
        assertThat(theatreList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNoOfScreensIsRequired() throws Exception {
        int databaseSizeBeforeTest = theatreRepository.findAll().size();
        // set the field null
        theatre.setNoOfScreens(null);

        // Create the Theatre, which fails.
        TheatreDTO theatreDTO = theatreMapper.toDto(theatre);

        restTheatreMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(theatreDTO)))
            .andExpect(status().isBadRequest());

        List<Theatre> theatreList = theatreRepository.findAll();
        assertThat(theatreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTheatres() throws Exception {
        // Initialize the database
        theatreRepository.saveAndFlush(theatre);

        // Get all the theatreList
        restTheatreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=theatreId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].theatreId").value(hasItem(theatre.getTheatreId().intValue())))
            .andExpect(jsonPath("$.[*].theatreName").value(hasItem(DEFAULT_THEATRE_NAME)))
            .andExpect(jsonPath("$.[*].noOfScreens").value(hasItem(DEFAULT_NO_OF_SCREENS)));
    }

    @Test
    @Transactional
    void getTheatre() throws Exception {
        // Initialize the database
        theatreRepository.saveAndFlush(theatre);

        // Get the theatre
        restTheatreMockMvc
            .perform(get(ENTITY_API_URL_ID, theatre.getTheatreId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.theatreId").value(theatre.getTheatreId().intValue()))
            .andExpect(jsonPath("$.theatreName").value(DEFAULT_THEATRE_NAME))
            .andExpect(jsonPath("$.noOfScreens").value(DEFAULT_NO_OF_SCREENS));
    }

    @Test
    @Transactional
    void getTheatresByIdFiltering() throws Exception {
        // Initialize the database
        theatreRepository.saveAndFlush(theatre);

        Long id = theatre.getTheatreId();

        defaultTheatreShouldBeFound("theatreId.equals=" + id);
        defaultTheatreShouldNotBeFound("theatreId.notEquals=" + id);

        defaultTheatreShouldBeFound("theatreId.greaterThanOrEqual=" + id);
        defaultTheatreShouldNotBeFound("theatreId.greaterThan=" + id);

        defaultTheatreShouldBeFound("theatreId.lessThanOrEqual=" + id);
        defaultTheatreShouldNotBeFound("theatreId.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTheatresByTheatreNameIsEqualToSomething() throws Exception {
        // Initialize the database
        theatreRepository.saveAndFlush(theatre);

        // Get all the theatreList where theatreName equals to DEFAULT_THEATRE_NAME
        defaultTheatreShouldBeFound("theatreName.equals=" + DEFAULT_THEATRE_NAME);

        // Get all the theatreList where theatreName equals to UPDATED_THEATRE_NAME
        defaultTheatreShouldNotBeFound("theatreName.equals=" + UPDATED_THEATRE_NAME);
    }

    @Test
    @Transactional
    void getAllTheatresByTheatreNameIsInShouldWork() throws Exception {
        // Initialize the database
        theatreRepository.saveAndFlush(theatre);

        // Get all the theatreList where theatreName in DEFAULT_THEATRE_NAME or UPDATED_THEATRE_NAME
        defaultTheatreShouldBeFound("theatreName.in=" + DEFAULT_THEATRE_NAME + "," + UPDATED_THEATRE_NAME);

        // Get all the theatreList where theatreName equals to UPDATED_THEATRE_NAME
        defaultTheatreShouldNotBeFound("theatreName.in=" + UPDATED_THEATRE_NAME);
    }

    @Test
    @Transactional
    void getAllTheatresByTheatreNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        theatreRepository.saveAndFlush(theatre);

        // Get all the theatreList where theatreName is not null
        defaultTheatreShouldBeFound("theatreName.specified=true");

        // Get all the theatreList where theatreName is null
        defaultTheatreShouldNotBeFound("theatreName.specified=false");
    }

    @Test
    @Transactional
    void getAllTheatresByTheatreNameContainsSomething() throws Exception {
        // Initialize the database
        theatreRepository.saveAndFlush(theatre);

        // Get all the theatreList where theatreName contains DEFAULT_THEATRE_NAME
        defaultTheatreShouldBeFound("theatreName.contains=" + DEFAULT_THEATRE_NAME);

        // Get all the theatreList where theatreName contains UPDATED_THEATRE_NAME
        defaultTheatreShouldNotBeFound("theatreName.contains=" + UPDATED_THEATRE_NAME);
    }

    @Test
    @Transactional
    void getAllTheatresByTheatreNameNotContainsSomething() throws Exception {
        // Initialize the database
        theatreRepository.saveAndFlush(theatre);

        // Get all the theatreList where theatreName does not contain DEFAULT_THEATRE_NAME
        defaultTheatreShouldNotBeFound("theatreName.doesNotContain=" + DEFAULT_THEATRE_NAME);

        // Get all the theatreList where theatreName does not contain UPDATED_THEATRE_NAME
        defaultTheatreShouldBeFound("theatreName.doesNotContain=" + UPDATED_THEATRE_NAME);
    }

    @Test
    @Transactional
    void getAllTheatresByNoOfScreensIsEqualToSomething() throws Exception {
        // Initialize the database
        theatreRepository.saveAndFlush(theatre);

        // Get all the theatreList where noOfScreens equals to DEFAULT_NO_OF_SCREENS
        defaultTheatreShouldBeFound("noOfScreens.equals=" + DEFAULT_NO_OF_SCREENS);

        // Get all the theatreList where noOfScreens equals to UPDATED_NO_OF_SCREENS
        defaultTheatreShouldNotBeFound("noOfScreens.equals=" + UPDATED_NO_OF_SCREENS);
    }

    @Test
    @Transactional
    void getAllTheatresByNoOfScreensIsInShouldWork() throws Exception {
        // Initialize the database
        theatreRepository.saveAndFlush(theatre);

        // Get all the theatreList where noOfScreens in DEFAULT_NO_OF_SCREENS or UPDATED_NO_OF_SCREENS
        defaultTheatreShouldBeFound("noOfScreens.in=" + DEFAULT_NO_OF_SCREENS + "," + UPDATED_NO_OF_SCREENS);

        // Get all the theatreList where noOfScreens equals to UPDATED_NO_OF_SCREENS
        defaultTheatreShouldNotBeFound("noOfScreens.in=" + UPDATED_NO_OF_SCREENS);
    }

    @Test
    @Transactional
    void getAllTheatresByNoOfScreensIsNullOrNotNull() throws Exception {
        // Initialize the database
        theatreRepository.saveAndFlush(theatre);

        // Get all the theatreList where noOfScreens is not null
        defaultTheatreShouldBeFound("noOfScreens.specified=true");

        // Get all the theatreList where noOfScreens is null
        defaultTheatreShouldNotBeFound("noOfScreens.specified=false");
    }

    @Test
    @Transactional
    void getAllTheatresByNoOfScreensIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        theatreRepository.saveAndFlush(theatre);

        // Get all the theatreList where noOfScreens is greater than or equal to DEFAULT_NO_OF_SCREENS
        defaultTheatreShouldBeFound("noOfScreens.greaterThanOrEqual=" + DEFAULT_NO_OF_SCREENS);

        // Get all the theatreList where noOfScreens is greater than or equal to UPDATED_NO_OF_SCREENS
        defaultTheatreShouldNotBeFound("noOfScreens.greaterThanOrEqual=" + UPDATED_NO_OF_SCREENS);
    }

    @Test
    @Transactional
    void getAllTheatresByNoOfScreensIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        theatreRepository.saveAndFlush(theatre);

        // Get all the theatreList where noOfScreens is less than or equal to DEFAULT_NO_OF_SCREENS
        defaultTheatreShouldBeFound("noOfScreens.lessThanOrEqual=" + DEFAULT_NO_OF_SCREENS);

        // Get all the theatreList where noOfScreens is less than or equal to SMALLER_NO_OF_SCREENS
        defaultTheatreShouldNotBeFound("noOfScreens.lessThanOrEqual=" + SMALLER_NO_OF_SCREENS);
    }

    @Test
    @Transactional
    void getAllTheatresByNoOfScreensIsLessThanSomething() throws Exception {
        // Initialize the database
        theatreRepository.saveAndFlush(theatre);

        // Get all the theatreList where noOfScreens is less than DEFAULT_NO_OF_SCREENS
        defaultTheatreShouldNotBeFound("noOfScreens.lessThan=" + DEFAULT_NO_OF_SCREENS);

        // Get all the theatreList where noOfScreens is less than UPDATED_NO_OF_SCREENS
        defaultTheatreShouldBeFound("noOfScreens.lessThan=" + UPDATED_NO_OF_SCREENS);
    }

    @Test
    @Transactional
    void getAllTheatresByNoOfScreensIsGreaterThanSomething() throws Exception {
        // Initialize the database
        theatreRepository.saveAndFlush(theatre);

        // Get all the theatreList where noOfScreens is greater than DEFAULT_NO_OF_SCREENS
        defaultTheatreShouldNotBeFound("noOfScreens.greaterThan=" + DEFAULT_NO_OF_SCREENS);

        // Get all the theatreList where noOfScreens is greater than SMALLER_NO_OF_SCREENS
        defaultTheatreShouldBeFound("noOfScreens.greaterThan=" + SMALLER_NO_OF_SCREENS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTheatreShouldBeFound(String filter) throws Exception {
        restTheatreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=theatreId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].theatreId").value(hasItem(theatre.getTheatreId().intValue())))
            .andExpect(jsonPath("$.[*].theatreName").value(hasItem(DEFAULT_THEATRE_NAME)))
            .andExpect(jsonPath("$.[*].noOfScreens").value(hasItem(DEFAULT_NO_OF_SCREENS)));

        // Check, that the count call also returns 1
        restTheatreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=theatreId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTheatreShouldNotBeFound(String filter) throws Exception {
        restTheatreMockMvc
            .perform(get(ENTITY_API_URL + "?sort=theatreId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTheatreMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=theatreId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTheatre() throws Exception {
        // Get the theatre
        restTheatreMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTheatre() throws Exception {
        // Initialize the database
        theatreRepository.saveAndFlush(theatre);

        int databaseSizeBeforeUpdate = theatreRepository.findAll().size();

        // Update the theatre
        Theatre updatedTheatre = theatreRepository.findById(theatre.getTheatreId()).get();
        // Disconnect from session so that the updates on updatedTheatre are not directly saved in db
        em.detach(updatedTheatre);
        updatedTheatre.theatreName(UPDATED_THEATRE_NAME).noOfScreens(UPDATED_NO_OF_SCREENS);
        TheatreDTO theatreDTO = theatreMapper.toDto(updatedTheatre);

        restTheatreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, theatreDTO.getTheatreId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(theatreDTO))
            )
            .andExpect(status().isOk());

        // Validate the Theatre in the database
        List<Theatre> theatreList = theatreRepository.findAll();
        assertThat(theatreList).hasSize(databaseSizeBeforeUpdate);
        Theatre testTheatre = theatreList.get(theatreList.size() - 1);
        assertThat(testTheatre.getTheatreName()).isEqualTo(UPDATED_THEATRE_NAME);
        assertThat(testTheatre.getNoOfScreens()).isEqualTo(UPDATED_NO_OF_SCREENS);
    }

    @Test
    @Transactional
    void putNonExistingTheatre() throws Exception {
        int databaseSizeBeforeUpdate = theatreRepository.findAll().size();
        theatre.setTheatreId(count.incrementAndGet());

        // Create the Theatre
        TheatreDTO theatreDTO = theatreMapper.toDto(theatre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTheatreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, theatreDTO.getTheatreId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(theatreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Theatre in the database
        List<Theatre> theatreList = theatreRepository.findAll();
        assertThat(theatreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTheatre() throws Exception {
        int databaseSizeBeforeUpdate = theatreRepository.findAll().size();
        theatre.setTheatreId(count.incrementAndGet());

        // Create the Theatre
        TheatreDTO theatreDTO = theatreMapper.toDto(theatre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTheatreMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(theatreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Theatre in the database
        List<Theatre> theatreList = theatreRepository.findAll();
        assertThat(theatreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTheatre() throws Exception {
        int databaseSizeBeforeUpdate = theatreRepository.findAll().size();
        theatre.setTheatreId(count.incrementAndGet());

        // Create the Theatre
        TheatreDTO theatreDTO = theatreMapper.toDto(theatre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTheatreMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(theatreDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Theatre in the database
        List<Theatre> theatreList = theatreRepository.findAll();
        assertThat(theatreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTheatreWithPatch() throws Exception {
        // Initialize the database
        theatreRepository.saveAndFlush(theatre);

        int databaseSizeBeforeUpdate = theatreRepository.findAll().size();

        // Update the theatre using partial update
        Theatre partialUpdatedTheatre = new Theatre();
        partialUpdatedTheatre.setTheatreId(theatre.getTheatreId());

        restTheatreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTheatre.getTheatreId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTheatre))
            )
            .andExpect(status().isOk());

        // Validate the Theatre in the database
        List<Theatre> theatreList = theatreRepository.findAll();
        assertThat(theatreList).hasSize(databaseSizeBeforeUpdate);
        Theatre testTheatre = theatreList.get(theatreList.size() - 1);
        assertThat(testTheatre.getTheatreName()).isEqualTo(DEFAULT_THEATRE_NAME);
        assertThat(testTheatre.getNoOfScreens()).isEqualTo(DEFAULT_NO_OF_SCREENS);
    }

    @Test
    @Transactional
    void fullUpdateTheatreWithPatch() throws Exception {
        // Initialize the database
        theatreRepository.saveAndFlush(theatre);

        int databaseSizeBeforeUpdate = theatreRepository.findAll().size();

        // Update the theatre using partial update
        Theatre partialUpdatedTheatre = new Theatre();
        partialUpdatedTheatre.setTheatreId(theatre.getTheatreId());

        partialUpdatedTheatre.theatreName(UPDATED_THEATRE_NAME).noOfScreens(UPDATED_NO_OF_SCREENS);

        restTheatreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTheatre.getTheatreId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTheatre))
            )
            .andExpect(status().isOk());

        // Validate the Theatre in the database
        List<Theatre> theatreList = theatreRepository.findAll();
        assertThat(theatreList).hasSize(databaseSizeBeforeUpdate);
        Theatre testTheatre = theatreList.get(theatreList.size() - 1);
        assertThat(testTheatre.getTheatreName()).isEqualTo(UPDATED_THEATRE_NAME);
        assertThat(testTheatre.getNoOfScreens()).isEqualTo(UPDATED_NO_OF_SCREENS);
    }

    @Test
    @Transactional
    void patchNonExistingTheatre() throws Exception {
        int databaseSizeBeforeUpdate = theatreRepository.findAll().size();
        theatre.setTheatreId(count.incrementAndGet());

        // Create the Theatre
        TheatreDTO theatreDTO = theatreMapper.toDto(theatre);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTheatreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, theatreDTO.getTheatreId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(theatreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Theatre in the database
        List<Theatre> theatreList = theatreRepository.findAll();
        assertThat(theatreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTheatre() throws Exception {
        int databaseSizeBeforeUpdate = theatreRepository.findAll().size();
        theatre.setTheatreId(count.incrementAndGet());

        // Create the Theatre
        TheatreDTO theatreDTO = theatreMapper.toDto(theatre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTheatreMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(theatreDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Theatre in the database
        List<Theatre> theatreList = theatreRepository.findAll();
        assertThat(theatreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTheatre() throws Exception {
        int databaseSizeBeforeUpdate = theatreRepository.findAll().size();
        theatre.setTheatreId(count.incrementAndGet());

        // Create the Theatre
        TheatreDTO theatreDTO = theatreMapper.toDto(theatre);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTheatreMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(theatreDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Theatre in the database
        List<Theatre> theatreList = theatreRepository.findAll();
        assertThat(theatreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTheatre() throws Exception {
        // Initialize the database
        theatreRepository.saveAndFlush(theatre);

        int databaseSizeBeforeDelete = theatreRepository.findAll().size();

        // Delete the theatre
        restTheatreMockMvc
            .perform(delete(ENTITY_API_URL_ID, theatre.getTheatreId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Theatre> theatreList = theatreRepository.findAll();
        assertThat(theatreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
