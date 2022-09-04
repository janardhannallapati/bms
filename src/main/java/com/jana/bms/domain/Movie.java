package com.jana.bms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jana.bms.domain.enumeration.Rating;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Movie.
 */
@Entity
@Table(name = "movie")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "movie_id", nullable = false)
    private Long movieId;

    @NotNull
    @Size(max = 128)
    @Column(name = "title", length = 128, nullable = false)
    private String title;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @Min(value = 1870)
    @Max(value = 2100)
    @Column(name = "release_year")
    private Integer releaseYear;

    @Column(name = "length")
    private Integer length;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "rating", nullable = false)
    private Rating rating;

    @ManyToOne(optional = false)
    @NotNull
    private Language language;

    @ManyToMany
    @NotNull
    @JoinTable(
        name = "rel_movie__actor",
        joinColumns = @JoinColumn(name = "movie_movie_id"),
        inverseJoinColumns = @JoinColumn(name = "actor_actor_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "movies" }, allowSetters = true)
    private Set<Actor> actors = new HashSet<>();

    @ManyToMany
    @NotNull
    @JoinTable(
        name = "rel_movie__genre",
        joinColumns = @JoinColumn(name = "movie_movie_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_genre_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "movies" }, allowSetters = true)
    private Set<Genre> genres = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getMovieId() {
        return this.movieId;
    }

    public Movie movieId(Long movieId) {
        this.setMovieId(movieId);
        return this;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return this.title;
    }

    public Movie title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Movie description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getReleaseYear() {
        return this.releaseYear;
    }

    public Movie releaseYear(Integer releaseYear) {
        this.setReleaseYear(releaseYear);
        return this;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Integer getLength() {
        return this.length;
    }

    public Movie length(Integer length) {
        this.setLength(length);
        return this;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Rating getRating() {
        return this.rating;
    }

    public Movie rating(Rating rating) {
        this.setRating(rating);
        return this;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public Language getLanguage() {
        return this.language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Movie language(Language language) {
        this.setLanguage(language);
        return this;
    }

    public Set<Actor> getActors() {
        return this.actors;
    }

    public void setActors(Set<Actor> actors) {
        this.actors = actors;
    }

    public Movie actors(Set<Actor> actors) {
        this.setActors(actors);
        return this;
    }

    public Movie addActor(Actor actor) {
        this.actors.add(actor);
        actor.getMovies().add(this);
        return this;
    }

    public Movie removeActor(Actor actor) {
        this.actors.remove(actor);
        actor.getMovies().remove(this);
        return this;
    }

    public Set<Genre> getGenres() {
        return this.genres;
    }

    public void setGenres(Set<Genre> genres) {
        this.genres = genres;
    }

    public Movie genres(Set<Genre> genres) {
        this.setGenres(genres);
        return this;
    }

    public Movie addGenre(Genre genre) {
        this.genres.add(genre);
        genre.getMovies().add(this);
        return this;
    }

    public Movie removeGenre(Genre genre) {
        this.genres.remove(genre);
        genre.getMovies().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Movie)) {
            return false;
        }
        return movieId != null && movieId.equals(((Movie) o).movieId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Movie{" +
            "movieId=" + getMovieId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", releaseYear=" + getReleaseYear() +
            ", length=" + getLength() +
            ", rating='" + getRating() + "'" +
            "}";
    }
}
