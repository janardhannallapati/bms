package com.jana.bms.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Language.
 */
@Entity
@Table(name = "language")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Language implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id", nullable = false)
    private Long languageId;

    @NotNull
    @Size(max = 20)
    @Column(name = "name", length = 20, nullable = false)
    private String name;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getLanguageId() {
        return this.languageId;
    }

    public Language languageId(Long languageId) {
        this.setLanguageId(languageId);
        return this;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }

    public String getName() {
        return this.name;
    }

    public Language name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Language)) {
            return false;
        }
        return languageId != null && languageId.equals(((Language) o).languageId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Language{" +
            "languageId=" + getLanguageId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
