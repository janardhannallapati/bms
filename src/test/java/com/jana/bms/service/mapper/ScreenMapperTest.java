package com.jana.bms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScreenMapperTest {

    private ScreenMapper screenMapper;

    @BeforeEach
    public void setUp() {
        screenMapper = new ScreenMapperImpl();
    }
}
