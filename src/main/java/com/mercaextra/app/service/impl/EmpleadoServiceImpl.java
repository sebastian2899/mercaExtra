package com.mercaextra.app.service.impl;

import com.mercaextra.app.domain.Empleado;
import com.mercaextra.app.repository.EmpleadoRepository;
import com.mercaextra.app.service.EmpleadoService;
import com.mercaextra.app.service.dto.EmpleadoDTO;
import com.mercaextra.app.service.mapper.EmpleadoMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Empleado}.
 */
@Service
@Transactional
public class EmpleadoServiceImpl implements EmpleadoService {

    private final Logger log = LoggerFactory.getLogger(EmpleadoServiceImpl.class);

    private final EmpleadoRepository empleadoRepository;

    private final EmpleadoMapper empleadoMapper;

    public EmpleadoServiceImpl(EmpleadoRepository empleadoRepository, EmpleadoMapper empleadoMapper) {
        this.empleadoRepository = empleadoRepository;
        this.empleadoMapper = empleadoMapper;
    }

    @Override
    public EmpleadoDTO save(EmpleadoDTO empleadoDTO) {
        log.debug("Request to save Empleado : {}", empleadoDTO);
        Empleado empleado = empleadoMapper.toEntity(empleadoDTO);
        empleado = empleadoRepository.save(empleado);
        return empleadoMapper.toDto(empleado);
    }

    @Override
    public Optional<EmpleadoDTO> partialUpdate(EmpleadoDTO empleadoDTO) {
        log.debug("Request to partially update Empleado : {}", empleadoDTO);

        return empleadoRepository
            .findById(empleadoDTO.getId())
            .map(existingEmpleado -> {
                empleadoMapper.partialUpdate(existingEmpleado, empleadoDTO);

                return existingEmpleado;
            })
            .map(empleadoRepository::save)
            .map(empleadoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmpleadoDTO> findAll() {
        log.debug("Request to get all Empleados");
        return empleadoRepository.findAll().stream().map(empleadoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmpleadoDTO> findOne(Long id) {
        log.debug("Request to get Empleado : {}", id);
        return empleadoRepository.findById(id).map(empleadoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Empleado : {}", id);
        empleadoRepository.deleteById(id);
    }
}
