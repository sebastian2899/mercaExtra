package com.mercaextra.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mercaextra.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReembolsoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reembolso.class);
        Reembolso reembolso1 = new Reembolso();
        reembolso1.setId(1L);
        Reembolso reembolso2 = new Reembolso();
        reembolso2.setId(reembolso1.getId());
        assertThat(reembolso1).isEqualTo(reembolso2);
        reembolso2.setId(2L);
        assertThat(reembolso1).isNotEqualTo(reembolso2);
        reembolso1.setId(null);
        assertThat(reembolso1).isNotEqualTo(reembolso2);
    }
}
