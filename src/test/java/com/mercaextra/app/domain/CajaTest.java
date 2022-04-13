package com.mercaextra.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mercaextra.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CajaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Caja.class);
        Caja caja1 = new Caja();
        caja1.setId(1L);
        Caja caja2 = new Caja();
        caja2.setId(caja1.getId());
        assertThat(caja1).isEqualTo(caja2);
        caja2.setId(2L);
        assertThat(caja1).isNotEqualTo(caja2);
        caja1.setId(null);
        assertThat(caja1).isNotEqualTo(caja2);
    }
}
