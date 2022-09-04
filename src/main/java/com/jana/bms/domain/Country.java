package com.jana.bms.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Country.
 */
@Entity
@Table(name = "country")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Country implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "country_id", nullable = false)
    private Long countryId;

    @NotNull
    @Size(max = 3)
    @Column(name = "country_code", length = 3, nullable = false)
    private String countryCode;

    @NotNull
    @Size(max = 50)
    @Column(name = "country_name", length = 50, nullable = false)
    private String countryName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getCountryId() {
        return this.countryId;
    }

    public Country countryId(Long countryId) {
        this.setCountryId(countryId);
        return this;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public Country countryCode(String countryCode) {
        this.setCountryCode(countryCode);
        return this;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return this.countryName;
    }

    public Country countryName(String countryName) {
        this.setCountryName(countryName);
        return this;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Country)) {
            return false;
        }
        return countryId != null && countryId.equals(((Country) o).countryId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Country{" +
            "countryId=" + getCountryId() +
            ", countryCode='" + getCountryCode() + "'" +
            ", countryName='" + getCountryName() + "'" +
            "}";
    }
}
