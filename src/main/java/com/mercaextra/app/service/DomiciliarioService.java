package com.mercaextra.app.service;

import com.mercaextra.app.service.dto.DomiciliarioDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mercaextra.app.domain.Domiciliario}.
 */
public interface DomiciliarioService {
    /**
     * Save a domiciliario.
     *
     * @param domiciliarioDTO the entity to save.
     * @return the persisted entity.
     */
    DomiciliarioDTO save(DomiciliarioDTO domiciliarioDTO);

    /**
     * Partially updates a domiciliario.
     *
     * @param domiciliarioDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<DomiciliarioDTO> partialUpdate(DomiciliarioDTO domiciliarioDTO);

    /**
     * Get all the domiciliarios.
     *
     * @return the list of entities.
     */
    List<DomiciliarioDTO> findAll();

    /**
     * Get the "id" domiciliario.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<DomiciliarioDTO> findOne(Long id);

    /**
     * Delete the "id" domiciliario.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
