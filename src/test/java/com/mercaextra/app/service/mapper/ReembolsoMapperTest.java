package com.mercaextra.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReembolsoMapperTest {

    private ReembolsoMapper reembolsoMapper;

    @BeforeEach
    public void setUp() {
        reembolsoMapper = new ReembolsoMapperImpl();
    }
}
