package com.jana.bms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShowSeatMapperTest {

    private ShowSeatMapper showSeatMapper;

    @BeforeEach
    public void setUp() {
        showSeatMapper = new ShowSeatMapperImpl();
    }
}
