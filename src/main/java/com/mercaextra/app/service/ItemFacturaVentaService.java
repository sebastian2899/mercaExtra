package com.mercaextra.app.service;

import com.mercaextra.app.service.dto.ItemFacturaVentaDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mercaextra.app.domain.ItemFacturaVenta}.
 */
public interface ItemFacturaVentaService {
    /**
     * Save a itemFacturaVenta.
     *
     * @param itemFacturaVentaDTO the entity to save.
     * @return the persisted entity.
     */
    ItemFacturaVentaDTO save(ItemFacturaVentaDTO itemFacturaVentaDTO);

    /**
     * Partially updates a itemFacturaVenta.
     *
     * @param itemFacturaVentaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ItemFacturaVentaDTO> partialUpdate(ItemFacturaVentaDTO itemFacturaVentaDTO);

    /**
     * Get all the itemFacturaVentas.
     *
     * @return the list of entities.
     */
    List<ItemFacturaVentaDTO> findAll();

    /**
     * Get the "id" itemFacturaVenta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ItemFacturaVentaDTO> findOne(Long id);

    /**
     * Delete the "id" itemFacturaVenta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
