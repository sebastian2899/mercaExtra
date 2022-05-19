package com.mercaextra.app.repository;

import com.mercaextra.app.domain.Compra;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Compra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompraRepository extends JpaRepository<Compra, Long> {
    @Query(
        "SELECT p.nombre,i.cantidad,i.precio FROM ItemFacturaCompra i INNER JOIN Producto p ON i.idProducto = p.id INNER JOIN" +
        " Compra c ON i.idFactura = :id WHERE c.id = :id"
    )
    List<Object[]> itemsPerCompra(@Param("id") Long idFactura);
}
