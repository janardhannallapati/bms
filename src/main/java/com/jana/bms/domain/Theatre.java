package com.jana.bms.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Theatre.
 */
@Entity
@Table(name = "theatre")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Theatre implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "theatre_id", nullable = false)
    private Long theatreId;

    @Size(max = 45)
    @Column(name = "theatre_name", length = 45)
    private String theatreName;

    @NotNull
    @Column(name = "no_of_screens", nullable = false)
    private Integer noOfScreens;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getTheatreId() {
        return this.theatreId;
    }

    public Theatre theatreId(Long theatreId) {
        this.setTheatreId(theatreId);
        return this;
    }

    public void setTheatreId(Long theatreId) {
        this.theatreId = theatreId;
    }

    public String getTheatreName() {
        return this.theatreName;
    }

    public Theatre theatreName(String theatreName) {
        this.setTheatreName(theatreName);
        return this;
    }

    public void setTheatreName(String theatreName) {
        this.theatreName = theatreName;
    }

    public Integer getNoOfScreens() {
        return this.noOfScreens;
    }

    public Theatre noOfScreens(Integer noOfScreens) {
        this.setNoOfScreens(noOfScreens);
        return this;
    }

    public void setNoOfScreens(Integer noOfScreens) {
        this.noOfScreens = noOfScreens;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Theatre)) {
            return false;
        }
        return theatreId != null && theatreId.equals(((Theatre) o).theatreId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Theatre{" +
            "theatreId=" + getTheatreId() +
            ", theatreName='" + getTheatreName() + "'" +
            ", noOfScreens=" + getNoOfScreens() +
            "}";
    }
}
