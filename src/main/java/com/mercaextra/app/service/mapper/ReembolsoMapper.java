package com.mercaextra.app.service.mapper;

import com.mercaextra.app.domain.Reembolso;
import com.mercaextra.app.service.dto.ReembolsoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Reembolso} and its DTO {@link ReembolsoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ReembolsoMapper extends EntityMapper<ReembolsoDTO, Reembolso> {}
