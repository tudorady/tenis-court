package com.tenis.repository;

import com.tenis.domain.Court;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Court entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CourtRepository extends JpaRepository<Court, Long> {

}
