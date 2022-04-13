package com.mercaextra.app.service.impl;

import com.mercaextra.app.domain.Compra;
import com.mercaextra.app.repository.CompraRepository;
import com.mercaextra.app.service.CompraService;
import com.mercaextra.app.service.dto.CompraDTO;
import com.mercaextra.app.service.mapper.CompraMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Compra}.
 */
@Service
@Transactional
public class CompraServiceImpl implements CompraService {

    private final Logger log = LoggerFactory.getLogger(CompraServiceImpl.class);

    private final CompraRepository compraRepository;

    private final CompraMapper compraMapper;

    public CompraServiceImpl(CompraRepository compraRepository, CompraMapper compraMapper) {
        this.compraRepository = compraRepository;
        this.compraMapper = compraMapper;
    }

    @Override
    public CompraDTO save(CompraDTO compraDTO) {
        log.debug("Request to save Compra : {}", compraDTO);
        Compra compra = compraMapper.toEntity(compraDTO);
        compra = compraRepository.save(compra);
        return compraMapper.toDto(compra);
    }

    @Override
    public Optional<CompraDTO> partialUpdate(CompraDTO compraDTO) {
        log.debug("Request to partially update Compra : {}", compraDTO);

        return compraRepository
            .findById(compraDTO.getId())
            .map(existingCompra -> {
                compraMapper.partialUpdate(existingCompra, compraDTO);

                return existingCompra;
            })
            .map(compraRepository::save)
            .map(compraMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompraDTO> findAll() {
        log.debug("Request to get all Compras");
        return compraRepository.findAll().stream().map(compraMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CompraDTO> findOne(Long id) {
        log.debug("Request to get Compra : {}", id);
        return compraRepository.findById(id).map(compraMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Compra : {}", id);
        compraRepository.deleteById(id);
    }
}
