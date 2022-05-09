package com.mercaextra.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mercaextra.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReembolsoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ReembolsoDTO.class);
        ReembolsoDTO reembolsoDTO1 = new ReembolsoDTO();
        reembolsoDTO1.setId(1L);
        ReembolsoDTO reembolsoDTO2 = new ReembolsoDTO();
        assertThat(reembolsoDTO1).isNotEqualTo(reembolsoDTO2);
        reembolsoDTO2.setId(reembolsoDTO1.getId());
        assertThat(reembolsoDTO1).isEqualTo(reembolsoDTO2);
        reembolsoDTO2.setId(2L);
        assertThat(reembolsoDTO1).isNotEqualTo(reembolsoDTO2);
        reembolsoDTO1.setId(null);
        assertThat(reembolsoDTO1).isNotEqualTo(reembolsoDTO2);
    }
}
