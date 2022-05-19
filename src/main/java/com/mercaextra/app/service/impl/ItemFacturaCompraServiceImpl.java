package com.mercaextra.app.service.impl;

import com.mercaextra.app.domain.ItemFacturaCompra;
import com.mercaextra.app.repository.ItemFacturaCompraRepository;
import com.mercaextra.app.service.ItemFacturaCompraService;
import com.mercaextra.app.service.dto.ItemFacturaCompraDTO;
import com.mercaextra.app.service.mapper.ItemFacturaCompraMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ItemFacturaCompraServiceImpl implements ItemFacturaCompraService {

    //private final Logger log = LoggerFactory.getLogger(ItemFacturaCompraServiceImpl.class);

    private final Logger log = LoggerFactory.getLogger(ItemFacturaCompraServiceImpl.class);

    private final ItemFacturaCompraRepository itemFacturaCompraRepository;

    private final ItemFacturaCompraMapper itemFacturaCompraMapper;

    public ItemFacturaCompraServiceImpl(
        ItemFacturaCompraRepository itemFacturaCompraRepository,
        ItemFacturaCompraMapper itemFacturaCompraMapper
    ) {
        this.itemFacturaCompraRepository = itemFacturaCompraRepository;
        this.itemFacturaCompraMapper = itemFacturaCompraMapper;
    }

    @Override
    public ItemFacturaCompraDTO save(ItemFacturaCompraDTO itemFacturaCompraDTO) {
        ItemFacturaCompra itemFacturaCompra = itemFacturaCompraMapper.toEntity(itemFacturaCompraDTO);
        itemFacturaCompraRepository.save(itemFacturaCompra);

        return itemFacturaCompraMapper.toDto(itemFacturaCompra);
    }

    @Override
    public List<ItemFacturaCompraDTO> findAll() {
        return itemFacturaCompraRepository
            .findAll()
            .stream()
            .map(itemFacturaCompraMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public ItemFacturaCompraDTO itemCompraById(Long id) {
        log.debug("Request to get itemCompra by id", id);
        ItemFacturaCompra itemFacturaCompra = itemFacturaCompraRepository.getById(id);
        return itemFacturaCompraMapper.toDto(itemFacturaCompra);
    }

    @Override
    public void delete(Long id) {
        log.debug("Resquest to delete item per id:", id);
        itemFacturaCompraRepository.deleteById(id);
    }
}
