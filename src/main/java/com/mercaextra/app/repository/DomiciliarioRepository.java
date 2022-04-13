package com.mercaextra.app.repository;

import com.mercaextra.app.domain.Domiciliario;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Domiciliario entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DomiciliarioRepository extends JpaRepository<Domiciliario, Long> {}
