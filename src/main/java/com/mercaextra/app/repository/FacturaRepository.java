package com.mercaextra.app.repository;

import com.mercaextra.app.domain.Factura;
import com.mercaextra.app.domain.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Factura entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    @Query("SELECT p.nombre FROM Producto p WHERE p.id=:id")
    String nombreProdcuto(@Param("id") Long id);

    @Query("SELECT f FROM Factura f WHERE f.id=:id")
    Factura facturaId(@Param("id") Long id);

    @Query("SELECT p FROM Producto p WHERE p.cantidad > 0")
    List<Producto> productosDisponibles();

    @Query("SELECT p FROM Producto p WHERE p.categoria = :categoria")
    List<Producto> productoPorCategoria(@Param("categoria") String categoria);
}
