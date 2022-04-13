package com.mercaextra.app.repository;

import com.mercaextra.app.domain.Factura;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Factura entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {}
