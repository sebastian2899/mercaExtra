package com.mercaextra.app.service.impl;

import com.mercaextra.app.config.Constants;
import com.mercaextra.app.domain.Domiciliario;
import com.mercaextra.app.domain.enumeration.EstadoDomiciliario;
import com.mercaextra.app.domain.enumeration.TipoSalario;
import com.mercaextra.app.repository.DomiciliarioRepository;
import com.mercaextra.app.service.DomiciliarioService;
import com.mercaextra.app.service.dto.DomiciliarioDTO;
import com.mercaextra.app.service.mapper.DomiciliarioMapper;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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

    @PersistenceContext
    private EntityManager entityManager;

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

    // METODO PARA CONSULTAR LOS DOMICILIARIOS POR FILTROS.
    @Override
    public List<DomiciliarioDTO> domiciliariosFiltro(DomiciliarioDTO domiciliarioDTO) {
        log.debug("Request to get domicilaries per filters");

        //Map en donde se van guardando los filtros.
        Map<String, Object> filtros = new HashMap<String, Object>();

        //StringBuilder para ir construyendo la cosulta
        StringBuilder sb = new StringBuilder();

        // Consulta base
        sb.append(Constants.BASE_DOMICILIARY);

        if (domiciliarioDTO.getNombre() != null && !domiciliarioDTO.getNombre().isEmpty()) {
            filtros.put("nombre", "%" + domiciliarioDTO.getNombre().toUpperCase() + "%");
            sb.append(Constants.DOMICILIARY_PER_NAME);
        }

        if (domiciliarioDTO.getSalario() != null) {
            filtros.put("tipoSalario", TipoSalario.valueOf(domiciliarioDTO.getSalario().toString()));
            sb.append(Constants.DOMICILIARY_PER_TYPE_SALARY);
        }

        if (domiciliarioDTO.getHorario() != null && !domiciliarioDTO.getHorario().isEmpty()) {
            filtros.put("horario", domiciliarioDTO.getHorario());
            sb.append(Constants.DOMICILIARY_PER_SHEDULED);
        }

        if (domiciliarioDTO.getEstado() != null) {
            filtros.put("estado", EstadoDomiciliario.valueOf(domiciliarioDTO.getEstado().toString()));
            sb.append(Constants.DOMICILIARY_PER_STATE);
        }

        Query q = entityManager.createQuery(sb.toString());

        for (Map.Entry<String, Object> filtro : filtros.entrySet()) {
            q.setParameter(filtro.getKey(), filtro.getValue());
        }

        List<Domiciliario> domiciliaries = q.getResultList();

        return domiciliaries.stream().map(domiciliarioMapper::toDto).toList();
    }
}
