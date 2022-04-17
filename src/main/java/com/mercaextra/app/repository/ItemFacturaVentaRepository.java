package com.mercaextra.app.repository;

import com.mercaextra.app.domain.ItemFacturaVenta;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ItemFacturaVenta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemFacturaVentaRepository extends JpaRepository<ItemFacturaVenta, Long> {
    @Query("SELECT i FROM ItemFacturaVenta i WHERE i.id =:id")
    List<ItemFacturaVenta> itemsPorFactura(@Param("id") Long id);
}
