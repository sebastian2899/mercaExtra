package com.mercaextra.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mercaextra.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ItemFacturaVentaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ItemFacturaVentaDTO.class);
        ItemFacturaVentaDTO itemFacturaVentaDTO1 = new ItemFacturaVentaDTO();
        itemFacturaVentaDTO1.setId(1L);
        ItemFacturaVentaDTO itemFacturaVentaDTO2 = new ItemFacturaVentaDTO();
        assertThat(itemFacturaVentaDTO1).isNotEqualTo(itemFacturaVentaDTO2);
        itemFacturaVentaDTO2.setId(itemFacturaVentaDTO1.getId());
        assertThat(itemFacturaVentaDTO1).isEqualTo(itemFacturaVentaDTO2);
        itemFacturaVentaDTO2.setId(2L);
        assertThat(itemFacturaVentaDTO1).isNotEqualTo(itemFacturaVentaDTO2);
        itemFacturaVentaDTO1.setId(null);
        assertThat(itemFacturaVentaDTO1).isNotEqualTo(itemFacturaVentaDTO2);
    }
}
