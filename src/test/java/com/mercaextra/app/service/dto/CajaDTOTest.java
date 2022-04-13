package com.mercaextra.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mercaextra.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CajaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CajaDTO.class);
        CajaDTO cajaDTO1 = new CajaDTO();
        cajaDTO1.setId(1L);
        CajaDTO cajaDTO2 = new CajaDTO();
        assertThat(cajaDTO1).isNotEqualTo(cajaDTO2);
        cajaDTO2.setId(cajaDTO1.getId());
        assertThat(cajaDTO1).isEqualTo(cajaDTO2);
        cajaDTO2.setId(2L);
        assertThat(cajaDTO1).isNotEqualTo(cajaDTO2);
        cajaDTO1.setId(null);
        assertThat(cajaDTO1).isNotEqualTo(cajaDTO2);
    }
}
