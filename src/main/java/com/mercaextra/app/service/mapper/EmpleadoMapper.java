package com.mercaextra.app.service.mapper;

import com.mercaextra.app.domain.Empleado;
import com.mercaextra.app.service.dto.EmpleadoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Empleado} and its DTO {@link EmpleadoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EmpleadoMapper extends EntityMapper<EmpleadoDTO, Empleado> {}
