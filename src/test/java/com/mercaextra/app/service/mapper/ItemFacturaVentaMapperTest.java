package com.mercaextra.app.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ItemFacturaVentaMapperTest {

    private ItemFacturaVentaMapper itemFacturaVentaMapper;

    @BeforeEach
    public void setUp() {
        itemFacturaVentaMapper = new ItemFacturaVentaMapperImpl();
    }
}
