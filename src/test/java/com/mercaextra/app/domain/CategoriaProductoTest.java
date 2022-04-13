package com.mercaextra.app.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mercaextra.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategoriaProductoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoriaProducto.class);
        CategoriaProducto categoriaProducto1 = new CategoriaProducto();
        categoriaProducto1.setId(1L);
        CategoriaProducto categoriaProducto2 = new CategoriaProducto();
        categoriaProducto2.setId(categoriaProducto1.getId());
        assertThat(categoriaProducto1).isEqualTo(categoriaProducto2);
        categoriaProducto2.setId(2L);
        assertThat(categoriaProducto1).isNotEqualTo(categoriaProducto2);
        categoriaProducto1.setId(null);
        assertThat(categoriaProducto1).isNotEqualTo(categoriaProducto2);
    }
}
