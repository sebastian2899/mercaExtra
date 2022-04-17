package com.mercaextra.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mercaextra.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemFacturaVentaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemFacturaVenta.class);
        ItemFacturaVenta itemFacturaVenta1 = new ItemFacturaVenta();
        itemFacturaVenta1.setId(1L);
        ItemFacturaVenta itemFacturaVenta2 = new ItemFacturaVenta();
        itemFacturaVenta2.setId(itemFacturaVenta1.getId());
        assertThat(itemFacturaVenta1).isEqualTo(itemFacturaVenta2);
        itemFacturaVenta2.setId(2L);
        assertThat(itemFacturaVenta1).isNotEqualTo(itemFacturaVenta2);
        itemFacturaVenta1.setId(null);
        assertThat(itemFacturaVenta1).isNotEqualTo(itemFacturaVenta2);
    }
}
