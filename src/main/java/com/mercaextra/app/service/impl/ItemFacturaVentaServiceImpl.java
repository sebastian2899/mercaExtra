package com.mercaextra.app.service.impl;

import com.mercaextra.app.domain.ItemFacturaVenta;
import com.mercaextra.app.repository.ItemFacturaVentaRepository;
import com.mercaextra.app.service.ItemFacturaVentaService;
import com.mercaextra.app.service.dto.ItemFacturaVentaDTO;
import com.mercaextra.app.service.mapper.ItemFacturaVentaMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ItemFacturaVenta}.
 */
@Service
@Transactional
public class ItemFacturaVentaServiceImpl implements ItemFacturaVentaService {

    private final Logger log = LoggerFactory.getLogger(ItemFacturaVentaServiceImpl.class);

    private final ItemFacturaVentaRepository itemFacturaVentaRepository;

    private final ItemFacturaVentaMapper itemFacturaVentaMapper;

    public ItemFacturaVentaServiceImpl(
        ItemFacturaVentaRepository itemFacturaVentaRepository,
        ItemFacturaVentaMapper itemFacturaVentaMapper
    ) {
        this.itemFacturaVentaRepository = itemFacturaVentaRepository;
        this.itemFacturaVentaMapper = itemFacturaVentaMapper;
    }

    @Override
    public ItemFacturaVentaDTO save(ItemFacturaVentaDTO itemFacturaVentaDTO) {
        log.debug("Request to save ItemFacturaVenta : {}", itemFacturaVentaDTO);
        ItemFacturaVenta itemFacturaVenta = itemFacturaVentaMapper.toEntity(itemFacturaVentaDTO);
        itemFacturaVenta = itemFacturaVentaRepository.save(itemFacturaVenta);
        return itemFacturaVentaMapper.toDto(itemFacturaVenta);
    }

    @Override
    public Optional<ItemFacturaVentaDTO> partialUpdate(ItemFacturaVentaDTO itemFacturaVentaDTO) {
        log.debug("Request to partially update ItemFacturaVenta : {}", itemFacturaVentaDTO);

        return itemFacturaVentaRepository
            .findById(itemFacturaVentaDTO.getId())
            .map(existingItemFacturaVenta -> {
                itemFacturaVentaMapper.partialUpdate(existingItemFacturaVenta, itemFacturaVentaDTO);

                return existingItemFacturaVenta;
            })
            .map(itemFacturaVentaRepository::save)
            .map(itemFacturaVentaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemFacturaVentaDTO> findAll() {
        log.debug("Request to get all ItemFacturaVentas");
        return itemFacturaVentaRepository
            .findAll()
            .stream()
            .map(itemFacturaVentaMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemFacturaVentaDTO> findOne(Long id) {
        log.debug("Request to get ItemFacturaVenta : {}", id);
        return itemFacturaVentaRepository.findById(id).map(itemFacturaVentaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ItemFacturaVenta : {}", id);
        itemFacturaVentaRepository.deleteById(id);
    }
}
