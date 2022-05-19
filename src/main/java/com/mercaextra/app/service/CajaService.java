package com.mercaextra.app.service;

import com.mercaextra.app.service.dto.CajaDTO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mercaextra.app.domain.Caja}.
 */
public interface CajaService {
    /**
     * Save a caja.
     *
     * @param cajaDTO the entity to save.
     * @return the persisted entity.
     */
    CajaDTO save(CajaDTO cajaDTO);

    /**
     * Partially updates a caja.
     *
     * @param cajaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CajaDTO> partialUpdate(CajaDTO cajaDTO);

    /**
     * Get all the cajas.
     *
     * @return the list of entities.
     */
    List<CajaDTO> findAll();

    /**
     * Get the "id" caja.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CajaDTO> findOne(Long id);

    /**
     * Delete the "id" caja.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    BigDecimal valorVendidoDia();

    int RememberCreationCaja();
}
