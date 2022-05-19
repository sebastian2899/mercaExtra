package com.mercaextra.app.service.impl;

import com.mercaextra.app.config.Constants;
import com.mercaextra.app.domain.Compra;
import com.mercaextra.app.domain.ItemFacturaCompra;
import com.mercaextra.app.domain.enumeration.TipoFactura;
import com.mercaextra.app.repository.CompraRepository;
import com.mercaextra.app.repository.ItemFacturaCompraRepository;
import com.mercaextra.app.service.CompraService;
import com.mercaextra.app.service.dto.CompraDTO;
import com.mercaextra.app.service.dto.ItemFacturaCompraDTO;
import com.mercaextra.app.service.mapper.CompraMapper;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Compra}.
 */
@Service
@Transactional
public class CompraServiceImpl implements CompraService {

    private final Logger log = LoggerFactory.getLogger(CompraServiceImpl.class);

    private final CompraRepository compraRepository;

    private final CompraMapper compraMapper;

    private final ItemFacturaCompraRepository itemFacturaCompraRepository;

    @PersistenceContext
    private EntityManager entityManager;

    public CompraServiceImpl(
        CompraRepository compraRepository,
        CompraMapper compraMapper,
        ItemFacturaCompraRepository itemFacturaCompraRepository
    ) {
        this.compraRepository = compraRepository;
        this.compraMapper = compraMapper;
        this.itemFacturaCompraRepository = itemFacturaCompraRepository;
    }

    @Override
    public CompraDTO save(CompraDTO compraDTO) {
        log.debug("Request to save Compra : {}", compraDTO);
        Compra compra = compraMapper.toEntity(compraDTO);
        compra.setFechaCreacion(Instant.now());

        compra = compraRepository.save(compra);

        ItemFacturaCompra itemsCompra = null;

        if (compraDTO.getItemsFacturaCompra() != null && !compraDTO.getItemsFacturaCompra().isEmpty()) {
            for (ItemFacturaCompraDTO itemFactura : compraDTO.getItemsFacturaCompra()) {
                itemsCompra = new ItemFacturaCompra();
                itemsCompra.setIdFactura(compra.getId());
                itemsCompra.setIdProducto(itemFactura.getIdProducto());
                itemsCompra.setCantidad(itemFactura.getCantidad());
                itemsCompra.setPrecio(itemFactura.getPrecio());

                itemFacturaCompraRepository.save(itemsCompra);

                //se suman los productos comprados
                Query q = entityManager
                    .createQuery(Constants.INCREASE_AMOUNT_PRODUCTS)
                    .setParameter("cantidad", itemsCompra.getCantidad())
                    .setParameter("id", itemsCompra.getIdProducto());

                q.executeUpdate();
            }
        }
        return compraMapper.toDto(compra);
    }

    @Override
    public Optional<CompraDTO> partialUpdate(CompraDTO compraDTO) {
        log.debug("Request to partially update Compra : {}", compraDTO);

        return compraRepository
            .findById(compraDTO.getId())
            .map(existingCompra -> {
                compraMapper.partialUpdate(existingCompra, compraDTO);

                return existingCompra;
            })
            .map(compraRepository::save)
            .map(compraMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompraDTO> findAll() {
        log.debug("Request to get all Compras");
        return compraRepository.findAll().stream().map(compraMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    //compraMapper::toDto
    public Optional<CompraDTO> findOne(Long id) {
        log.debug("Request to get Compra : {}", id);
        return Optional
            .of(compraRepository.findById(id))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(compra -> {
                compra.setItemsFacturaCompra(aisgnarItemsCompra(id));
                return compra;
            })
            .map(compraMapper::toDto);
    }

    private List<ItemFacturaCompra> aisgnarItemsCompra(Long id) {
        log.debug("Request to get items per compra");

        List<Object[]> itemsCompra = compraRepository.itemsPerCompra(id);
        List<ItemFacturaCompra> itemsFactura = new ArrayList<>();

        ItemFacturaCompra item = null;
        for (Object[] itemCompra : itemsCompra) {
            item = new ItemFacturaCompra();
            item.setNombreProducto(itemCompra[0].toString());
            item.setCantidad(Long.parseLong(itemCompra[1].toString()));
            item.setPrecio(new BigDecimal(itemCompra[2].toString()));

            itemsFactura.add(item);
        }

        return itemsFactura;
    }

    @Override
    public List<CompraDTO> comprasFiltros(CompraDTO compra, String fecha) {
        log.debug("Request to get compras per filters");

        Map<String, Object> filtros = new HashMap<>();
        StringBuilder sb = new StringBuilder();

        // COMPRA BASE
        sb.append(Constants.BASE_COMPRA);

        if (compra.getTipoFactura() != null) {
            sb.append(Constants.COMPRA_TIPO_FACTURA);
            filtros.put("tipoFactura", TipoFactura.valueOf(compra.getTipoFactura().toString()));
        }

        if (compra.getEstado() != null && !compra.getEstado().isEmpty()) {
            sb.append(Constants.COMPRA_ESTADO);
            filtros.put("estado", compra.getEstado());
        }

        if (compra.getIdProveedor() != null) {
            sb.append(Constants.COMPRA_PROVEEDOR);
            filtros.put("idProveedor", compra.getIdProveedor());
        }

        if (compra.getNumeroFactura() != null && !compra.getNumeroFactura().isEmpty()) {
            sb.append(Constants.COMPRA_NUMERO_FACTURA);
            filtros.put("numeroFactura", compra.getNumeroFactura());
        }

        if (fecha != null && !fecha.isEmpty()) {
            String fechaFormat = fecha.substring(0, 10);

            sb.append(Constants.COMPRA_FECHA);
            filtros.put("fechaCreacion", fechaFormat);
        }

        Query q = entityManager.createQuery(sb.toString());

        // RECORREMOS EL MAP Y ASIGNAMOS LOS PARAMETROS
        for (Map.Entry<String, Object> filtro : filtros.entrySet()) {
            q.setParameter(filtro.getKey(), filtro.getValue());
        }

        List<Compra> compras = q.getResultList();

        return compras.stream().map(compraMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Compra : {}", id);
        compraRepository.deleteById(id);
    }
}
