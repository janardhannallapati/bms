package com.jana.bms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ActorDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ActorDTO.class);
        ActorDTO actorDTO1 = new ActorDTO();
        actorDTO1.setActorId(1L);
        ActorDTO actorDTO2 = new ActorDTO();
        assertThat(actorDTO1).isNotEqualTo(actorDTO2);
        actorDTO2.setActorId(actorDTO1.getActorId());
        assertThat(actorDTO1).isEqualTo(actorDTO2);
        actorDTO2.setActorId(2L);
        assertThat(actorDTO1).isNotEqualTo(actorDTO2);
        actorDTO1.setActorId(null);
        assertThat(actorDTO1).isNotEqualTo(actorDTO2);
    }
}
