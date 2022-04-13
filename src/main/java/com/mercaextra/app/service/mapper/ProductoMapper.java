package com.mercaextra.app.service.mapper;

import com.mercaextra.app.domain.Producto;
import com.mercaextra.app.service.dto.ProductoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Producto} and its DTO {@link ProductoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProductoMapper extends EntityMapper<ProductoDTO, Producto> {}
