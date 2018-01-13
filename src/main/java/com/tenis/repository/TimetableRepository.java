package com.tenis.repository;

import com.tenis.domain.Timetable;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Timetable entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TimetableRepository extends JpaRepository<Timetable, Long> {

}
