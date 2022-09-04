package com.jana.bms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jana.bms.IntegrationTest;
import com.jana.bms.domain.Country;
import com.jana.bms.repository.CountryRepository;
import com.jana.bms.service.criteria.CountryCriteria;
import com.jana.bms.service.dto.CountryDTO;
import com.jana.bms.service.mapper.CountryMapper;
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
 * Integration tests for the {@link CountryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CountryResourceIT {

    private static final String DEFAULT_COUNTRY_CODE = "AAA";
    private static final String UPDATED_COUNTRY_CODE = "BBB";

    private static final String DEFAULT_COUNTRY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/countries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{countryId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private CountryMapper countryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCountryMockMvc;

    private Country country;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Country createEntity(EntityManager em) {
        Country country = new Country().countryCode(DEFAULT_COUNTRY_CODE).countryName(DEFAULT_COUNTRY_NAME);
        return country;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Country createUpdatedEntity(EntityManager em) {
        Country country = new Country().countryCode(UPDATED_COUNTRY_CODE).countryName(UPDATED_COUNTRY_NAME);
        return country;
    }

    @BeforeEach
    public void initTest() {
        country = createEntity(em);
    }

    @Test
    @Transactional
    void createCountry() throws Exception {
        int databaseSizeBeforeCreate = countryRepository.findAll().size();
        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);
        restCountryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isCreated());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeCreate + 1);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getCountryCode()).isEqualTo(DEFAULT_COUNTRY_CODE);
        assertThat(testCountry.getCountryName()).isEqualTo(DEFAULT_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void createCountryWithExistingId() throws Exception {
        // Create the Country with an existing ID
        country.setCountryId(1L);
        CountryDTO countryDTO = countryMapper.toDto(country);

        int databaseSizeBeforeCreate = countryRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCountryCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().size();
        // set the field null
        country.setCountryCode(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        restCountryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCountryNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = countryRepository.findAll().size();
        // set the field null
        country.setCountryName(null);

        // Create the Country, which fails.
        CountryDTO countryDTO = countryMapper.toDto(country);

        restCountryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isBadRequest());

        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCountries() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=countryId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].countryId").value(hasItem(country.getCountryId().intValue())))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE)))
            .andExpect(jsonPath("$.[*].countryName").value(hasItem(DEFAULT_COUNTRY_NAME)));
    }

    @Test
    @Transactional
    void getCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get the country
        restCountryMockMvc
            .perform(get(ENTITY_API_URL_ID, country.getCountryId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.countryId").value(country.getCountryId().intValue()))
            .andExpect(jsonPath("$.countryCode").value(DEFAULT_COUNTRY_CODE))
            .andExpect(jsonPath("$.countryName").value(DEFAULT_COUNTRY_NAME));
    }

    @Test
    @Transactional
    void getCountriesByIdFiltering() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        Long id = country.getCountryId();

        defaultCountryShouldBeFound("countryId.equals=" + id);
        defaultCountryShouldNotBeFound("countryId.notEquals=" + id);

        defaultCountryShouldBeFound("countryId.greaterThanOrEqual=" + id);
        defaultCountryShouldNotBeFound("countryId.greaterThan=" + id);

        defaultCountryShouldBeFound("countryId.lessThanOrEqual=" + id);
        defaultCountryShouldNotBeFound("countryId.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryCode equals to DEFAULT_COUNTRY_CODE
        defaultCountryShouldBeFound("countryCode.equals=" + DEFAULT_COUNTRY_CODE);

        // Get all the countryList where countryCode equals to UPDATED_COUNTRY_CODE
        defaultCountryShouldNotBeFound("countryCode.equals=" + UPDATED_COUNTRY_CODE);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryCodeIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryCode in DEFAULT_COUNTRY_CODE or UPDATED_COUNTRY_CODE
        defaultCountryShouldBeFound("countryCode.in=" + DEFAULT_COUNTRY_CODE + "," + UPDATED_COUNTRY_CODE);

        // Get all the countryList where countryCode equals to UPDATED_COUNTRY_CODE
        defaultCountryShouldNotBeFound("countryCode.in=" + UPDATED_COUNTRY_CODE);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryCode is not null
        defaultCountryShouldBeFound("countryCode.specified=true");

        // Get all the countryList where countryCode is null
        defaultCountryShouldNotBeFound("countryCode.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByCountryCodeContainsSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryCode contains DEFAULT_COUNTRY_CODE
        defaultCountryShouldBeFound("countryCode.contains=" + DEFAULT_COUNTRY_CODE);

        // Get all the countryList where countryCode contains UPDATED_COUNTRY_CODE
        defaultCountryShouldNotBeFound("countryCode.contains=" + UPDATED_COUNTRY_CODE);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryCodeNotContainsSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryCode does not contain DEFAULT_COUNTRY_CODE
        defaultCountryShouldNotBeFound("countryCode.doesNotContain=" + DEFAULT_COUNTRY_CODE);

        // Get all the countryList where countryCode does not contain UPDATED_COUNTRY_CODE
        defaultCountryShouldBeFound("countryCode.doesNotContain=" + UPDATED_COUNTRY_CODE);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryNameIsEqualToSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryName equals to DEFAULT_COUNTRY_NAME
        defaultCountryShouldBeFound("countryName.equals=" + DEFAULT_COUNTRY_NAME);

        // Get all the countryList where countryName equals to UPDATED_COUNTRY_NAME
        defaultCountryShouldNotBeFound("countryName.equals=" + UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryNameIsInShouldWork() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryName in DEFAULT_COUNTRY_NAME or UPDATED_COUNTRY_NAME
        defaultCountryShouldBeFound("countryName.in=" + DEFAULT_COUNTRY_NAME + "," + UPDATED_COUNTRY_NAME);

        // Get all the countryList where countryName equals to UPDATED_COUNTRY_NAME
        defaultCountryShouldNotBeFound("countryName.in=" + UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryName is not null
        defaultCountryShouldBeFound("countryName.specified=true");

        // Get all the countryList where countryName is null
        defaultCountryShouldNotBeFound("countryName.specified=false");
    }

    @Test
    @Transactional
    void getAllCountriesByCountryNameContainsSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryName contains DEFAULT_COUNTRY_NAME
        defaultCountryShouldBeFound("countryName.contains=" + DEFAULT_COUNTRY_NAME);

        // Get all the countryList where countryName contains UPDATED_COUNTRY_NAME
        defaultCountryShouldNotBeFound("countryName.contains=" + UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void getAllCountriesByCountryNameNotContainsSomething() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        // Get all the countryList where countryName does not contain DEFAULT_COUNTRY_NAME
        defaultCountryShouldNotBeFound("countryName.doesNotContain=" + DEFAULT_COUNTRY_NAME);

        // Get all the countryList where countryName does not contain UPDATED_COUNTRY_NAME
        defaultCountryShouldBeFound("countryName.doesNotContain=" + UPDATED_COUNTRY_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCountryShouldBeFound(String filter) throws Exception {
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=countryId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].countryId").value(hasItem(country.getCountryId().intValue())))
            .andExpect(jsonPath("$.[*].countryCode").value(hasItem(DEFAULT_COUNTRY_CODE)))
            .andExpect(jsonPath("$.[*].countryName").value(hasItem(DEFAULT_COUNTRY_NAME)));

        // Check, that the count call also returns 1
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=countryId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCountryShouldNotBeFound(String filter) throws Exception {
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=countryId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCountryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=countryId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCountry() throws Exception {
        // Get the country
        restCountryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        int databaseSizeBeforeUpdate = countryRepository.findAll().size();

        // Update the country
        Country updatedCountry = countryRepository.findById(country.getCountryId()).get();
        // Disconnect from session so that the updates on updatedCountry are not directly saved in db
        em.detach(updatedCountry);
        updatedCountry.countryCode(UPDATED_COUNTRY_CODE).countryName(UPDATED_COUNTRY_NAME);
        CountryDTO countryDTO = countryMapper.toDto(updatedCountry);

        restCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countryDTO.getCountryId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countryDTO))
            )
            .andExpect(status().isOk());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getCountryCode()).isEqualTo(UPDATED_COUNTRY_CODE);
        assertThat(testCountry.getCountryName()).isEqualTo(UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();
        country.setCountryId(count.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, countryDTO.getCountryId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();
        country.setCountryId(count.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();
        country.setCountryId(count.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(countryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCountryWithPatch() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        int databaseSizeBeforeUpdate = countryRepository.findAll().size();

        // Update the country using partial update
        Country partialUpdatedCountry = new Country();
        partialUpdatedCountry.setCountryId(country.getCountryId());

        partialUpdatedCountry.countryCode(UPDATED_COUNTRY_CODE).countryName(UPDATED_COUNTRY_NAME);

        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountry.getCountryId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountry))
            )
            .andExpect(status().isOk());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getCountryCode()).isEqualTo(UPDATED_COUNTRY_CODE);
        assertThat(testCountry.getCountryName()).isEqualTo(UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCountryWithPatch() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        int databaseSizeBeforeUpdate = countryRepository.findAll().size();

        // Update the country using partial update
        Country partialUpdatedCountry = new Country();
        partialUpdatedCountry.setCountryId(country.getCountryId());

        partialUpdatedCountry.countryCode(UPDATED_COUNTRY_CODE).countryName(UPDATED_COUNTRY_NAME);

        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCountry.getCountryId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCountry))
            )
            .andExpect(status().isOk());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
        Country testCountry = countryList.get(countryList.size() - 1);
        assertThat(testCountry.getCountryCode()).isEqualTo(UPDATED_COUNTRY_CODE);
        assertThat(testCountry.getCountryName()).isEqualTo(UPDATED_COUNTRY_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();
        country.setCountryId(count.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, countryDTO.getCountryId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();
        country.setCountryId(count.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(countryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCountry() throws Exception {
        int databaseSizeBeforeUpdate = countryRepository.findAll().size();
        country.setCountryId(count.incrementAndGet());

        // Create the Country
        CountryDTO countryDTO = countryMapper.toDto(country);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCountryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(countryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Country in the database
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCountry() throws Exception {
        // Initialize the database
        countryRepository.saveAndFlush(country);

        int databaseSizeBeforeDelete = countryRepository.findAll().size();

        // Delete the country
        restCountryMockMvc
            .perform(delete(ENTITY_API_URL_ID, country.getCountryId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Country> countryList = countryRepository.findAll();
        assertThat(countryList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
