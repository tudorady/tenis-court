package com.tenis.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.tenis.domain.Court;

import com.tenis.repository.CourtRepository;
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
 * REST controller for managing Court.
 */
@RestController
@RequestMapping("/api")
public class CourtResource {

    private final Logger log = LoggerFactory.getLogger(CourtResource.class);

    private static final String ENTITY_NAME = "court";

    private final CourtRepository courtRepository;

    public CourtResource(CourtRepository courtRepository) {
        this.courtRepository = courtRepository;
    }

    /**
     * POST  /courts : Create a new court.
     *
     * @param court the court to create
     * @return the ResponseEntity with status 201 (Created) and with body the new court, or with status 400 (Bad Request) if the court has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/courts")
    @Timed
    public ResponseEntity<Court> createCourt(@RequestBody Court court) throws URISyntaxException {
        log.debug("REST request to save Court : {}", court);
        if (court.getId() != null) {
            throw new BadRequestAlertException("A new court cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Court result = courtRepository.save(court);
        return ResponseEntity.created(new URI("/api/courts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /courts : Updates an existing court.
     *
     * @param court the court to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated court,
     * or with status 400 (Bad Request) if the court is not valid,
     * or with status 500 (Internal Server Error) if the court couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/courts")
    @Timed
    public ResponseEntity<Court> updateCourt(@RequestBody Court court) throws URISyntaxException {
        log.debug("REST request to update Court : {}", court);
        if (court.getId() == null) {
            return createCourt(court);
        }
        Court result = courtRepository.save(court);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, court.getId().toString()))
            .body(result);
    }

    /**
     * GET  /courts : get all the courts.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of courts in body
     */
    @GetMapping("/courts")
    @Timed
    public List<Court> getAllCourts() {
        log.debug("REST request to get all Courts");
        return courtRepository.findAll();
        }

    /**
     * GET  /courts/:id : get the "id" court.
     *
     * @param id the id of the court to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the court, or with status 404 (Not Found)
     */
    @GetMapping("/courts/{id}")
    @Timed
    public ResponseEntity<Court> getCourt(@PathVariable Long id) {
        log.debug("REST request to get Court : {}", id);
        Court court = courtRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(court));
    }

    /**
     * DELETE  /courts/:id : delete the "id" court.
     *
     * @param id the id of the court to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/courts/{id}")
    @Timed
    public ResponseEntity<Void> deleteCourt(@PathVariable Long id) {
        log.debug("REST request to delete Court : {}", id);
        courtRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
