package by.diplom.clinic.web.rest;

import by.diplom.clinic.ClinicApp;
import by.diplom.clinic.domain.Specialty;
import by.diplom.clinic.repository.SpecialtyRepository;
import by.diplom.clinic.service.SpecialtyService;
import by.diplom.clinic.service.dto.SpecialtyDTO;
import by.diplom.clinic.service.mapper.SpecialtyMapper;

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
 * Integration tests for the {@link SpecialtyResource} REST controller.
 */
@SpringBootTest(classes = ClinicApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class SpecialtyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private SpecialtyMapper specialtyMapper;

    @Autowired
    private SpecialtyService specialtyService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecialtyMockMvc;

    private Specialty specialty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialty createEntity(EntityManager em) {
        Specialty specialty = new Specialty()
            .name(DEFAULT_NAME);
        return specialty;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialty createUpdatedEntity(EntityManager em) {
        Specialty specialty = new Specialty()
            .name(UPDATED_NAME);
        return specialty;
    }

    @BeforeEach
    public void initTest() {
        specialty = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllSpecialties() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList
        restSpecialtyMockMvc.perform(get("/api/specialties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialty.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getSpecialty() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get the specialty
        restSpecialtyMockMvc.perform(get("/api/specialties/{id}", specialty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specialty.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingSpecialty() throws Exception {
        // Get the specialty
        restSpecialtyMockMvc.perform(get("/api/specialties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}
