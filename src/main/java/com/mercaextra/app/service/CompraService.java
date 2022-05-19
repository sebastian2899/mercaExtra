package com.mercaextra.app.service;

import com.mercaextra.app.service.dto.CompraDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mercaextra.app.domain.Compra}.
 */
public interface CompraService {
    /**
     * Save a compra.
     *
     * @param compraDTO the entity to save.
     * @return the persisted entity.
     */
    CompraDTO save(CompraDTO compraDTO);

    /**
     * Partially updates a compra.
     *
     * @param compraDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CompraDTO> partialUpdate(CompraDTO compraDTO);

    /**
     * Get all the compras.
     *
     * @return the list of entities.
     */
    List<CompraDTO> findAll();

    /**
     * Get the "id" compra.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CompraDTO> findOne(Long id);

    /**
     * Delete the "id" compra.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<CompraDTO> comprasFiltros(CompraDTO comprafecha, String fecha);
}
