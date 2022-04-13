package com.mercaextra.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mercaextra.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NotificacionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notificacion.class);
        Notificacion notificacion1 = new Notificacion();
        notificacion1.setId(1L);
        Notificacion notificacion2 = new Notificacion();
        notificacion2.setId(notificacion1.getId());
        assertThat(notificacion1).isEqualTo(notificacion2);
        notificacion2.setId(2L);
        assertThat(notificacion1).isNotEqualTo(notificacion2);
        notificacion1.setId(null);
        assertThat(notificacion1).isNotEqualTo(notificacion2);
    }
}
