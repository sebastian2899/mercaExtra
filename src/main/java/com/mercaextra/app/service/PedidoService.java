package com.mercaextra.app.service;

import com.mercaextra.app.service.dto.FacturaPedidoDTO;
import com.mercaextra.app.service.dto.PedidoDTO;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mercaextra.app.domain.Pedido}.
 */
public interface PedidoService {
    /**
     * Save a pedido.
     *
     * @param pedidoDTO the entity to save.
     * @return the persisted entity.
     */
    PedidoDTO save(PedidoDTO pedidoDTO);

    /**
     * Partially updates a pedido.
     *
     * @param pedidoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PedidoDTO> partialUpdate(PedidoDTO pedidoDTO);

    /**
     * Get all the pedidos.
     *
     * @return the list of entities.
     */
    List<PedidoDTO> findAll();

    /**
     * Get the "id" pedido.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PedidoDTO> findOne(Long id);

    /**
     * Delete the "id" pedido.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<FacturaPedidoDTO> facturasLogin();

    int validarDomiciliario();

    PedidoDTO pedidoEntrega();

    void pedidoFinalizado(PedidoDTO pedidoDTO);

    List<PedidoDTO> pedidosFecha(String fecha);
}
