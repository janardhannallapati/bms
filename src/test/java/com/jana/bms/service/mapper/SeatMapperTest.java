package com.jana.bms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SeatMapperTest {

    private SeatMapper seatMapper;

    @BeforeEach
    public void setUp() {
        seatMapper = new SeatMapperImpl();
    }
}
