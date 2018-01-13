package com.tenis.web.rest;

import com.tenis.TenisApp;

import com.tenis.domain.Timetable;
import com.tenis.repository.TimetableRepository;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.tenis.web.rest.TestUtil.sameInstant;
import static com.tenis.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TimetableResource REST controller.
 *
 * @see TimetableResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TenisApp.class)
public class TimetableResourceIntTest {

    private static final ZonedDateTime DEFAULT_START_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_END_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private TimetableRepository timetableRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restTimetableMockMvc;

    private Timetable timetable;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TimetableResource timetableResource = new TimetableResource(timetableRepository);
        this.restTimetableMockMvc = MockMvcBuilders.standaloneSetup(timetableResource)
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
    public static Timetable createEntity(EntityManager em) {
        Timetable timetable = new Timetable()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return timetable;
    }

    @Before
    public void initTest() {
        timetable = createEntity(em);
    }

    @Test
    @Transactional
    public void createTimetable() throws Exception {
        int databaseSizeBeforeCreate = timetableRepository.findAll().size();

        // Create the Timetable
        restTimetableMockMvc.perform(post("/api/timetables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timetable)))
            .andExpect(status().isCreated());

        // Validate the Timetable in the database
        List<Timetable> timetableList = timetableRepository.findAll();
        assertThat(timetableList).hasSize(databaseSizeBeforeCreate + 1);
        Timetable testTimetable = timetableList.get(timetableList.size() - 1);
        assertThat(testTimetable.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testTimetable.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void createTimetableWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = timetableRepository.findAll().size();

        // Create the Timetable with an existing ID
        timetable.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTimetableMockMvc.perform(post("/api/timetables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timetable)))
            .andExpect(status().isBadRequest());

        // Validate the Timetable in the database
        List<Timetable> timetableList = timetableRepository.findAll();
        assertThat(timetableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllTimetables() throws Exception {
        // Initialize the database
        timetableRepository.saveAndFlush(timetable);

        // Get all the timetableList
        restTimetableMockMvc.perform(get("/api/timetables?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(timetable.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(sameInstant(DEFAULT_START_DATE))))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(sameInstant(DEFAULT_END_DATE))));
    }

    @Test
    @Transactional
    public void getTimetable() throws Exception {
        // Initialize the database
        timetableRepository.saveAndFlush(timetable);

        // Get the timetable
        restTimetableMockMvc.perform(get("/api/timetables/{id}", timetable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(timetable.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(sameInstant(DEFAULT_START_DATE)))
            .andExpect(jsonPath("$.endDate").value(sameInstant(DEFAULT_END_DATE)));
    }

    @Test
    @Transactional
    public void getNonExistingTimetable() throws Exception {
        // Get the timetable
        restTimetableMockMvc.perform(get("/api/timetables/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTimetable() throws Exception {
        // Initialize the database
        timetableRepository.saveAndFlush(timetable);
        int databaseSizeBeforeUpdate = timetableRepository.findAll().size();

        // Update the timetable
        Timetable updatedTimetable = timetableRepository.findOne(timetable.getId());
        // Disconnect from session so that the updates on updatedTimetable are not directly saved in db
        em.detach(updatedTimetable);
        updatedTimetable
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restTimetableMockMvc.perform(put("/api/timetables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTimetable)))
            .andExpect(status().isOk());

        // Validate the Timetable in the database
        List<Timetable> timetableList = timetableRepository.findAll();
        assertThat(timetableList).hasSize(databaseSizeBeforeUpdate);
        Timetable testTimetable = timetableList.get(timetableList.size() - 1);
        assertThat(testTimetable.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testTimetable.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingTimetable() throws Exception {
        int databaseSizeBeforeUpdate = timetableRepository.findAll().size();

        // Create the Timetable

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restTimetableMockMvc.perform(put("/api/timetables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(timetable)))
            .andExpect(status().isCreated());

        // Validate the Timetable in the database
        List<Timetable> timetableList = timetableRepository.findAll();
        assertThat(timetableList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteTimetable() throws Exception {
        // Initialize the database
        timetableRepository.saveAndFlush(timetable);
        int databaseSizeBeforeDelete = timetableRepository.findAll().size();

        // Get the timetable
        restTimetableMockMvc.perform(delete("/api/timetables/{id}", timetable.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Timetable> timetableList = timetableRepository.findAll();
        assertThat(timetableList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Timetable.class);
        Timetable timetable1 = new Timetable();
        timetable1.setId(1L);
        Timetable timetable2 = new Timetable();
        timetable2.setId(timetable1.getId());
        assertThat(timetable1).isEqualTo(timetable2);
        timetable2.setId(2L);
        assertThat(timetable1).isNotEqualTo(timetable2);
        timetable1.setId(null);
        assertThat(timetable1).isNotEqualTo(timetable2);
    }
}
