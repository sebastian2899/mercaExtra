package com.mercaextra.app.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mercaextra.app.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CategoriaProductoDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoriaProductoDTO.class);
        CategoriaProductoDTO categoriaProductoDTO1 = new CategoriaProductoDTO();
        categoriaProductoDTO1.setId(1L);
        CategoriaProductoDTO categoriaProductoDTO2 = new CategoriaProductoDTO();
        assertThat(categoriaProductoDTO1).isNotEqualTo(categoriaProductoDTO2);
        categoriaProductoDTO2.setId(categoriaProductoDTO1.getId());
        assertThat(categoriaProductoDTO1).isEqualTo(categoriaProductoDTO2);
        categoriaProductoDTO2.setId(2L);
        assertThat(categoriaProductoDTO1).isNotEqualTo(categoriaProductoDTO2);
        categoriaProductoDTO1.setId(null);
        assertThat(categoriaProductoDTO1).isNotEqualTo(categoriaProductoDTO2);
    }
}
