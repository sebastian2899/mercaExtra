package com.mercaextra.app.service.mapper;

import com.mercaextra.app.domain.Notificacion;
import com.mercaextra.app.service.dto.NotificacionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notificacion} and its DTO {@link NotificacionDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface NotificacionMapper extends EntityMapper<NotificacionDTO, Notificacion> {}
