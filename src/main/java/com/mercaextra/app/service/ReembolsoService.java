package com.mercaextra.app.service;

import com.mercaextra.app.service.dto.PedidoDTO;
import com.mercaextra.app.service.dto.ReembolsoDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mercaextra.app.domain.Reembolso}.
 */
public interface ReembolsoService {
    /**
     * Save a reembolso.
     *
     * @param reembolsoDTO the entity to save.
     * @return the persisted entity.
     */
    ReembolsoDTO save(ReembolsoDTO reembolsoDTO);

    /**
     * Partially updates a reembolso.
     *
     * @param reembolsoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ReembolsoDTO> partialUpdate(ReembolsoDTO reembolsoDTO);

    /**
     * Get all the reembolsos.
     *
     * @return the list of entities.
     */
    List<ReembolsoDTO> findAll();

    /**
     * Get the "id" reembolso.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReembolsoDTO> findOne(Long id);

    /**
     * Delete the "id" reembolso.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<PedidoDTO> pedidosExpirados();
}
