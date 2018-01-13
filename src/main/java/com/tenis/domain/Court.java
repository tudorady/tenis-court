package com.tenis.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Court.
 */
@Entity
@Table(name = "court")
public class Court implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "court")
    @JsonIgnore
    private Set<Reservation> reservations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Court name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public Court reservations(Set<Reservation> reservations) {
        this.reservations = reservations;
        return this;
    }

    public Court addReservation(Reservation reservation) {
        this.reservations.add(reservation);
        reservation.setCourt(this);
        return this;
    }

    public Court removeReservation(Reservation reservation) {
        this.reservations.remove(reservation);
        reservation.setCourt(null);
        return this;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Court court = (Court) o;
        if (court.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), court.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Court{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
