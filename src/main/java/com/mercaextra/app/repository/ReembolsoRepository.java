package com.mercaextra.app.repository;

import com.mercaextra.app.domain.Pedido;
import com.mercaextra.app.domain.Reembolso;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Reembolso entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReembolsoRepository extends JpaRepository<Reembolso, Long> {
    @Query("SELECT p.fechaPedido,p.direccion,p.infoDomicilio FROM Pedido p WHERE p.userName=:userName AND p.estado = 'expirado'")
    List<Object[]> pedidosReembolso(@Param("userName") String userName);
}
