package com.jana.bms.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.jana.bms.domain.Actor} entity.
 */
public class ActorDTO implements Serializable {

    @NotNull
    private Long actorId;

    @NotNull
    @Size(max = 45)
    private String firstName;

    @NotNull
    @Size(max = 45)
    private String lastName;

    public Long getActorId() {
        return actorId;
    }

    public void setActorId(Long actorId) {
        this.actorId = actorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ActorDTO)) {
            return false;
        }

        ActorDTO actorDTO = (ActorDTO) o;
        if (this.actorId == null) {
            return false;
        }
        return Objects.equals(this.actorId, actorDTO.actorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.actorId);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ActorDTO{" +
            "actorId=" + getActorId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            "}";
    }
}
