package com.mercaextra.app.repository;

import com.mercaextra.app.domain.Proveedor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Proveedor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {}
