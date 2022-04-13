package com.mercaextra.app.service.impl;

import com.mercaextra.app.domain.Notificacion;
import com.mercaextra.app.repository.NotificacionRepository;
import com.mercaextra.app.service.NotificacionService;
import com.mercaextra.app.service.dto.NotificacionDTO;
import com.mercaextra.app.service.mapper.NotificacionMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Notificacion}.
 */
@Service
@Transactional
public class NotificacionServiceImpl implements NotificacionService {

    private final Logger log = LoggerFactory.getLogger(NotificacionServiceImpl.class);

    private final NotificacionRepository notificacionRepository;

    private final NotificacionMapper notificacionMapper;

    public NotificacionServiceImpl(NotificacionRepository notificacionRepository, NotificacionMapper notificacionMapper) {
        this.notificacionRepository = notificacionRepository;
        this.notificacionMapper = notificacionMapper;
    }

    @Override
    public NotificacionDTO save(NotificacionDTO notificacionDTO) {
        log.debug("Request to save Notificacion : {}", notificacionDTO);
        Notificacion notificacion = notificacionMapper.toEntity(notificacionDTO);
        notificacion = notificacionRepository.save(notificacion);
        return notificacionMapper.toDto(notificacion);
    }

    @Override
    public Optional<NotificacionDTO> partialUpdate(NotificacionDTO notificacionDTO) {
        log.debug("Request to partially update Notificacion : {}", notificacionDTO);

        return notificacionRepository
            .findById(notificacionDTO.getId())
            .map(existingNotificacion -> {
                notificacionMapper.partialUpdate(existingNotificacion, notificacionDTO);

                return existingNotificacion;
            })
            .map(notificacionRepository::save)
            .map(notificacionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificacionDTO> findAll() {
        log.debug("Request to get all Notificacions");
        return notificacionRepository.findAll().stream().map(notificacionMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<NotificacionDTO> findOne(Long id) {
        log.debug("Request to get Notificacion : {}", id);
        return notificacionRepository.findById(id).map(notificacionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Notificacion : {}", id);
        notificacionRepository.deleteById(id);
    }
}
