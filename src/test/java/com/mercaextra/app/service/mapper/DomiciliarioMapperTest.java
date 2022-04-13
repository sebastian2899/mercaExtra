package com.mercaextra.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DomiciliarioMapperTest {

    private DomiciliarioMapper domiciliarioMapper;

    @BeforeEach
    public void setUp() {
        domiciliarioMapper = new DomiciliarioMapperImpl();
    }
}
