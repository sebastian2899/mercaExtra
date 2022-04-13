package com.mercaextra.app.service.impl;

import com.mercaextra.app.domain.Domiciliario;
import com.mercaextra.app.repository.DomiciliarioRepository;
import com.mercaextra.app.service.DomiciliarioService;
import com.mercaextra.app.service.dto.DomiciliarioDTO;
import com.mercaextra.app.service.mapper.DomiciliarioMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Domiciliario}.
 */
@Service
@Transactional
public class DomiciliarioServiceImpl implements DomiciliarioService {

    private final Logger log = LoggerFactory.getLogger(DomiciliarioServiceImpl.class);

    private final DomiciliarioRepository domiciliarioRepository;

    private final DomiciliarioMapper domiciliarioMapper;

    public DomiciliarioServiceImpl(DomiciliarioRepository domiciliarioRepository, DomiciliarioMapper domiciliarioMapper) {
        this.domiciliarioRepository = domiciliarioRepository;
        this.domiciliarioMapper = domiciliarioMapper;
    }

    @Override
    public DomiciliarioDTO save(DomiciliarioDTO domiciliarioDTO) {
        log.debug("Request to save Domiciliario : {}", domiciliarioDTO);
        Domiciliario domiciliario = domiciliarioMapper.toEntity(domiciliarioDTO);
        domiciliario = domiciliarioRepository.save(domiciliario);
        return domiciliarioMapper.toDto(domiciliario);
    }

    @Override
    public Optional<DomiciliarioDTO> partialUpdate(DomiciliarioDTO domiciliarioDTO) {
        log.debug("Request to partially update Domiciliario : {}", domiciliarioDTO);

        return domiciliarioRepository
            .findById(domiciliarioDTO.getId())
            .map(existingDomiciliario -> {
                domiciliarioMapper.partialUpdate(existingDomiciliario, domiciliarioDTO);

                return existingDomiciliario;
            })
            .map(domiciliarioRepository::save)
            .map(domiciliarioMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DomiciliarioDTO> findAll() {
        log.debug("Request to get all Domiciliarios");
        return domiciliarioRepository.findAll().stream().map(domiciliarioMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DomiciliarioDTO> findOne(Long id) {
        log.debug("Request to get Domiciliario : {}", id);
        return domiciliarioRepository.findById(id).map(domiciliarioMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Domiciliario : {}", id);
        domiciliarioRepository.deleteById(id);
    }
}
