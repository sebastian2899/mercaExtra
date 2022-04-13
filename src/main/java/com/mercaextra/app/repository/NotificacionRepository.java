package com.mercaextra.app.repository;

import com.mercaextra.app.domain.Notificacion;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Notificacion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {}
