package com.mercaextra.app.service;

import com.mercaextra.app.service.dto.ProductoDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mercaextra.app.domain.Producto}.
 */
public interface ProductoService {
    /**
     * Save a producto.
     *
     * @param productoDTO the entity to save.
     * @return the persisted entity.
     */
    ProductoDTO save(ProductoDTO productoDTO);

    /**
     * Partially updates a producto.
     *
     * @param productoDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductoDTO> partialUpdate(ProductoDTO productoDTO);

    /**
     * Get all the productos.
     *
     * @return the list of entities.
     */
    List<ProductoDTO> findAll();

    /**
     * Get the "id" producto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductoDTO> findOne(Long id);

    /**
     * Delete the "id" producto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /* Traer todos los productos por categoria */
    List<ProductoDTO> productoPorCategoria(String categoria);

    // Productos agotados
    List<ProductoDTO> productosAgotados();

    // productos escasos
    List<ProductoDTO> productosEnEscases();

    //productos por fitro
    List<ProductoDTO> productoFiltros(ProductoDTO producto);

    List<ProductoDTO> productosFiltroCategoria(int opcion, String categoria);

    void aplicarPorcentajePrecio(String opcion, double cantidad);

    List<ProductoDTO> productosSimilares(ProductoDTO producto);

    List<ProductoDTO> allProducts();

    List<ProductoDTO> discountProductHome();
}
