package com.mercaextra.app.service.impl;

import com.mercaextra.app.domain.Proveedor;
import com.mercaextra.app.repository.ProveedorRepository;
import com.mercaextra.app.service.ProveedorService;
import com.mercaextra.app.service.dto.ProveedorDTO;
import com.mercaextra.app.service.mapper.ProveedorMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Proveedor}.
 */
@Service
@Transactional
public class ProveedorServiceImpl implements ProveedorService {

    private final Logger log = LoggerFactory.getLogger(ProveedorServiceImpl.class);

    private final ProveedorRepository proveedorRepository;

    private final ProveedorMapper proveedorMapper;

    public ProveedorServiceImpl(ProveedorRepository proveedorRepository, ProveedorMapper proveedorMapper) {
        this.proveedorRepository = proveedorRepository;
        this.proveedorMapper = proveedorMapper;
    }

    @Override
    public ProveedorDTO save(ProveedorDTO proveedorDTO) {
        log.debug("Request to save Proveedor : {}", proveedorDTO);
        Proveedor proveedor = proveedorMapper.toEntity(proveedorDTO);
        proveedor = proveedorRepository.save(proveedor);
        return proveedorMapper.toDto(proveedor);
    }

    @Override
    public Optional<ProveedorDTO> partialUpdate(ProveedorDTO proveedorDTO) {
        log.debug("Request to partially update Proveedor : {}", proveedorDTO);

        return proveedorRepository
            .findById(proveedorDTO.getId())
            .map(existingProveedor -> {
                proveedorMapper.partialUpdate(existingProveedor, proveedorDTO);

                return existingProveedor;
            })
            .map(proveedorRepository::save)
            .map(proveedorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProveedorDTO> findAll() {
        log.debug("Request to get all Proveedors");
        return proveedorRepository.findAll().stream().map(proveedorMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProveedorDTO> findOne(Long id) {
        log.debug("Request to get Proveedor : {}", id);
        return proveedorRepository.findById(id).map(proveedorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Proveedor : {}", id);
        proveedorRepository.deleteById(id);
    }
}
