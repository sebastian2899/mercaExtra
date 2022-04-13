package com.mercaextra.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mercaextra.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EgresoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Egreso.class);
        Egreso egreso1 = new Egreso();
        egreso1.setId(1L);
        Egreso egreso2 = new Egreso();
        egreso2.setId(egreso1.getId());
        assertThat(egreso1).isEqualTo(egreso2);
        egreso2.setId(2L);
        assertThat(egreso1).isNotEqualTo(egreso2);
        egreso1.setId(null);
        assertThat(egreso1).isNotEqualTo(egreso2);
    }
}
