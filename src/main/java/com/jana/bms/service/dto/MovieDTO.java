package com.jana.bms.service.dto;

import com.jana.bms.domain.enumeration.Rating;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.jana.bms.domain.Movie} entity.
 */
public class MovieDTO implements Serializable {

    @NotNull
    private Long movieId;

    @NotNull
    @Size(max = 128)
    private String title;

    @Size(max = 255)
    private String description;

    @Min(value = 1870)
    @Max(value = 2100)
    private Integer releaseYear;

    private Integer length;

    @NotNull
    private Rating rating;

    private LanguageDTO language;

    private Set<ActorDTO> actors = new HashSet<>();

    private Set<GenreDTO> genres = new HashSet<>();

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public LanguageDTO getLanguage() {
        return language;
    }

    public void setLanguage(LanguageDTO language) {
        this.language = language;
    }

    public Set<ActorDTO> getActors() {
        return actors;
    }

    public void setActors(Set<ActorDTO> actors) {
        this.actors = actors;
    }

    public Set<GenreDTO> getGenres() {
        return genres;
    }

    public void setGenres(Set<GenreDTO> genres) {
        this.genres = genres;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MovieDTO)) {
            return false;
        }

        MovieDTO movieDTO = (MovieDTO) o;
        if (this.movieId == null) {
            return false;
        }
        return Objects.equals(this.movieId, movieDTO.movieId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.movieId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MovieDTO{" +
            "movieId=" + getMovieId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", releaseYear=" + getReleaseYear() +
            ", length=" + getLength() +
            ", rating='" + getRating() + "'" +
            ", language=" + getLanguage() +
            ", actors=" + getActors() +
            ", genres=" + getGenres() +
            "}";
    }
}
