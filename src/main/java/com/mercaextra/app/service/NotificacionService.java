package com.mercaextra.app.service;

import com.mercaextra.app.service.dto.NotificacionDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mercaextra.app.domain.Notificacion}.
 */
public interface NotificacionService {
    /**
     * Save a notificacion.
     *
     * @param notificacionDTO the entity to save.
     * @return the persisted entity.
     */
    NotificacionDTO save(NotificacionDTO notificacionDTO);

    /**
     * Partially updates a notificacion.
     *
     * @param notificacionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<NotificacionDTO> partialUpdate(NotificacionDTO notificacionDTO);

    /**
     * Get all the notificacions.
     *
     * @return the list of entities.
     */
    List<NotificacionDTO> findAll();

    /**
     * Get the "id" notificacion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<NotificacionDTO> findOne(Long id);

    /**
     * Delete the "id" notificacion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
