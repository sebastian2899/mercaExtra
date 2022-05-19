package com.mercaextra.app.service.mapper;

import com.mercaextra.app.domain.ItemFacturaCompra;
import com.mercaextra.app.service.dto.ItemFacturaCompraDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface ItemFacturaCompraMapper extends EntityMapper<ItemFacturaCompraDTO, ItemFacturaCompra> {}
