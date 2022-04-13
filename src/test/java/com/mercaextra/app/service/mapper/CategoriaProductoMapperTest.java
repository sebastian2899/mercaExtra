package com.mercaextra.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CategoriaProductoMapperTest {

    private CategoriaProductoMapper categoriaProductoMapper;

    @BeforeEach
    public void setUp() {
        categoriaProductoMapper = new CategoriaProductoMapperImpl();
    }
}
