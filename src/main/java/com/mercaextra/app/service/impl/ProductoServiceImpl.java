package com.mercaextra.app.service.impl;

import com.mercaextra.app.config.Constants;
import com.mercaextra.app.domain.Producto;
import com.mercaextra.app.repository.ProductoRepository;
import com.mercaextra.app.service.ProductoService;
import com.mercaextra.app.service.dto.ProductoDTO;
import com.mercaextra.app.service.mapper.ProductoMapper;
import java.math.BigDecimal;
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
 * Service Implementation for managing {@link Producto}.
 */
@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final Logger log = LoggerFactory.getLogger(ProductoServiceImpl.class);

    private final ProductoRepository productoRepository;

    private final ProductoMapper productoMapper;

    @PersistenceContext
    private EntityManager entityManager;

    public ProductoServiceImpl(ProductoRepository productoRepository, ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
    }

    @Override
    public ProductoDTO save(ProductoDTO productoDTO) {
        log.debug("Request to save Producto : {}", productoDTO);
        Producto producto = productoMapper.toEntity(productoDTO);
        producto = productoRepository.save(producto);
        return productoMapper.toDto(producto);
    }

    @Override
    public Optional<ProductoDTO> partialUpdate(ProductoDTO productoDTO) {
        log.debug("Request to partially update Producto : {}", productoDTO);

        return productoRepository
            .findById(productoDTO.getId())
            .map(existingProducto -> {
                productoMapper.partialUpdate(existingProducto, productoDTO);

                return existingProducto;
            })
            .map(productoRepository::save)
            .map(productoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> findAll() {
        log.debug("Request to get all Productos");

        Query q = entityManager.createQuery("SELECT p FROM" + " Producto p WHERE p.cantidad > 0 ORDER BY p.categoria ASC");

        List<Producto> productos = q.getResultList();

        return productos.stream().map(productoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductoDTO> findOne(Long id) {
        log.debug("Request to get Producto : {}", id);
        return productoRepository.findById(id).map(productoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Producto : {}", id);
        productoRepository.deleteById(id);
    }

    @Override
    public List<ProductoDTO> productoPorCategoria(String categoria) {
        log.debug("Request to get all productos per category");

        List<Producto> productosPorCategoria = productoRepository.productuosPorCategoria(categoria);

        return productosPorCategoria.stream().map(productoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /*
     * SE TRAEN TODOS LOS PRODUCTOS QUE TIENEN UNA CANTIDAD DE 0.
     * */
    @Override
    public List<ProductoDTO> productosAgotados() {
        log.debug("Request to get all unviable productos");
        List<Producto> productosAgotados = productoRepository.productosAgotados();
        return productosAgotados.stream().map(productoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /*
     * SE TRAEN TODOS LOS PRODUCTOS QUE TIENEN UNA CANTIDAD MAYOR A 0 PERO SU CANTIDAD ES MENOR A 10.
     * */
    @Override
    public List<ProductoDTO> productosEnEscases() {
        log.debug("Request to get products ");
        List<Producto> productosEnEscases = productoRepository.productosEnEscases();
        return productosEnEscases.stream().map(productoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<ProductoDTO> productoFiltros(ProductoDTO producto) {
        log.debug("Request to get products per filter");

        StringBuilder sb = new StringBuilder();
        Map<String, Object> filtros = new HashMap<String, Object>();

        //SE AGREGA LA CONSULTA QUE TRAE A LOS PRODUCTOS POR DEFECTO.
        sb.append(Constants.PRODUCTO_BASE);

        //SE VALIDA SI EL NOMBRE QUE SE ENVIO ES DIFERENTE DE NULO PARA CONSULTAR POR NOMBRE.
        if (producto.getNombre() != null && !producto.getNombre().isEmpty()) {
            //SE AGREGA A LA CONSULTA EL PRODUCTOS POR EL NOMBRE ENVIADO
            sb.append(Constants.PRODUCTO_NOMBRE);
            filtros.put("nombre", "%" + producto.getNombre().toUpperCase() + "%");
        }

        //SE VALIDA QUE LA DESCRIPCION SEA DIFERENTE DE NULA PARA FILTRAR POR DESCRIPCION
        if (producto.getDescripcion() != null && !producto.getDescripcion().isEmpty()) {
            //SE AGREGA A LA CONSULTA EL FILTRO DESCRIPCION
            sb.append(Constants.PRODUCTO_DESCRIPCION);
            filtros.put("descripcion", "%" + producto.getDescripcion().toUpperCase() + "%");
        }

        // SE CREA LA CONSULTA Y LE ASIGNAN LOS FILTROS CORRESPONDIENTES

        Query q = entityManager.createQuery(sb.toString());

        for (Map.Entry<String, Object> filtro : filtros.entrySet()) {
            q.setParameter(filtro.getKey(), filtro.getValue());
        }

        List<Producto> productosFiltro = q.getResultList();

        return productosFiltro.stream().map(productoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<ProductoDTO> productosFiltroCategoria(int opcion, String categoria) {
        StringBuilder sb = new StringBuilder();
        List<Producto> productos = null;

        if (categoria != null && !categoria.isEmpty()) {
            if (opcion == 1) {
                sb.append(Constants.PRODUCTOS_ORDENADOS_PRECIO_CATEGORIA);
            } else if (opcion == 2) {
                sb.append(Constants.PRODUCTOS_ORDENAOS_ALFABETICAMENTE_CATEGORIA);
            }

            Query q = entityManager.createQuery(sb.toString());
            q.setParameter("categoria", categoria);
            productos = q.getResultList();
        } else {
            if (opcion == 1) {
                sb.append(Constants.PRODUCTOS_ORDENADOS_PRECIO);
            } else if (opcion == 2) {
                sb.append(Constants.PRODUCTOS_ORDENADOS_ALFABETICAMENTE);
            }

            Query q = entityManager.createQuery(sb.toString());
            productos = q.getResultList();
        }

        /*
         * Map<String,Object>filtros = new HashMap<String,Object>();
         *
         * if(opcion == 1) { if(categoria != null && !categoria.isEmpty()) {
         * sb.append(Constants.PRODUCTOS_ORDENADOS_PRECIO_CATEGORIA);
         * filtros.put("categoria", categoria); }else {
         * sb.append(Constants.PRODUCTOS_ORDENADOS_PRECIO); } }else if(opcion == 2) {
         * if(categoria != null && !categoria.isEmpty()) {
         * sb.append(Constants.PRODUCTOS_ORDENAOS_ALFABETICAMENTE_CATEGORIA);
         * filtros.put("categoria", categoria); }else {
         * sb.append(Constants.PRODUCTOS_ORDENADOS_ALFABETICAMENTE); } }
         */

        return productos.stream().map(productoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> allProducts() {
        log.debug("Request to get all products no matter amount");

        return productoRepository.findAll().stream().map(productoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public void aplicarPorcentajePrecio(String opcion, double cantidad) {
        log.debug("Request to change value of all products where option: ", opcion, " and value: ", cantidad);

        List<Producto> productos = productoRepository.findAll();

        for (Producto producto : productos) {
            BigDecimal porcentaje = (producto.getPrecio().multiply(BigDecimal.valueOf(cantidad))).divide(new BigDecimal(100));

            // OPCIONAL(SE PUEDEN QUITAR LOS PUNTOS DECIMALES DE EL DESCUENTO O DECREMENTO APLICADO EN EL Math.pow(10, 1), REEMPLZAR EL 1 POR 0)
            switch (opcion) {
                case "aumentar":
                    double precioProdut = producto.getPrecio().add(porcentaje).doubleValue();
                    double precioProductoFormater = Math.round(precioProdut * Math.pow(10, 1)) / Math.pow(10, 1);
                    producto.setPrecio(BigDecimal.valueOf(precioProductoFormater));

                    break;
                case "decrementar":
                    double precioProdutSubs = producto.getPrecio().subtract(porcentaje).doubleValue();
                    double precioProductoSubsFormater = Math.round(precioProdutSubs * Math.pow(10, 1)) / Math.pow(10, 1);
                    producto.setPrecio(BigDecimal.valueOf(precioProductoSubsFormater));

                    break;
                default:
                    break;
            }

            productoRepository.save(producto);
        }
    }

    /*
     * METODO PARA CONSULTAR LOS PRODUCTOS SIMILARES AL VER EL DETALLE DE UN
     * PRODUCTO. SOLO SE CONSULTAN 3 PRODUCTOS QUE ESTEN DENTRO DE LA MISMA
     * CATEGORIA DEL PRODUCTO QUE SE ESTA CONSULTADO, DE ESOS 3 PRODUCTOS
     * RECOLECTADOS DE LA BD NO SE PUEDE REPETIR EL MISMO PRODUCTO QUE SE ESTA
     * CONSULTANDO
     */
    @Override
    public List<ProductoDTO> productosSimilares(ProductoDTO producto) {
        log.debug("Request to get similar products");

        // SE ENVIAN LOS 2 PARAMETROS QUE EJECUTAN LA CONSULTA CORRECTAMENTE.
        List<Producto> productosSimilares = productoRepository.proctosSimilares(producto.getId(), producto.getCategoria());

        return productosSimilares.stream().map(productoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public List<ProductoDTO> discountProductHome() {
        log.debug("Request to get 4 products whit aviable discount");
        return productoRepository.discountProductHome().stream().map(productoMapper::toDto).toList();
    }
}
