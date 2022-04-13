package com.mercaextra.app.repository;

import com.mercaextra.app.domain.Egreso;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Egreso entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EgresoRepository extends JpaRepository<Egreso, Long> {}
