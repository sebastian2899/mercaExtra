package com.mercaextra.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompraMapperTest {

    private CompraMapper compraMapper;

    @BeforeEach
    public void setUp() {
        compraMapper = new CompraMapperImpl();
    }
}
