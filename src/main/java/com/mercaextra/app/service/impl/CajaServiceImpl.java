package com.mercaextra.app.service.impl;

import com.mercaextra.app.domain.Caja;
import com.mercaextra.app.repository.CajaRepository;
import com.mercaextra.app.service.CajaService;
import com.mercaextra.app.service.dto.CajaDTO;
import com.mercaextra.app.service.mapper.CajaMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Caja}.
 */
@Service
@Transactional
public class CajaServiceImpl implements CajaService {

    private final Logger log = LoggerFactory.getLogger(CajaServiceImpl.class);

    private final CajaRepository cajaRepository;

    private final CajaMapper cajaMapper;

    public CajaServiceImpl(CajaRepository cajaRepository, CajaMapper cajaMapper) {
        this.cajaRepository = cajaRepository;
        this.cajaMapper = cajaMapper;
    }

    @Override
    public CajaDTO save(CajaDTO cajaDTO) {
        log.debug("Request to save Caja : {}", cajaDTO);
        Caja caja = cajaMapper.toEntity(cajaDTO);
        caja = cajaRepository.save(caja);
        return cajaMapper.toDto(caja);
    }

    @Override
    public Optional<CajaDTO> partialUpdate(CajaDTO cajaDTO) {
        log.debug("Request to partially update Caja : {}", cajaDTO);

        return cajaRepository
            .findById(cajaDTO.getId())
            .map(existingCaja -> {
                cajaMapper.partialUpdate(existingCaja, cajaDTO);

                return existingCaja;
            })
            .map(cajaRepository::save)
            .map(cajaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CajaDTO> findAll() {
        log.debug("Request to get all Cajas");
        return cajaRepository.findAll().stream().map(cajaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CajaDTO> findOne(Long id) {
        log.debug("Request to get Caja : {}", id);
        return cajaRepository.findById(id).map(cajaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Caja : {}", id);
        cajaRepository.deleteById(id);
    }
}
