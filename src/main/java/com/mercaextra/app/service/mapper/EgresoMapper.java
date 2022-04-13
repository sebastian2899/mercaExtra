package com.mercaextra.app.service.mapper;

import com.mercaextra.app.domain.Egreso;
import com.mercaextra.app.service.dto.EgresoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Egreso} and its DTO {@link EgresoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EgresoMapper extends EntityMapper<EgresoDTO, Egreso> {}
