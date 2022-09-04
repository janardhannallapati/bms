package com.jana.bms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.jana.bms.IntegrationTest;
import com.jana.bms.domain.Address;
import com.jana.bms.domain.City;
import com.jana.bms.repository.AddressRepository;
import com.jana.bms.service.criteria.AddressCriteria;
import com.jana.bms.service.dto.AddressDTO;
import com.jana.bms.service.mapper.AddressMapper;
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
 * Integration tests for the {@link AddressResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AddressResourceIT {

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS_2 = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS_2 = "BBBBBBBBBB";

    private static final String DEFAULT_DISTRICT = "AAAAAAAAAA";
    private static final String UPDATED_DISTRICT = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/addresses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{addressId}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAddressMockMvc;

    private Address address;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Address createEntity(EntityManager em) {
        Address address = new Address()
            .address(DEFAULT_ADDRESS)
            .address2(DEFAULT_ADDRESS_2)
            .district(DEFAULT_DISTRICT)
            .postalCode(DEFAULT_POSTAL_CODE)
            .phone(DEFAULT_PHONE)
            .location(DEFAULT_LOCATION);
        // Add required entity
        City city;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            city = CityResourceIT.createEntity(em);
            em.persist(city);
            em.flush();
        } else {
            city = TestUtil.findAll(em, City.class).get(0);
        }
        address.setCity(city);
        return address;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Address createUpdatedEntity(EntityManager em) {
        Address address = new Address()
            .address(UPDATED_ADDRESS)
            .address2(UPDATED_ADDRESS_2)
            .district(UPDATED_DISTRICT)
            .postalCode(UPDATED_POSTAL_CODE)
            .phone(UPDATED_PHONE)
            .location(UPDATED_LOCATION);
        // Add required entity
        City city;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            city = CityResourceIT.createUpdatedEntity(em);
            em.persist(city);
            em.flush();
        } else {
            city = TestUtil.findAll(em, City.class).get(0);
        }
        address.setCity(city);
        return address;
    }

    @BeforeEach
    public void initTest() {
        address = createEntity(em);
    }

    @Test
    @Transactional
    void createAddress() throws Exception {
        int databaseSizeBeforeCreate = addressRepository.findAll().size();
        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);
        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isCreated());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeCreate + 1);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testAddress.getAddress2()).isEqualTo(DEFAULT_ADDRESS_2);
        assertThat(testAddress.getDistrict()).isEqualTo(DEFAULT_DISTRICT);
        assertThat(testAddress.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testAddress.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testAddress.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    void createAddressWithExistingId() throws Exception {
        // Create the Address with an existing ID
        address.setAddressId(1L);
        AddressDTO addressDTO = addressMapper.toDto(address);

        int databaseSizeBeforeCreate = addressRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setAddress(null);

        // Create the Address, which fails.
        AddressDTO addressDTO = addressMapper.toDto(address);

        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDistrictIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setDistrict(null);

        // Create the Address, which fails.
        AddressDTO addressDTO = addressMapper.toDto(address);

        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPostalCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setPostalCode(null);

        // Create the Address, which fails.
        AddressDTO addressDTO = addressMapper.toDto(address);

        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setPhone(null);

        // Create the Address, which fails.
        AddressDTO addressDTO = addressMapper.toDto(address);

        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = addressRepository.findAll().size();
        // set the field null
        address.setLocation(null);

        // Create the Address, which fails.
        AddressDTO addressDTO = addressMapper.toDto(address);

        restAddressMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isBadRequest());

        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAddresses() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=addressId,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].addressId").value(hasItem(address.getAddressId().intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2)))
            .andExpect(jsonPath("$.[*].district").value(hasItem(DEFAULT_DISTRICT)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));
    }

    @Test
    @Transactional
    void getAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get the address
        restAddressMockMvc
            .perform(get(ENTITY_API_URL_ID, address.getAddressId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.addressId").value(address.getAddressId().intValue()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.address2").value(DEFAULT_ADDRESS_2))
            .andExpect(jsonPath("$.district").value(DEFAULT_DISTRICT))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION));
    }

    @Test
    @Transactional
    void getAddressesByIdFiltering() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        Long id = address.getAddressId();

        defaultAddressShouldBeFound("addressId.equals=" + id);
        defaultAddressShouldNotBeFound("addressId.notEquals=" + id);

        defaultAddressShouldBeFound("addressId.greaterThanOrEqual=" + id);
        defaultAddressShouldNotBeFound("addressId.greaterThan=" + id);

        defaultAddressShouldBeFound("addressId.lessThanOrEqual=" + id);
        defaultAddressShouldNotBeFound("addressId.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAddressesByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where address equals to DEFAULT_ADDRESS
        defaultAddressShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the addressList where address equals to UPDATED_ADDRESS
        defaultAddressShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAddressesByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultAddressShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the addressList where address equals to UPDATED_ADDRESS
        defaultAddressShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAddressesByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where address is not null
        defaultAddressShouldBeFound("address.specified=true");

        // Get all the addressList where address is null
        defaultAddressShouldNotBeFound("address.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByAddressContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where address contains DEFAULT_ADDRESS
        defaultAddressShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the addressList where address contains UPDATED_ADDRESS
        defaultAddressShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAddressesByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where address does not contain DEFAULT_ADDRESS
        defaultAddressShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the addressList where address does not contain UPDATED_ADDRESS
        defaultAddressShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllAddressesByAddress2IsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where address2 equals to DEFAULT_ADDRESS_2
        defaultAddressShouldBeFound("address2.equals=" + DEFAULT_ADDRESS_2);

        // Get all the addressList where address2 equals to UPDATED_ADDRESS_2
        defaultAddressShouldNotBeFound("address2.equals=" + UPDATED_ADDRESS_2);
    }

    @Test
    @Transactional
    void getAllAddressesByAddress2IsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where address2 in DEFAULT_ADDRESS_2 or UPDATED_ADDRESS_2
        defaultAddressShouldBeFound("address2.in=" + DEFAULT_ADDRESS_2 + "," + UPDATED_ADDRESS_2);

        // Get all the addressList where address2 equals to UPDATED_ADDRESS_2
        defaultAddressShouldNotBeFound("address2.in=" + UPDATED_ADDRESS_2);
    }

    @Test
    @Transactional
    void getAllAddressesByAddress2IsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where address2 is not null
        defaultAddressShouldBeFound("address2.specified=true");

        // Get all the addressList where address2 is null
        defaultAddressShouldNotBeFound("address2.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByAddress2ContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where address2 contains DEFAULT_ADDRESS_2
        defaultAddressShouldBeFound("address2.contains=" + DEFAULT_ADDRESS_2);

        // Get all the addressList where address2 contains UPDATED_ADDRESS_2
        defaultAddressShouldNotBeFound("address2.contains=" + UPDATED_ADDRESS_2);
    }

    @Test
    @Transactional
    void getAllAddressesByAddress2NotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where address2 does not contain DEFAULT_ADDRESS_2
        defaultAddressShouldNotBeFound("address2.doesNotContain=" + DEFAULT_ADDRESS_2);

        // Get all the addressList where address2 does not contain UPDATED_ADDRESS_2
        defaultAddressShouldBeFound("address2.doesNotContain=" + UPDATED_ADDRESS_2);
    }

    @Test
    @Transactional
    void getAllAddressesByDistrictIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where district equals to DEFAULT_DISTRICT
        defaultAddressShouldBeFound("district.equals=" + DEFAULT_DISTRICT);

        // Get all the addressList where district equals to UPDATED_DISTRICT
        defaultAddressShouldNotBeFound("district.equals=" + UPDATED_DISTRICT);
    }

    @Test
    @Transactional
    void getAllAddressesByDistrictIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where district in DEFAULT_DISTRICT or UPDATED_DISTRICT
        defaultAddressShouldBeFound("district.in=" + DEFAULT_DISTRICT + "," + UPDATED_DISTRICT);

        // Get all the addressList where district equals to UPDATED_DISTRICT
        defaultAddressShouldNotBeFound("district.in=" + UPDATED_DISTRICT);
    }

    @Test
    @Transactional
    void getAllAddressesByDistrictIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where district is not null
        defaultAddressShouldBeFound("district.specified=true");

        // Get all the addressList where district is null
        defaultAddressShouldNotBeFound("district.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByDistrictContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where district contains DEFAULT_DISTRICT
        defaultAddressShouldBeFound("district.contains=" + DEFAULT_DISTRICT);

        // Get all the addressList where district contains UPDATED_DISTRICT
        defaultAddressShouldNotBeFound("district.contains=" + UPDATED_DISTRICT);
    }

    @Test
    @Transactional
    void getAllAddressesByDistrictNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where district does not contain DEFAULT_DISTRICT
        defaultAddressShouldNotBeFound("district.doesNotContain=" + DEFAULT_DISTRICT);

        // Get all the addressList where district does not contain UPDATED_DISTRICT
        defaultAddressShouldBeFound("district.doesNotContain=" + UPDATED_DISTRICT);
    }

    @Test
    @Transactional
    void getAllAddressesByPostalCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where postalCode equals to DEFAULT_POSTAL_CODE
        defaultAddressShouldBeFound("postalCode.equals=" + DEFAULT_POSTAL_CODE);

        // Get all the addressList where postalCode equals to UPDATED_POSTAL_CODE
        defaultAddressShouldNotBeFound("postalCode.equals=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllAddressesByPostalCodeIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where postalCode in DEFAULT_POSTAL_CODE or UPDATED_POSTAL_CODE
        defaultAddressShouldBeFound("postalCode.in=" + DEFAULT_POSTAL_CODE + "," + UPDATED_POSTAL_CODE);

        // Get all the addressList where postalCode equals to UPDATED_POSTAL_CODE
        defaultAddressShouldNotBeFound("postalCode.in=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllAddressesByPostalCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where postalCode is not null
        defaultAddressShouldBeFound("postalCode.specified=true");

        // Get all the addressList where postalCode is null
        defaultAddressShouldNotBeFound("postalCode.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByPostalCodeContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where postalCode contains DEFAULT_POSTAL_CODE
        defaultAddressShouldBeFound("postalCode.contains=" + DEFAULT_POSTAL_CODE);

        // Get all the addressList where postalCode contains UPDATED_POSTAL_CODE
        defaultAddressShouldNotBeFound("postalCode.contains=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllAddressesByPostalCodeNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where postalCode does not contain DEFAULT_POSTAL_CODE
        defaultAddressShouldNotBeFound("postalCode.doesNotContain=" + DEFAULT_POSTAL_CODE);

        // Get all the addressList where postalCode does not contain UPDATED_POSTAL_CODE
        defaultAddressShouldBeFound("postalCode.doesNotContain=" + UPDATED_POSTAL_CODE);
    }

    @Test
    @Transactional
    void getAllAddressesByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where phone equals to DEFAULT_PHONE
        defaultAddressShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the addressList where phone equals to UPDATED_PHONE
        defaultAddressShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllAddressesByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultAddressShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the addressList where phone equals to UPDATED_PHONE
        defaultAddressShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllAddressesByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where phone is not null
        defaultAddressShouldBeFound("phone.specified=true");

        // Get all the addressList where phone is null
        defaultAddressShouldNotBeFound("phone.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByPhoneContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where phone contains DEFAULT_PHONE
        defaultAddressShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the addressList where phone contains UPDATED_PHONE
        defaultAddressShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllAddressesByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where phone does not contain DEFAULT_PHONE
        defaultAddressShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the addressList where phone does not contain UPDATED_PHONE
        defaultAddressShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllAddressesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where location equals to DEFAULT_LOCATION
        defaultAddressShouldBeFound("location.equals=" + DEFAULT_LOCATION);

        // Get all the addressList where location equals to UPDATED_LOCATION
        defaultAddressShouldNotBeFound("location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllAddressesByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where location in DEFAULT_LOCATION or UPDATED_LOCATION
        defaultAddressShouldBeFound("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION);

        // Get all the addressList where location equals to UPDATED_LOCATION
        defaultAddressShouldNotBeFound("location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllAddressesByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where location is not null
        defaultAddressShouldBeFound("location.specified=true");

        // Get all the addressList where location is null
        defaultAddressShouldNotBeFound("location.specified=false");
    }

    @Test
    @Transactional
    void getAllAddressesByLocationContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where location contains DEFAULT_LOCATION
        defaultAddressShouldBeFound("location.contains=" + DEFAULT_LOCATION);

        // Get all the addressList where location contains UPDATED_LOCATION
        defaultAddressShouldNotBeFound("location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllAddressesByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        // Get all the addressList where location does not contain DEFAULT_LOCATION
        defaultAddressShouldNotBeFound("location.doesNotContain=" + DEFAULT_LOCATION);

        // Get all the addressList where location does not contain UPDATED_LOCATION
        defaultAddressShouldBeFound("location.doesNotContain=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllAddressesByCityIsEqualToSomething() throws Exception {
        City city;
        if (TestUtil.findAll(em, City.class).isEmpty()) {
            addressRepository.saveAndFlush(address);
            city = CityResourceIT.createEntity(em);
        } else {
            city = TestUtil.findAll(em, City.class).get(0);
        }
        em.persist(city);
        em.flush();
        address.setCity(city);
        addressRepository.saveAndFlush(address);
        Long cityId = city.getCityId();

        // Get all the addressList where city equals to cityId
        defaultAddressShouldBeFound("cityId.equals=" + cityId);

        // Get all the addressList where city equals to (cityId + 1)
        defaultAddressShouldNotBeFound("cityId.equals=" + (cityId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAddressShouldBeFound(String filter) throws Exception {
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=addressId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].addressId").value(hasItem(address.getAddressId().intValue())))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].address2").value(hasItem(DEFAULT_ADDRESS_2)))
            .andExpect(jsonPath("$.[*].district").value(hasItem(DEFAULT_DISTRICT)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));

        // Check, that the count call also returns 1
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=addressId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAddressShouldNotBeFound(String filter) throws Exception {
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "?sort=addressId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAddressMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=addressId,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAddress() throws Exception {
        // Get the address
        restAddressMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address
        Address updatedAddress = addressRepository.findById(address.getAddressId()).get();
        // Disconnect from session so that the updates on updatedAddress are not directly saved in db
        em.detach(updatedAddress);
        updatedAddress
            .address(UPDATED_ADDRESS)
            .address2(UPDATED_ADDRESS_2)
            .district(UPDATED_DISTRICT)
            .postalCode(UPDATED_POSTAL_CODE)
            .phone(UPDATED_PHONE)
            .location(UPDATED_LOCATION);
        AddressDTO addressDTO = addressMapper.toDto(updatedAddress);

        restAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, addressDTO.getAddressId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testAddress.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testAddress.getDistrict()).isEqualTo(UPDATED_DISTRICT);
        assertThat(testAddress.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testAddress.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testAddress.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void putNonExistingAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setAddressId(count.incrementAndGet());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, addressDTO.getAddressId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setAddressId(count.incrementAndGet());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setAddressId(count.incrementAndGet());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(addressDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAddressWithPatch() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address using partial update
        Address partialUpdatedAddress = new Address();
        partialUpdatedAddress.setAddressId(address.getAddressId());

        partialUpdatedAddress.address2(UPDATED_ADDRESS_2).district(UPDATED_DISTRICT).postalCode(UPDATED_POSTAL_CODE).phone(UPDATED_PHONE);

        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAddress.getAddressId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddress))
            )
            .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testAddress.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testAddress.getDistrict()).isEqualTo(UPDATED_DISTRICT);
        assertThat(testAddress.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testAddress.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testAddress.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    void fullUpdateAddressWithPatch() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeUpdate = addressRepository.findAll().size();

        // Update the address using partial update
        Address partialUpdatedAddress = new Address();
        partialUpdatedAddress.setAddressId(address.getAddressId());

        partialUpdatedAddress
            .address(UPDATED_ADDRESS)
            .address2(UPDATED_ADDRESS_2)
            .district(UPDATED_DISTRICT)
            .postalCode(UPDATED_POSTAL_CODE)
            .phone(UPDATED_PHONE)
            .location(UPDATED_LOCATION);

        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAddress.getAddressId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAddress))
            )
            .andExpect(status().isOk());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
        Address testAddress = addressList.get(addressList.size() - 1);
        assertThat(testAddress.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testAddress.getAddress2()).isEqualTo(UPDATED_ADDRESS_2);
        assertThat(testAddress.getDistrict()).isEqualTo(UPDATED_DISTRICT);
        assertThat(testAddress.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testAddress.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testAddress.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void patchNonExistingAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setAddressId(count.incrementAndGet());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, addressDTO.getAddressId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setAddressId(count.incrementAndGet());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAddress() throws Exception {
        int databaseSizeBeforeUpdate = addressRepository.findAll().size();
        address.setAddressId(count.incrementAndGet());

        // Create the Address
        AddressDTO addressDTO = addressMapper.toDto(address);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAddressMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(addressDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Address in the database
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAddress() throws Exception {
        // Initialize the database
        addressRepository.saveAndFlush(address);

        int databaseSizeBeforeDelete = addressRepository.findAll().size();

        // Delete the address
        restAddressMockMvc
            .perform(delete(ENTITY_API_URL_ID, address.getAddressId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Address> addressList = addressRepository.findAll();
        assertThat(addressList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
