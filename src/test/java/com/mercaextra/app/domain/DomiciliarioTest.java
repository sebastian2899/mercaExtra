package com.mercaextra.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mercaextra.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DomiciliarioTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Domiciliario.class);
        Domiciliario domiciliario1 = new Domiciliario();
        domiciliario1.setId(1L);
        Domiciliario domiciliario2 = new Domiciliario();
        domiciliario2.setId(domiciliario1.getId());
        assertThat(domiciliario1).isEqualTo(domiciliario2);
        domiciliario2.setId(2L);
        assertThat(domiciliario1).isNotEqualTo(domiciliario2);
        domiciliario1.setId(null);
        assertThat(domiciliario1).isNotEqualTo(domiciliario2);
    }
}
