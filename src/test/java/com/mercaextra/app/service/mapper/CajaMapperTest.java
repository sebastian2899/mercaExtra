package com.mercaextra.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CajaMapperTest {

    private CajaMapper cajaMapper;

    @BeforeEach
    public void setUp() {
        cajaMapper = new CajaMapperImpl();
    }
}
