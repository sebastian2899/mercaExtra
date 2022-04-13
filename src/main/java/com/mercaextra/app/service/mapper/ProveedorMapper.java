package com.mercaextra.app.service.mapper;

import com.mercaextra.app.domain.Proveedor;
import com.mercaextra.app.service.dto.ProveedorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Proveedor} and its DTO {@link ProveedorDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProveedorMapper extends EntityMapper<ProveedorDTO, Proveedor> {}
