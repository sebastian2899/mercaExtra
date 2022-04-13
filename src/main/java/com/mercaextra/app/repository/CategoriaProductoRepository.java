package com.mercaextra.app.repository;

import com.mercaextra.app.domain.CategoriaProducto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CategoriaProducto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoriaProductoRepository extends JpaRepository<CategoriaProducto, Long> {}
