package com.mercaextra.app.service.mapper;

import com.mercaextra.app.domain.CategoriaProducto;
import com.mercaextra.app.service.dto.CategoriaProductoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CategoriaProducto} and its DTO {@link CategoriaProductoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CategoriaProductoMapper extends EntityMapper<CategoriaProductoDTO, CategoriaProducto> {}
