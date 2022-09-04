package com.jana.bms.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.jana.bms.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookingDTO.class);
        BookingDTO bookingDTO1 = new BookingDTO();
        bookingDTO1.setBookingId(1L);
        BookingDTO bookingDTO2 = new BookingDTO();
        assertThat(bookingDTO1).isNotEqualTo(bookingDTO2);
        bookingDTO2.setBookingId(bookingDTO1.getBookingId());
        assertThat(bookingDTO1).isEqualTo(bookingDTO2);
        bookingDTO2.setBookingId(2L);
        assertThat(bookingDTO1).isNotEqualTo(bookingDTO2);
        bookingDTO1.setBookingId(null);
        assertThat(bookingDTO1).isNotEqualTo(bookingDTO2);
    }
}
