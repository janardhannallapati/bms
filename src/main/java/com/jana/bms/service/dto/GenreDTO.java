package com.jana.bms.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.jana.bms.domain.Genre} entity.
 */
public class GenreDTO implements Serializable {

    @NotNull
    private Long genreId;

    @NotNull
    @Size(max = 25)
    private String name;

    public Long getGenreId() {
        return genreId;
    }

    public void setGenreId(Long genreId) {
        this.genreId = genreId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GenreDTO)) {
            return false;
        }

        GenreDTO genreDTO = (GenreDTO) o;
        if (this.genreId == null) {
            return false;
        }
        return Objects.equals(this.genreId, genreDTO.genreId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.genreId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GenreDTO{" +
            "genreId=" + getGenreId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
