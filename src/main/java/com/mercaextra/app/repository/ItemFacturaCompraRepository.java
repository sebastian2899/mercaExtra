package com.mercaextra.app.repository;

import com.mercaextra.app.domain.ItemFacturaCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemFacturaCompraRepository extends JpaRepository<ItemFacturaCompra, Long> {}
