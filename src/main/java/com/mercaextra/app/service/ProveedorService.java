package com.mercaextra.app.service;

import com.mercaextra.app.service.dto.ProveedorDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mercaextra.app.domain.Proveedor}.
 */
public interface ProveedorService {
    /**
     * Save a proveedor.
     *
     * @param proveedorDTO the entity to save.
     * @return the persisted entity.
     */
    ProveedorDTO save(ProveedorDTO proveedorDTO);

    /**
     * Partially updates a proveedor.
     *
     * @param proveedorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProveedorDTO> partialUpdate(ProveedorDTO proveedorDTO);

    /**
     * Get all the proveedors.
     *
     * @return the list of entities.
     */
    List<ProveedorDTO> findAll();

    /**
     * Get the "id" proveedor.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProveedorDTO> findOne(Long id);

    /**
     * Delete the "id" proveedor.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
