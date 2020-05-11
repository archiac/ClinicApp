package by.diplom.clinic.web.rest;

import by.diplom.clinic.ClinicApp;
import by.diplom.clinic.domain.Doctor;
import by.diplom.clinic.domain.Specialty;
import by.diplom.clinic.repository.DoctorRepository;
import by.diplom.clinic.service.DoctorService;
import by.diplom.clinic.service.dto.DoctorDTO;
import by.diplom.clinic.service.mapper.DoctorMapper;
import by.diplom.clinic.service.dto.DoctorCriteria;
import by.diplom.clinic.service.DoctorQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DoctorResource} REST controller.
 */
@SpringBootTest(classes = ClinicApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class DoctorResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SURNAME = "AAAAAAAAAA";
    private static final String UPDATED_SURNAME = "BBBBBBBBBB";

    private static final String DEFAULT_PATRONYMIC = "AAAAAAAAAA";
    private static final String UPDATED_PATRONYMIC = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final Integer DEFAULT_TICKETS = 1;
    private static final Integer UPDATED_TICKETS = 2;
    private static final Integer SMALLER_TICKETS = 1 - 1;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private DoctorQueryService doctorQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDoctorMockMvc;

    private Doctor doctor;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doctor createEntity(EntityManager em) {
        Doctor doctor = new Doctor()
            .name(DEFAULT_NAME)
            .surname(DEFAULT_SURNAME)
            .patronymic(DEFAULT_PATRONYMIC)
            .phone(DEFAULT_PHONE)
            .tickets(DEFAULT_TICKETS);
        return doctor;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Doctor createUpdatedEntity(EntityManager em) {
        Doctor doctor = new Doctor()
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .patronymic(UPDATED_PATRONYMIC)
            .phone(UPDATED_PHONE)
            .tickets(UPDATED_TICKETS);
        return doctor;
    }

    @BeforeEach
    public void initTest() {
        doctor = createEntity(em);
    }

    @Test
    @Transactional
    public void createDoctor() throws Exception {
        int databaseSizeBeforeCreate = doctorRepository.findAll().size();

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);
        restDoctorMockMvc.perform(post("/api/doctors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(doctorDTO)))
            .andExpect(status().isCreated());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeCreate + 1);
        Doctor testDoctor = doctorList.get(doctorList.size() - 1);
        assertThat(testDoctor.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testDoctor.getSurname()).isEqualTo(DEFAULT_SURNAME);
        assertThat(testDoctor.getPatronymic()).isEqualTo(DEFAULT_PATRONYMIC);
        assertThat(testDoctor.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testDoctor.getTickets()).isEqualTo(DEFAULT_TICKETS);
    }

    @Test
    @Transactional
    public void createDoctorWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = doctorRepository.findAll().size();

        // Create the Doctor with an existing ID
        doctor.setId(1L);
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDoctorMockMvc.perform(post("/api/doctors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(doctorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllDoctors() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList
        restDoctorMockMvc.perform(get("/api/doctors?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].patronymic").value(hasItem(DEFAULT_PATRONYMIC)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].tickets").value(hasItem(DEFAULT_TICKETS)));
    }
    
    @Test
    @Transactional
    public void getDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get the doctor
        restDoctorMockMvc.perform(get("/api/doctors/{id}", doctor.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(doctor.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.surname").value(DEFAULT_SURNAME))
            .andExpect(jsonPath("$.patronymic").value(DEFAULT_PATRONYMIC))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.tickets").value(DEFAULT_TICKETS));
    }


    @Test
    @Transactional
    public void getDoctorsByIdFiltering() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        Long id = doctor.getId();

        defaultDoctorShouldBeFound("id.equals=" + id);
        defaultDoctorShouldNotBeFound("id.notEquals=" + id);

        defaultDoctorShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDoctorShouldNotBeFound("id.greaterThan=" + id);

        defaultDoctorShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDoctorShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllDoctorsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where name equals to DEFAULT_NAME
        defaultDoctorShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the doctorList where name equals to UPDATED_NAME
        defaultDoctorShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDoctorsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where name not equals to DEFAULT_NAME
        defaultDoctorShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the doctorList where name not equals to UPDATED_NAME
        defaultDoctorShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDoctorsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where name in DEFAULT_NAME or UPDATED_NAME
        defaultDoctorShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the doctorList where name equals to UPDATED_NAME
        defaultDoctorShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDoctorsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where name is not null
        defaultDoctorShouldBeFound("name.specified=true");

        // Get all the doctorList where name is null
        defaultDoctorShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllDoctorsByNameContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where name contains DEFAULT_NAME
        defaultDoctorShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the doctorList where name contains UPDATED_NAME
        defaultDoctorShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllDoctorsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where name does not contain DEFAULT_NAME
        defaultDoctorShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the doctorList where name does not contain UPDATED_NAME
        defaultDoctorShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllDoctorsBySurnameIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where surname equals to DEFAULT_SURNAME
        defaultDoctorShouldBeFound("surname.equals=" + DEFAULT_SURNAME);

        // Get all the doctorList where surname equals to UPDATED_SURNAME
        defaultDoctorShouldNotBeFound("surname.equals=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    public void getAllDoctorsBySurnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where surname not equals to DEFAULT_SURNAME
        defaultDoctorShouldNotBeFound("surname.notEquals=" + DEFAULT_SURNAME);

        // Get all the doctorList where surname not equals to UPDATED_SURNAME
        defaultDoctorShouldBeFound("surname.notEquals=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    public void getAllDoctorsBySurnameIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where surname in DEFAULT_SURNAME or UPDATED_SURNAME
        defaultDoctorShouldBeFound("surname.in=" + DEFAULT_SURNAME + "," + UPDATED_SURNAME);

        // Get all the doctorList where surname equals to UPDATED_SURNAME
        defaultDoctorShouldNotBeFound("surname.in=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    public void getAllDoctorsBySurnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where surname is not null
        defaultDoctorShouldBeFound("surname.specified=true");

        // Get all the doctorList where surname is null
        defaultDoctorShouldNotBeFound("surname.specified=false");
    }
                @Test
    @Transactional
    public void getAllDoctorsBySurnameContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where surname contains DEFAULT_SURNAME
        defaultDoctorShouldBeFound("surname.contains=" + DEFAULT_SURNAME);

        // Get all the doctorList where surname contains UPDATED_SURNAME
        defaultDoctorShouldNotBeFound("surname.contains=" + UPDATED_SURNAME);
    }

    @Test
    @Transactional
    public void getAllDoctorsBySurnameNotContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where surname does not contain DEFAULT_SURNAME
        defaultDoctorShouldNotBeFound("surname.doesNotContain=" + DEFAULT_SURNAME);

        // Get all the doctorList where surname does not contain UPDATED_SURNAME
        defaultDoctorShouldBeFound("surname.doesNotContain=" + UPDATED_SURNAME);
    }


    @Test
    @Transactional
    public void getAllDoctorsByPatronymicIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where patronymic equals to DEFAULT_PATRONYMIC
        defaultDoctorShouldBeFound("patronymic.equals=" + DEFAULT_PATRONYMIC);

        // Get all the doctorList where patronymic equals to UPDATED_PATRONYMIC
        defaultDoctorShouldNotBeFound("patronymic.equals=" + UPDATED_PATRONYMIC);
    }

    @Test
    @Transactional
    public void getAllDoctorsByPatronymicIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where patronymic not equals to DEFAULT_PATRONYMIC
        defaultDoctorShouldNotBeFound("patronymic.notEquals=" + DEFAULT_PATRONYMIC);

        // Get all the doctorList where patronymic not equals to UPDATED_PATRONYMIC
        defaultDoctorShouldBeFound("patronymic.notEquals=" + UPDATED_PATRONYMIC);
    }

    @Test
    @Transactional
    public void getAllDoctorsByPatronymicIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where patronymic in DEFAULT_PATRONYMIC or UPDATED_PATRONYMIC
        defaultDoctorShouldBeFound("patronymic.in=" + DEFAULT_PATRONYMIC + "," + UPDATED_PATRONYMIC);

        // Get all the doctorList where patronymic equals to UPDATED_PATRONYMIC
        defaultDoctorShouldNotBeFound("patronymic.in=" + UPDATED_PATRONYMIC);
    }

    @Test
    @Transactional
    public void getAllDoctorsByPatronymicIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where patronymic is not null
        defaultDoctorShouldBeFound("patronymic.specified=true");

        // Get all the doctorList where patronymic is null
        defaultDoctorShouldNotBeFound("patronymic.specified=false");
    }
                @Test
    @Transactional
    public void getAllDoctorsByPatronymicContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where patronymic contains DEFAULT_PATRONYMIC
        defaultDoctorShouldBeFound("patronymic.contains=" + DEFAULT_PATRONYMIC);

        // Get all the doctorList where patronymic contains UPDATED_PATRONYMIC
        defaultDoctorShouldNotBeFound("patronymic.contains=" + UPDATED_PATRONYMIC);
    }

    @Test
    @Transactional
    public void getAllDoctorsByPatronymicNotContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where patronymic does not contain DEFAULT_PATRONYMIC
        defaultDoctorShouldNotBeFound("patronymic.doesNotContain=" + DEFAULT_PATRONYMIC);

        // Get all the doctorList where patronymic does not contain UPDATED_PATRONYMIC
        defaultDoctorShouldBeFound("patronymic.doesNotContain=" + UPDATED_PATRONYMIC);
    }


    @Test
    @Transactional
    public void getAllDoctorsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where phone equals to DEFAULT_PHONE
        defaultDoctorShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the doctorList where phone equals to UPDATED_PHONE
        defaultDoctorShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllDoctorsByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where phone not equals to DEFAULT_PHONE
        defaultDoctorShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the doctorList where phone not equals to UPDATED_PHONE
        defaultDoctorShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllDoctorsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultDoctorShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the doctorList where phone equals to UPDATED_PHONE
        defaultDoctorShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllDoctorsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where phone is not null
        defaultDoctorShouldBeFound("phone.specified=true");

        // Get all the doctorList where phone is null
        defaultDoctorShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllDoctorsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where phone contains DEFAULT_PHONE
        defaultDoctorShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the doctorList where phone contains UPDATED_PHONE
        defaultDoctorShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllDoctorsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where phone does not contain DEFAULT_PHONE
        defaultDoctorShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the doctorList where phone does not contain UPDATED_PHONE
        defaultDoctorShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllDoctorsByTicketsIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where tickets equals to DEFAULT_TICKETS
        defaultDoctorShouldBeFound("tickets.equals=" + DEFAULT_TICKETS);

        // Get all the doctorList where tickets equals to UPDATED_TICKETS
        defaultDoctorShouldNotBeFound("tickets.equals=" + UPDATED_TICKETS);
    }

    @Test
    @Transactional
    public void getAllDoctorsByTicketsIsNotEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where tickets not equals to DEFAULT_TICKETS
        defaultDoctorShouldNotBeFound("tickets.notEquals=" + DEFAULT_TICKETS);

        // Get all the doctorList where tickets not equals to UPDATED_TICKETS
        defaultDoctorShouldBeFound("tickets.notEquals=" + UPDATED_TICKETS);
    }

    @Test
    @Transactional
    public void getAllDoctorsByTicketsIsInShouldWork() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where tickets in DEFAULT_TICKETS or UPDATED_TICKETS
        defaultDoctorShouldBeFound("tickets.in=" + DEFAULT_TICKETS + "," + UPDATED_TICKETS);

        // Get all the doctorList where tickets equals to UPDATED_TICKETS
        defaultDoctorShouldNotBeFound("tickets.in=" + UPDATED_TICKETS);
    }

    @Test
    @Transactional
    public void getAllDoctorsByTicketsIsNullOrNotNull() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where tickets is not null
        defaultDoctorShouldBeFound("tickets.specified=true");

        // Get all the doctorList where tickets is null
        defaultDoctorShouldNotBeFound("tickets.specified=false");
    }

    @Test
    @Transactional
    public void getAllDoctorsByTicketsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where tickets is greater than or equal to DEFAULT_TICKETS
        defaultDoctorShouldBeFound("tickets.greaterThanOrEqual=" + DEFAULT_TICKETS);

        // Get all the doctorList where tickets is greater than or equal to UPDATED_TICKETS
        defaultDoctorShouldNotBeFound("tickets.greaterThanOrEqual=" + UPDATED_TICKETS);
    }

    @Test
    @Transactional
    public void getAllDoctorsByTicketsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where tickets is less than or equal to DEFAULT_TICKETS
        defaultDoctorShouldBeFound("tickets.lessThanOrEqual=" + DEFAULT_TICKETS);

        // Get all the doctorList where tickets is less than or equal to SMALLER_TICKETS
        defaultDoctorShouldNotBeFound("tickets.lessThanOrEqual=" + SMALLER_TICKETS);
    }

    @Test
    @Transactional
    public void getAllDoctorsByTicketsIsLessThanSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where tickets is less than DEFAULT_TICKETS
        defaultDoctorShouldNotBeFound("tickets.lessThan=" + DEFAULT_TICKETS);

        // Get all the doctorList where tickets is less than UPDATED_TICKETS
        defaultDoctorShouldBeFound("tickets.lessThan=" + UPDATED_TICKETS);
    }

    @Test
    @Transactional
    public void getAllDoctorsByTicketsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        // Get all the doctorList where tickets is greater than DEFAULT_TICKETS
        defaultDoctorShouldNotBeFound("tickets.greaterThan=" + DEFAULT_TICKETS);

        // Get all the doctorList where tickets is greater than SMALLER_TICKETS
        defaultDoctorShouldBeFound("tickets.greaterThan=" + SMALLER_TICKETS);
    }


    @Test
    @Transactional
    public void getAllDoctorsBySpecialtyIsEqualToSomething() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);
        Specialty specialty = SpecialtyResourceIT.createEntity(em);
        em.persist(specialty);
        em.flush();
        doctor.setSpecialty(specialty);
        doctorRepository.saveAndFlush(doctor);
        Long specialtyId = specialty.getId();

        // Get all the doctorList where specialty equals to specialtyId
        defaultDoctorShouldBeFound("specialtyId.equals=" + specialtyId);

        // Get all the doctorList where specialty equals to specialtyId + 1
        defaultDoctorShouldNotBeFound("specialtyId.equals=" + (specialtyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDoctorShouldBeFound(String filter) throws Exception {
        restDoctorMockMvc.perform(get("/api/doctors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(doctor.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].surname").value(hasItem(DEFAULT_SURNAME)))
            .andExpect(jsonPath("$.[*].patronymic").value(hasItem(DEFAULT_PATRONYMIC)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].tickets").value(hasItem(DEFAULT_TICKETS)));

        // Check, that the count call also returns 1
        restDoctorMockMvc.perform(get("/api/doctors/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDoctorShouldNotBeFound(String filter) throws Exception {
        restDoctorMockMvc.perform(get("/api/doctors?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDoctorMockMvc.perform(get("/api/doctors/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDoctor() throws Exception {
        // Get the doctor
        restDoctorMockMvc.perform(get("/api/doctors/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();

        // Update the doctor
        Doctor updatedDoctor = doctorRepository.findById(doctor.getId()).get();
        // Disconnect from session so that the updates on updatedDoctor are not directly saved in db
        em.detach(updatedDoctor);
        updatedDoctor
            .name(UPDATED_NAME)
            .surname(UPDATED_SURNAME)
            .patronymic(UPDATED_PATRONYMIC)
            .phone(UPDATED_PHONE)
            .tickets(UPDATED_TICKETS);
        DoctorDTO doctorDTO = doctorMapper.toDto(updatedDoctor);

        restDoctorMockMvc.perform(put("/api/doctors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(doctorDTO)))
            .andExpect(status().isOk());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);
        Doctor testDoctor = doctorList.get(doctorList.size() - 1);
        assertThat(testDoctor.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testDoctor.getSurname()).isEqualTo(UPDATED_SURNAME);
        assertThat(testDoctor.getPatronymic()).isEqualTo(UPDATED_PATRONYMIC);
        assertThat(testDoctor.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testDoctor.getTickets()).isEqualTo(UPDATED_TICKETS);
    }

    @Test
    @Transactional
    public void updateNonExistingDoctor() throws Exception {
        int databaseSizeBeforeUpdate = doctorRepository.findAll().size();

        // Create the Doctor
        DoctorDTO doctorDTO = doctorMapper.toDto(doctor);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDoctorMockMvc.perform(put("/api/doctors")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(doctorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Doctor in the database
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteDoctor() throws Exception {
        // Initialize the database
        doctorRepository.saveAndFlush(doctor);

        int databaseSizeBeforeDelete = doctorRepository.findAll().size();

        // Delete the doctor
        restDoctorMockMvc.perform(delete("/api/doctors/{id}", doctor.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Doctor> doctorList = doctorRepository.findAll();
        assertThat(doctorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
