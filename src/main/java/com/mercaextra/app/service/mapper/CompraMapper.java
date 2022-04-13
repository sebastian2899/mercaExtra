package com.mercaextra.app.service.mapper;

import com.mercaextra.app.domain.Compra;
import com.mercaextra.app.service.dto.CompraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Compra} and its DTO {@link CompraDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CompraMapper extends EntityMapper<CompraDTO, Compra> {}
