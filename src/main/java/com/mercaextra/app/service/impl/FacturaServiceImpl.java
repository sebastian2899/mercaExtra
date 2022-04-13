package com.mercaextra.app.service.impl;

import com.mercaextra.app.domain.Factura;
import com.mercaextra.app.repository.FacturaRepository;
import com.mercaextra.app.service.FacturaService;
import com.mercaextra.app.service.dto.FacturaDTO;
import com.mercaextra.app.service.mapper.FacturaMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Factura}.
 */
@Service
@Transactional
public class FacturaServiceImpl implements FacturaService {

    private final Logger log = LoggerFactory.getLogger(FacturaServiceImpl.class);

    private final FacturaRepository facturaRepository;

    private final FacturaMapper facturaMapper;

    public FacturaServiceImpl(FacturaRepository facturaRepository, FacturaMapper facturaMapper) {
        this.facturaRepository = facturaRepository;
        this.facturaMapper = facturaMapper;
    }

    @Override
    public FacturaDTO save(FacturaDTO facturaDTO) {
        log.debug("Request to save Factura : {}", facturaDTO);
        Factura factura = facturaMapper.toEntity(facturaDTO);
        factura = facturaRepository.save(factura);
        return facturaMapper.toDto(factura);
    }

    @Override
    public Optional<FacturaDTO> partialUpdate(FacturaDTO facturaDTO) {
        log.debug("Request to partially update Factura : {}", facturaDTO);

        return facturaRepository
            .findById(facturaDTO.getId())
            .map(existingFactura -> {
                facturaMapper.partialUpdate(existingFactura, facturaDTO);

                return existingFactura;
            })
            .map(facturaRepository::save)
            .map(facturaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacturaDTO> findAll() {
        log.debug("Request to get all Facturas");
        return facturaRepository.findAll().stream().map(facturaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FacturaDTO> findOne(Long id) {
        log.debug("Request to get Factura : {}", id);
        return facturaRepository.findById(id).map(facturaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Factura : {}", id);
        facturaRepository.deleteById(id);
    }
}
