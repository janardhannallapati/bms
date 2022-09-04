package com.jana.bms.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Screen.
 */
@Entity
@Table(name = "screen")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Screen implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "screen_id", nullable = false)
    private Long screenId;

    @NotNull
    @Size(max = 45)
    @Column(name = "name", length = 45, nullable = false)
    private String name;

    @NotNull
    @Column(name = "total_seats", nullable = false)
    private Integer totalSeats;

    @ManyToOne(optional = false)
    @NotNull
    private Theatre theatre;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getScreenId() {
        return this.screenId;
    }

    public Screen screenId(Long screenId) {
        this.setScreenId(screenId);
        return this;
    }

    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }

    public String getName() {
        return this.name;
    }

    public Screen name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTotalSeats() {
        return this.totalSeats;
    }

    public Screen totalSeats(Integer totalSeats) {
        this.setTotalSeats(totalSeats);
        return this;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Theatre getTheatre() {
        return this.theatre;
    }

    public void setTheatre(Theatre theatre) {
        this.theatre = theatre;
    }

    public Screen theatre(Theatre theatre) {
        this.setTheatre(theatre);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Screen)) {
            return false;
        }
        return screenId != null && screenId.equals(((Screen) o).screenId);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Screen{" +
            "screenId=" + getScreenId() +
            ", name='" + getName() + "'" +
            ", totalSeats=" + getTotalSeats() +
            "}";
    }
}
