package com.mercaextra.app.repository;

import com.mercaextra.app.domain.Domiciliario;
import com.mercaextra.app.domain.Pedido;
import com.mercaextra.app.domain.enumeration.EstadoDomiciliario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Pedido entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    @Query(
        "SELECT f.infoCiente, f.numeroFactura,f.valorFactura, f.estadoFactura,f.id FROM Factura f WHERE f.userName = :userName AND " +
        "f.estadoFactura = 'lista'"
    )
    List<Object[]> facturasCliente(@Param("userName") String userName);

    @Query("SELECT d.id FROM Domiciliario d WHERE d.estado = :estado")
    List<Long> domiciliariosDisponibles(@Param("estado") EstadoDomiciliario estado);

    @Query("SELECT p.estado, p.direccion, p.infoDomicilio FROM Pedido p WHERE p.userName =:userName AND p.estado ='Entregando'")
    String[] pedidoEnEntrega(@Param("userName") String userName);

    @Query("SELECT d.nombre FROM Domiciliario d WHERE d.id =:id")
    String nombreDomiciliario(@Param("id") Long id);

    @Query("SELECT p FROM Pedido p WHERE p.estado = 'Entregando' AND p.userName = :userName")
    Pedido pedidoEntrega(@Param("userName") String userName);

    @Query(
        "SELECT p.fechaPedido,p.direccion,p.infoDomicilio FROM Pedido p WHERE DATE_FORMAT(p.fechaPedido,'%Y-%m-%d')=:fecha" +
        " AND p.userName = :userName"
    )
    List<Object[]> pedidosFecha(@Param("fecha") String fecha, @Param("userName") String userName);

    @Query(
        value = "SELECT CASE WHEN EXISTS (SELECT p.estado FROM pedido p WHERE p.estado = 'Entregando' AND p.user_name =:userName)" +
        " THEN 'true' ELSE 'false' END",
        nativeQuery = true
    )
    String existingOrder(@Param("userName") String userName);

    @Query("SELECT n.descripcion FROM Notificacion n WHERE n.id =:idNotificacion")
    String descripcionNotificacion(@Param("idNotificacion") Long idNotificacion);
}
