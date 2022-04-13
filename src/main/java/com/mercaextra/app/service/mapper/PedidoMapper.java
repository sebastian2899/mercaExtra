package com.mercaextra.app.service.mapper;

import com.mercaextra.app.domain.Pedido;
import com.mercaextra.app.service.dto.PedidoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pedido} and its DTO {@link PedidoDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PedidoMapper extends EntityMapper<PedidoDTO, Pedido> {}
