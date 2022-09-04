package com.jana.bms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Actor.
 */
@Entity
@Table(name = "actor")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Actor implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "actor_id", nullable = false)
    private Long actorId;

    @NotNull
    @Size(max = 45)
    @Column(name = "first_name", length = 45, nullable = false)
    private String firstName;

    @NotNull
    @Size(max = 45)
    @Column(name = "last_name", length = 45, nullable = false)
    private String lastName;

    @ManyToMany(mappedBy = "actors")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "language", "actors", "genres" }, allowSetters = true)
    private Set<Movie> movies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getActorId() {
        return this.actorId;
    }

    public Actor actorId(Long actorId) {
        this.setActorId(actorId);
        return this;
    }

    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Actor firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public Actor lastName(String lastName) {
        this.setLastName(lastName);
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Movie> getMovies() {
        return this.movies;
    }

    public void setMovies(Set<Movie> movies) {
        if (this.movies != null) {
            this.movies.forEach(i -> i.removeActor(this));
        }
        if (movies != null) {
            movies.forEach(i -> i.addActor(this));
        }
        this.movies = movies;
    }

    public Actor movies(Set<Movie> movies) {
        this.setMovies(movies);
        return this;
    }

    public Actor addMovie(Movie movie) {
        this.movies.add(movie);
        movie.getActors().add(this);
        return this;
    }

    public Actor removeMovie(Movie movie) {
        this.movies.remove(movie);
        movie.getActors().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Actor)) {
            return false;
        }
        return actorId != null && actorId.equals(((Actor) o).actorId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Actor{" +
            "actorId=" + getActorId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            "}";
    }
}
