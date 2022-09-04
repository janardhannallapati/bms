package com.jana.bms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TheatreMapperTest {

    private TheatreMapper theatreMapper;

    @BeforeEach
    public void setUp() {
        theatreMapper = new TheatreMapperImpl();
    }
}
