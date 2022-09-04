package com.jana.bms.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.jana.bms.domain.Language} entity.
 */
public class LanguageDTO implements Serializable {

    @NotNull
    private Long languageId;

    @NotNull
    @Size(max = 20)
    private String name;

    public Long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
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
        if (!(o instanceof LanguageDTO)) {
            return false;
        }

        LanguageDTO languageDTO = (LanguageDTO) o;
        if (this.languageId == null) {
            return false;
        }
        return Objects.equals(this.languageId, languageDTO.languageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.languageId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LanguageDTO{" +
            "languageId=" + getLanguageId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
