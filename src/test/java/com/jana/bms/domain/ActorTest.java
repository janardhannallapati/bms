package com.jana.bms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ActorTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Actor.class);
        Actor actor1 = new Actor();
        actor1.setActorId(1L);
        Actor actor2 = new Actor();
        actor2.setActorId(actor1.getActorId());
        assertThat(actor1).isEqualTo(actor2);
        actor2.setActorId(2L);
        assertThat(actor1).isNotEqualTo(actor2);
        actor1.setActorId(null);
        assertThat(actor1).isNotEqualTo(actor2);
    }
}
