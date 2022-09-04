package com.jana.bms.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShowMapperTest {

    private ShowMapper showMapper;

    @BeforeEach
    public void setUp() {
        showMapper = new ShowMapperImpl();
    }
}
