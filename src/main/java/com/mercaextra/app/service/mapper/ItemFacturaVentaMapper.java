package com.mercaextra.app.service.mapper;

import com.mercaextra.app.domain.ItemFacturaVenta;
import com.mercaextra.app.service.dto.ItemFacturaVentaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ItemFacturaVenta} and its DTO {@link ItemFacturaVentaDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ItemFacturaVentaMapper extends EntityMapper<ItemFacturaVentaDTO, ItemFacturaVenta> {}
