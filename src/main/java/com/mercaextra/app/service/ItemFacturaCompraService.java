package com.mercaextra.app.service;

import com.mercaextra.app.service.dto.ItemFacturaCompraDTO;
import java.util.List;

public interface ItemFacturaCompraService {
    ItemFacturaCompraDTO save(ItemFacturaCompraDTO itemFacturaCompraDTO);

    List<ItemFacturaCompraDTO> findAll();

    ItemFacturaCompraDTO itemCompraById(Long id);

    void delete(Long id);
}
