package com.tenis.web.rest;

import com.tenis.TenisApp;

import com.tenis.domain.Court;
import com.tenis.repository.CourtRepository;
import com.tenis.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static com.tenis.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CourtResource REST controller.
 *
 * @see CourtResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TenisApp.class)
public class CourtResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private CourtRepository courtRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCourtMockMvc;

    private Court court;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CourtResource courtResource = new CourtResource(courtRepository);
        this.restCourtMockMvc = MockMvcBuilders.standaloneSetup(courtResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Court createEntity(EntityManager em) {
        Court court = new Court()
            .name(DEFAULT_NAME);
        return court;
    }

    @Before
    public void initTest() {
        court = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourt() throws Exception {
        int databaseSizeBeforeCreate = courtRepository.findAll().size();

        // Create the Court
        restCourtMockMvc.perform(post("/api/courts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(court)))
            .andExpect(status().isCreated());

        // Validate the Court in the database
        List<Court> courtList = courtRepository.findAll();
        assertThat(courtList).hasSize(databaseSizeBeforeCreate + 1);
        Court testCourt = courtList.get(courtList.size() - 1);
        assertThat(testCourt.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createCourtWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courtRepository.findAll().size();

        // Create the Court with an existing ID
        court.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourtMockMvc.perform(post("/api/courts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(court)))
            .andExpect(status().isBadRequest());

        // Validate the Court in the database
        List<Court> courtList = courtRepository.findAll();
        assertThat(courtList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCourts() throws Exception {
        // Initialize the database
        courtRepository.saveAndFlush(court);

        // Get all the courtList
        restCourtMockMvc.perform(get("/api/courts?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(court.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCourt() throws Exception {
        // Initialize the database
        courtRepository.saveAndFlush(court);

        // Get the court
        restCourtMockMvc.perform(get("/api/courts/{id}", court.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(court.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCourt() throws Exception {
        // Get the court
        restCourtMockMvc.perform(get("/api/courts/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourt() throws Exception {
        // Initialize the database
        courtRepository.saveAndFlush(court);
        int databaseSizeBeforeUpdate = courtRepository.findAll().size();

        // Update the court
        Court updatedCourt = courtRepository.findOne(court.getId());
        // Disconnect from session so that the updates on updatedCourt are not directly saved in db
        em.detach(updatedCourt);
        updatedCourt
            .name(UPDATED_NAME);

        restCourtMockMvc.perform(put("/api/courts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCourt)))
            .andExpect(status().isOk());

        // Validate the Court in the database
        List<Court> courtList = courtRepository.findAll();
        assertThat(courtList).hasSize(databaseSizeBeforeUpdate);
        Court testCourt = courtList.get(courtList.size() - 1);
        assertThat(testCourt.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingCourt() throws Exception {
        int databaseSizeBeforeUpdate = courtRepository.findAll().size();

        // Create the Court

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCourtMockMvc.perform(put("/api/courts")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(court)))
            .andExpect(status().isCreated());

        // Validate the Court in the database
        List<Court> courtList = courtRepository.findAll();
        assertThat(courtList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCourt() throws Exception {
        // Initialize the database
        courtRepository.saveAndFlush(court);
        int databaseSizeBeforeDelete = courtRepository.findAll().size();

        // Get the court
        restCourtMockMvc.perform(delete("/api/courts/{id}", court.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Court> courtList = courtRepository.findAll();
        assertThat(courtList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Court.class);
        Court court1 = new Court();
        court1.setId(1L);
        Court court2 = new Court();
        court2.setId(court1.getId());
        assertThat(court1).isEqualTo(court2);
        court2.setId(2L);
        assertThat(court1).isNotEqualTo(court2);
        court1.setId(null);
        assertThat(court1).isNotEqualTo(court2);
    }
}
