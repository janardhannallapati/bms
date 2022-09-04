package com.jana.bms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jana.bms.IntegrationTest;
import com.jana.bms.domain.Actor;
import com.jana.bms.domain.Movie;
import com.jana.bms.repository.ActorRepository;
import com.jana.bms.service.criteria.ActorCriteria;
import com.jana.bms.service.dto.ActorDTO;
import com.jana.bms.service.mapper.ActorMapper;
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
 * Integration tests for the {@link ActorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ActorResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/actors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{actorId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ActorRepository actorRepository;

    @Autowired
    private ActorMapper actorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActorMockMvc;

    private Actor actor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Actor createEntity(EntityManager em) {
        Actor actor = new Actor().firstName(DEFAULT_FIRST_NAME).lastName(DEFAULT_LAST_NAME);
        return actor;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Actor createUpdatedEntity(EntityManager em) {
        Actor actor = new Actor().firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);
        return actor;
    }

    @BeforeEach
    public void initTest() {
        actor = createEntity(em);
    }

    @Test
    @Transactional
    void createActor() throws Exception {
        int databaseSizeBeforeCreate = actorRepository.findAll().size();
        // Create the Actor
        ActorDTO actorDTO = actorMapper.toDto(actor);
        restActorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(actorDTO)))
            .andExpect(status().isCreated());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeCreate + 1);
        Actor testActor = actorList.get(actorList.size() - 1);
        assertThat(testActor.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testActor.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void createActorWithExistingId() throws Exception {
        // Create the Actor with an existing ID
        actor.setActorId(1L);
        ActorDTO actorDTO = actorMapper.toDto(actor);

        int databaseSizeBeforeCreate = actorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restActorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(actorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = actorRepository.findAll().size();
        // set the field null
        actor.setFirstName(null);

        // Create the Actor, which fails.
        ActorDTO actorDTO = actorMapper.toDto(actor);

        restActorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(actorDTO)))
            .andExpect(status().isBadRequest());

        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = actorRepository.findAll().size();
        // set the field null
        actor.setLastName(null);

        // Create the Actor, which fails.
        ActorDTO actorDTO = actorMapper.toDto(actor);

        restActorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(actorDTO)))
            .andExpect(status().isBadRequest());

        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllActors() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList
        restActorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=actorId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].actorId").value(hasItem(actor.getActorId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)));
    }

    @Test
    @Transactional
    void getActor() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get the actor
        restActorMockMvc
            .perform(get(ENTITY_API_URL_ID, actor.getActorId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.actorId").value(actor.getActorId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME));
    }

    @Test
    @Transactional
    void getActorsByIdFiltering() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        Long id = actor.getActorId();

        defaultActorShouldBeFound("actorId.equals=" + id);
        defaultActorShouldNotBeFound("actorId.notEquals=" + id);

        defaultActorShouldBeFound("actorId.greaterThanOrEqual=" + id);
        defaultActorShouldNotBeFound("actorId.greaterThan=" + id);

        defaultActorShouldBeFound("actorId.lessThanOrEqual=" + id);
        defaultActorShouldNotBeFound("actorId.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllActorsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where firstName equals to DEFAULT_FIRST_NAME
        defaultActorShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the actorList where firstName equals to UPDATED_FIRST_NAME
        defaultActorShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllActorsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultActorShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the actorList where firstName equals to UPDATED_FIRST_NAME
        defaultActorShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllActorsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where firstName is not null
        defaultActorShouldBeFound("firstName.specified=true");

        // Get all the actorList where firstName is null
        defaultActorShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    void getAllActorsByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where firstName contains DEFAULT_FIRST_NAME
        defaultActorShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the actorList where firstName contains UPDATED_FIRST_NAME
        defaultActorShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllActorsByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where firstName does not contain DEFAULT_FIRST_NAME
        defaultActorShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the actorList where firstName does not contain UPDATED_FIRST_NAME
        defaultActorShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void getAllActorsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where lastName equals to DEFAULT_LAST_NAME
        defaultActorShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the actorList where lastName equals to UPDATED_LAST_NAME
        defaultActorShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllActorsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultActorShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the actorList where lastName equals to UPDATED_LAST_NAME
        defaultActorShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllActorsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where lastName is not null
        defaultActorShouldBeFound("lastName.specified=true");

        // Get all the actorList where lastName is null
        defaultActorShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    void getAllActorsByLastNameContainsSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where lastName contains DEFAULT_LAST_NAME
        defaultActorShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the actorList where lastName contains UPDATED_LAST_NAME
        defaultActorShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllActorsByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        // Get all the actorList where lastName does not contain DEFAULT_LAST_NAME
        defaultActorShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the actorList where lastName does not contain UPDATED_LAST_NAME
        defaultActorShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void getAllActorsByMovieIsEqualToSomething() throws Exception {
        Movie movie;
        if (TestUtil.findAll(em, Movie.class).isEmpty()) {
            actorRepository.saveAndFlush(actor);
            movie = MovieResourceIT.createEntity(em);
        } else {
            movie = TestUtil.findAll(em, Movie.class).get(0);
        }
        em.persist(movie);
        em.flush();
        actor.addMovie(movie);
        actorRepository.saveAndFlush(actor);
        Long movieId = movie.getMovieId();

        // Get all the actorList where movie equals to movieId
        defaultActorShouldBeFound("movieId.equals=" + movieId);

        // Get all the actorList where movie equals to (movieId + 1)
        defaultActorShouldNotBeFound("movieId.equals=" + (movieId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultActorShouldBeFound(String filter) throws Exception {
        restActorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=actorId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].actorId").value(hasItem(actor.getActorId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)));

        // Check, that the count call also returns 1
        restActorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=actorId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultActorShouldNotBeFound(String filter) throws Exception {
        restActorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=actorId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restActorMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=actorId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingActor() throws Exception {
        // Get the actor
        restActorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewActor() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        int databaseSizeBeforeUpdate = actorRepository.findAll().size();

        // Update the actor
        Actor updatedActor = actorRepository.findById(actor.getActorId()).get();
        // Disconnect from session so that the updates on updatedActor are not directly saved in db
        em.detach(updatedActor);
        updatedActor.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);
        ActorDTO actorDTO = actorMapper.toDto(updatedActor);

        restActorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, actorDTO.getActorId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeUpdate);
        Actor testActor = actorList.get(actorList.size() - 1);
        assertThat(testActor.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testActor.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void putNonExistingActor() throws Exception {
        int databaseSizeBeforeUpdate = actorRepository.findAll().size();
        actor.setActorId(count.incrementAndGet());

        // Create the Actor
        ActorDTO actorDTO = actorMapper.toDto(actor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, actorDTO.getActorId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchActor() throws Exception {
        int databaseSizeBeforeUpdate = actorRepository.findAll().size();
        actor.setActorId(count.incrementAndGet());

        // Create the Actor
        ActorDTO actorDTO = actorMapper.toDto(actor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(actorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamActor() throws Exception {
        int databaseSizeBeforeUpdate = actorRepository.findAll().size();
        actor.setActorId(count.incrementAndGet());

        // Create the Actor
        ActorDTO actorDTO = actorMapper.toDto(actor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(actorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateActorWithPatch() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        int databaseSizeBeforeUpdate = actorRepository.findAll().size();

        // Update the actor using partial update
        Actor partialUpdatedActor = new Actor();
        partialUpdatedActor.setActorId(actor.getActorId());

        partialUpdatedActor.firstName(UPDATED_FIRST_NAME);

        restActorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActor.getActorId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedActor))
            )
            .andExpect(status().isOk());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeUpdate);
        Actor testActor = actorList.get(actorList.size() - 1);
        assertThat(testActor.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testActor.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    void fullUpdateActorWithPatch() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        int databaseSizeBeforeUpdate = actorRepository.findAll().size();

        // Update the actor using partial update
        Actor partialUpdatedActor = new Actor();
        partialUpdatedActor.setActorId(actor.getActorId());

        partialUpdatedActor.firstName(UPDATED_FIRST_NAME).lastName(UPDATED_LAST_NAME);

        restActorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedActor.getActorId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedActor))
            )
            .andExpect(status().isOk());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeUpdate);
        Actor testActor = actorList.get(actorList.size() - 1);
        assertThat(testActor.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testActor.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingActor() throws Exception {
        int databaseSizeBeforeUpdate = actorRepository.findAll().size();
        actor.setActorId(count.incrementAndGet());

        // Create the Actor
        ActorDTO actorDTO = actorMapper.toDto(actor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restActorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, actorDTO.getActorId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(actorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchActor() throws Exception {
        int databaseSizeBeforeUpdate = actorRepository.findAll().size();
        actor.setActorId(count.incrementAndGet());

        // Create the Actor
        ActorDTO actorDTO = actorMapper.toDto(actor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(actorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamActor() throws Exception {
        int databaseSizeBeforeUpdate = actorRepository.findAll().size();
        actor.setActorId(count.incrementAndGet());

        // Create the Actor
        ActorDTO actorDTO = actorMapper.toDto(actor);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restActorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(actorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Actor in the database
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteActor() throws Exception {
        // Initialize the database
        actorRepository.saveAndFlush(actor);

        int databaseSizeBeforeDelete = actorRepository.findAll().size();

        // Delete the actor
        restActorMockMvc
            .perform(delete(ENTITY_API_URL_ID, actor.getActorId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Actor> actorList = actorRepository.findAll();
        assertThat(actorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
