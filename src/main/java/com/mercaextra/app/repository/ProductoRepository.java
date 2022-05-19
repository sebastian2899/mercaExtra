package com.mercaextra.app.repository;

import com.mercaextra.app.domain.Producto;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Producto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    @Query("SELECT p FROM Producto p WHERE p.categoria=:categoria")
    List<Producto> productuosPorCategoria(@Param("categoria") String categoria);

    @Query("SELECT p FROM Producto p WHERE p.cantidad BETWEEN 1 AND 10")
    List<Producto> productosEnEscases();

    @Query("SELECT p FROM Producto p WHERE p.cantidad = 0")
    List<Producto> productosAgotados();

    @Query("SELECT p.precio FROM Producto p WHERE p.id=:id")
    BigDecimal precioProductoPorId(@Param("id") Long id);

    @Query(
        value = "SELECT * FROM Producto AS p WHERE p.id NOT IN (:id) AND p.categoria =:categoria ORDER BY rand() LIMIT 3",
        nativeQuery = true
    )
    List<Producto> proctosSimilares(@Param("id") Long id, @Param("categoria") String categoria);

    @Query(value = "SELECT * FROM producto WHERE precio_descuento IS NOT NULL ORDER BY rand() LIMIT 4", nativeQuery = true)
    List<Producto> discountProductHome();
}
