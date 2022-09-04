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
 * A Genre.
 */
@Entity
@Table(name = "genre")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Genre implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "genre_id", nullable = false)
    private Long genreId;

    @NotNull
    @Size(max = 25)
    @Column(name = "name", length = 25, nullable = false)
    private String name;

    @ManyToMany(mappedBy = "genres")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "language", "actors", "genres" }, allowSetters = true)
    private Set<Movie> movies = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getGenreId() {
        return this.genreId;
    }

    public Genre genreId(Long genreId) {
        this.setGenreId(genreId);
        return this;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

    public String getName() {
        return this.name;
    }

    public Genre name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Movie> getMovies() {
        return this.movies;
    }

    public void setMovies(Set<Movie> movies) {
        if (this.movies != null) {
            this.movies.forEach(i -> i.removeGenre(this));
        }
        if (movies != null) {
            movies.forEach(i -> i.addGenre(this));
        }
        this.movies = movies;
    }

    public Genre movies(Set<Movie> movies) {
        this.setMovies(movies);
        return this;
    }

    public Genre addMovie(Movie movie) {
        this.movies.add(movie);
        movie.getGenres().add(this);
        return this;
    }

    public Genre removeMovie(Movie movie) {
        this.movies.remove(movie);
        movie.getGenres().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Genre)) {
            return false;
        }
        return genreId != null && genreId.equals(((Genre) o).genreId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Genre{" +
            "genreId=" + getGenreId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
