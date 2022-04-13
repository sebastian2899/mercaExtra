package com.mercaextra.app.service.mapper;

import com.mercaextra.app.domain.Factura;
import com.mercaextra.app.service.dto.FacturaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Factura} and its DTO {@link FacturaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FacturaMapper extends EntityMapper<FacturaDTO, Factura> {}
