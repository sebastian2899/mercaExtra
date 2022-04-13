package com.mercaextra.app.service.mapper;

import com.mercaextra.app.domain.Domiciliario;
import com.mercaextra.app.service.dto.DomiciliarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Domiciliario} and its DTO {@link DomiciliarioDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DomiciliarioMapper extends EntityMapper<DomiciliarioDTO, Domiciliario> {}
