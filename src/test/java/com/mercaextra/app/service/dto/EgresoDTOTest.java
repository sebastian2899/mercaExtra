package com.mercaextra.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mercaextra.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EgresoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EgresoDTO.class);
        EgresoDTO egresoDTO1 = new EgresoDTO();
        egresoDTO1.setId(1L);
        EgresoDTO egresoDTO2 = new EgresoDTO();
        assertThat(egresoDTO1).isNotEqualTo(egresoDTO2);
        egresoDTO2.setId(egresoDTO1.getId());
        assertThat(egresoDTO1).isEqualTo(egresoDTO2);
        egresoDTO2.setId(2L);
        assertThat(egresoDTO1).isNotEqualTo(egresoDTO2);
        egresoDTO1.setId(null);
        assertThat(egresoDTO1).isNotEqualTo(egresoDTO2);
    }
}
