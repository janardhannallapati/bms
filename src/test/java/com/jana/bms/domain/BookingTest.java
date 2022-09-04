package com.jana.bms.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Booking.class);
        Booking booking1 = new Booking();
        booking1.setBookingId(1L);
        Booking booking2 = new Booking();
        booking2.setBookingId(booking1.getBookingId());
        assertThat(booking1).isEqualTo(booking2);
        booking2.setBookingId(2L);
        assertThat(booking1).isNotEqualTo(booking2);
        booking1.setBookingId(null);
        assertThat(booking1).isNotEqualTo(booking2);
    }
}
