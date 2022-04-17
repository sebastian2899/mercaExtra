package com.mercaextra.app.service.impl;

import com.mercaextra.app.config.Constants;
import com.mercaextra.app.domain.Factura;
import com.mercaextra.app.domain.ItemFacturaVenta;
import com.mercaextra.app.domain.Producto;
import com.mercaextra.app.repository.FacturaRepository;
import com.mercaextra.app.repository.ItemFacturaVentaRepository;
import com.mercaextra.app.service.FacturaService;
import com.mercaextra.app.service.dto.FacturaDTO;
import com.mercaextra.app.service.dto.ProductoDTO;
import com.mercaextra.app.service.mapper.FacturaMapper;
import com.mercaextra.app.service.mapper.ProductoMapper;
import java.util.LinkedList;
import java.util.List;
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
 * Service Implementation for managing {@link Factura}.
 */
@Service
@Transactional
public class FacturaServiceImpl implements FacturaService {

    private final Logger log = LoggerFactory.getLogger(FacturaServiceImpl.class);

    private final FacturaRepository facturaRepository;

    private final FacturaMapper facturaMapper;

    private final ItemFacturaVentaRepository itemFacturaVentaRepository;

    private final ProductoMapper productosMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public FacturaServiceImpl(
        FacturaRepository facturaRepository,
        FacturaMapper facturaMapper,
        ItemFacturaVentaRepository itemFacturaVentaRepository,
        ProductoMapper productosMapper
    ) {
        this.facturaRepository = facturaRepository;
        this.facturaMapper = facturaMapper;
        this.itemFacturaVentaRepository = itemFacturaVentaRepository;
        this.productosMapper = productosMapper;
    }

    @Override
    public FacturaDTO save(FacturaDTO facturaDTO) {
        log.debug("Request to save Factura : {}", facturaDTO);
        Factura factura = facturaMapper.toEntity(facturaDTO);
        factura = facturaRepository.save(factura);

        ItemFacturaVenta itemFacturaVenta = null;

        if (factura.getItemsPorFactura() != null) {
            for (ItemFacturaVenta item : factura.getItemsPorFactura()) {
                itemFacturaVenta = new ItemFacturaVenta();
                itemFacturaVenta.setIdFactura(factura.getId());
                itemFacturaVenta.setIdProducto(item.getIdProducto());
                itemFacturaVenta.setCantidad(item.getCantidad());
                itemFacturaVenta.setPrecio(item.getPrecio());

                //GUARDO EL ITEM
                itemFacturaVentaRepository.save(itemFacturaVenta);

                //SE RESTAN LA CANTIDAD DE PRODUCTOS VENDIDOS (INDIVIDUAL)
                Query q = entityManager
                    .createQuery(Constants.RESTAR_PRODUCTOS_VENDIDOS)
                    .setParameter("cantidad", itemFacturaVenta.getCantidad())
                    .setParameter("id", itemFacturaVenta.getId());

                q.executeUpdate();
            }
        }

        return facturaMapper.toDto(factura);
    }

    @Override
    public Optional<FacturaDTO> partialUpdate(FacturaDTO facturaDTO) {
        log.debug("Request to partially update Factura : {}", facturaDTO);

        return facturaRepository
            .findById(facturaDTO.getId())
            .map(existingFactura -> {
                facturaMapper.partialUpdate(existingFactura, facturaDTO);

                return existingFactura;
            })
            .map(facturaRepository::save)
            .map(facturaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacturaDTO> findAll() {
        log.debug("Request to get all Facturas");
        return facturaRepository.findAll().stream().map(facturaMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional
    @Override
    public List<ProductoDTO> productosDisponibles() {
        return facturaRepository
            .productosDisponibles()
            .stream()
            .map(productosMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public FacturaDTO findOne(Long id) {
        log.debug("Request to get Factura : {}", id);
        Factura factura = facturaRepository.facturaId(id);
        factura.setItemsPorFactura(consultarItemsPorFactura(id));
        return facturaMapper.toDto(factura);
    }

    private List<ItemFacturaVenta> consultarItemsPorFactura(Long id) {
        log.debug("Request to get all items per factura.", id);

        List<ItemFacturaVenta> itemsFactura = itemFacturaVentaRepository.itemsPorFactura(id);
        for (ItemFacturaVenta item : itemsFactura) {
            String nombreProducto = facturaRepository.nombreProdcuto(item.getIdProducto());
            item.setNombreProducto(nombreProducto);
        }

        return itemsFactura;
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Factura : {}", id);
        facturaRepository.deleteById(id);
    }

    @Override
    public List<ProductoDTO> productosCategoria(String categoria) {
        log.debug("Request to get productos per categoria", categoria);

        List<Producto> productosCategoria = facturaRepository.productoPorCategoria(categoria);

        return productosCategoria.stream().map(productosMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }
}
