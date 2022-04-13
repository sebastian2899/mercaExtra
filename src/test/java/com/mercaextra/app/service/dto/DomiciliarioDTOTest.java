package com.mercaextra.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mercaextra.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DomiciliarioDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DomiciliarioDTO.class);
        DomiciliarioDTO domiciliarioDTO1 = new DomiciliarioDTO();
        domiciliarioDTO1.setId(1L);
        DomiciliarioDTO domiciliarioDTO2 = new DomiciliarioDTO();
        assertThat(domiciliarioDTO1).isNotEqualTo(domiciliarioDTO2);
        domiciliarioDTO2.setId(domiciliarioDTO1.getId());
        assertThat(domiciliarioDTO1).isEqualTo(domiciliarioDTO2);
        domiciliarioDTO2.setId(2L);
        assertThat(domiciliarioDTO1).isNotEqualTo(domiciliarioDTO2);
        domiciliarioDTO1.setId(null);
        assertThat(domiciliarioDTO1).isNotEqualTo(domiciliarioDTO2);
    }
}
