package com.jana.bms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookingMapperTest {

    private BookingMapper bookingMapper;

    @BeforeEach
    public void setUp() {
        bookingMapper = new BookingMapperImpl();
    }
}
