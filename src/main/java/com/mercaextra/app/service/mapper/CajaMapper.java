package com.mercaextra.app.service.mapper;

import com.mercaextra.app.domain.Caja;
import com.mercaextra.app.service.dto.CajaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Caja} and its DTO {@link CajaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CajaMapper extends EntityMapper<CajaDTO, Caja> {}
