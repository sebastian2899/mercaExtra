package com.mercaextra.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EgresoMapperTest {

    private EgresoMapper egresoMapper;

    @BeforeEach
    public void setUp() {
        egresoMapper = new EgresoMapperImpl();
    }
}
