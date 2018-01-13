package com.tenis.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tenis.domain.Timetable;

import com.tenis.repository.TimetableRepository;
import com.tenis.web.rest.errors.BadRequestAlertException;
import com.tenis.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Timetable.
 */
@RestController
@RequestMapping("/api")
public class TimetableResource {

    private final Logger log = LoggerFactory.getLogger(TimetableResource.class);

    private static final String ENTITY_NAME = "timetable";

    private final TimetableRepository timetableRepository;

    public TimetableResource(TimetableRepository timetableRepository) {
        this.timetableRepository = timetableRepository;
    }

    /**
     * POST  /timetables : Create a new timetable.
     *
     * @param timetable the timetable to create
     * @return the ResponseEntity with status 201 (Created) and with body the new timetable, or with status 400 (Bad Request) if the timetable has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/timetables")
    @Timed
    public ResponseEntity<Timetable> createTimetable(@RequestBody Timetable timetable) throws URISyntaxException {
        log.debug("REST request to save Timetable : {}", timetable);
        if (timetable.getId() != null) {
            throw new BadRequestAlertException("A new timetable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Timetable result = timetableRepository.save(timetable);
        return ResponseEntity.created(new URI("/api/timetables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /timetables : Updates an existing timetable.
     *
     * @param timetable the timetable to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated timetable,
     * or with status 400 (Bad Request) if the timetable is not valid,
     * or with status 500 (Internal Server Error) if the timetable couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/timetables")
    @Timed
    public ResponseEntity<Timetable> updateTimetable(@RequestBody Timetable timetable) throws URISyntaxException {
        log.debug("REST request to update Timetable : {}", timetable);
        if (timetable.getId() == null) {
            return createTimetable(timetable);
        }
        Timetable result = timetableRepository.save(timetable);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, timetable.getId().toString()))
            .body(result);
    }

    /**
     * GET  /timetables : get all the timetables.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of timetables in body
     */
    @GetMapping("/timetables")
    @Timed
    public List<Timetable> getAllTimetables() {
        log.debug("REST request to get all Timetables");
        return timetableRepository.findAll();
        }

    /**
     * GET  /timetables/:id : get the "id" timetable.
     *
     * @param id the id of the timetable to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the timetable, or with status 404 (Not Found)
     */
    @GetMapping("/timetables/{id}")
    @Timed
    public ResponseEntity<Timetable> getTimetable(@PathVariable Long id) {
        log.debug("REST request to get Timetable : {}", id);
        Timetable timetable = timetableRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(timetable));
    }

    /**
     * DELETE  /timetables/:id : delete the "id" timetable.
     *
     * @param id the id of the timetable to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/timetables/{id}")
    @Timed
    public ResponseEntity<Void> deleteTimetable(@PathVariable Long id) {
        log.debug("REST request to delete Timetable : {}", id);
        timetableRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
