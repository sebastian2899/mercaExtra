package com.mercaextra.app.service.impl;

import com.mercaextra.app.domain.Domiciliario;
import com.mercaextra.app.domain.Factura;
import com.mercaextra.app.domain.Pedido;
import com.mercaextra.app.domain.enumeration.EstadoDomiciliario;
import com.mercaextra.app.repository.DomiciliarioRepository;
import com.mercaextra.app.repository.FacturaRepository;
import com.mercaextra.app.repository.PedidoRepository;
import com.mercaextra.app.service.PedidoService;
import com.mercaextra.app.service.UserService;
import com.mercaextra.app.service.dto.FacturaPedidoDTO;
import com.mercaextra.app.service.dto.PedidoDTO;
import com.mercaextra.app.service.mapper.PedidoMapper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Pedido}.
 */
@Service
@Transactional
public class PedidoServiceImpl implements PedidoService {

    private final Logger log = LoggerFactory.getLogger(PedidoServiceImpl.class);

    private final PedidoRepository pedidoRepository;

    private final PedidoMapper pedidoMapper;

    private final UserService userService;

    private final FacturaRepository facturaRepositoty;

    private final DomiciliarioRepository domiciliarioRepository;

    private static List<Long> aviableDomiciliary = null;

    public PedidoServiceImpl(
        PedidoRepository pedidoRepository,
        PedidoMapper pedidoMapper,
        UserService userService,
        FacturaRepository facturaRepositoty,
        DomiciliarioRepository domiciliarioRepository
    ) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoMapper = pedidoMapper;
        this.userService = userService;
        this.facturaRepositoty = facturaRepositoty;
        this.domiciliarioRepository = domiciliarioRepository;
    }

    @Override
    public PedidoDTO save(PedidoDTO pedidoDTO) {
        log.debug("Request to save Pedido : {}", pedidoDTO);
        Pedido pedido = pedidoMapper.toEntity(pedidoDTO);

        //se consulta el usuario quye acaba de generar el pedido
        String userName = userService.getUserWithAuthorities().get().getLogin();
        pedido.setUserName(userName);

        /* De la lista recuperada por el metodo que valida la disponibilidad de
         * domiciliarios disponibles, se usa la clase random para eligir cualquiera de
         * los domicilios disponibles para que haga el pedido.
         */
        Random random_method = new Random();
        Long idDomiciliario = (long) random_method.nextInt(aviableDomiciliary.size());
        Long idDomiciliarioIndex = aviableDomiciliary.get(idDomiciliario.intValue());
        pedido.setIdDomiciliario(idDomiciliarioIndex);

        //cambiamos el estado de la factura de lista a comprada
        Factura factura = facturaRepositoty.facturaId(pedido.getIdFactura());
        factura.setEstadoFactura("Comprada");
        facturaRepositoty.save(factura);

        if (pedidoDTO.getId() == null) {
            pedido.setIdNotificacion(1L);
            pedido.setEstado("Entregando");
            pedido.setInfoDomicilio(consultarDomiciliario(idDomiciliarioIndex));

            //despues de darle al pedido un domiciliario disponible, dicho domiciliario pasara a estado en entrega
            Domiciliario domiciliario = domiciliarioRepository.domiciliarioPorId(idDomiciliarioIndex);
            domiciliario.setEstado(EstadoDomiciliario.EN_ENTREGA);
            domiciliarioRepository.save(domiciliario);
        }

        pedido = pedidoRepository.save(pedido);
        return pedidoMapper.toDto(pedido);
    }

    @Override
    public PedidoDTO pedidoEntrega() {
        log.debug("Request to get pedido in comming state");
        //se recupera el usuario que acaba de hacer la peticion
        String userName = userService.getUserWithAuthorities().get().getLogin();

        //se consulta el pedido que esta siendo entregado
        Pedido pedido = pedidoRepository.pedidoEntrega(userName);

        return pedidoMapper.toDto(pedido);
    }

    //Metodo para consultar la informacion del domiciliario.
    private String consultarDomiciliario(Long id) {
        return pedidoRepository.nombreDomiciliario(id);
    }

    @Override
    public boolean validarDomiciliario() {
        log.debug("Request to validate aviable domiciliary");

        boolean resp;
        //CONSULTAMOS LOS DOMICILIARIOS QUE ESTEN DISPONBILES
        aviableDomiciliary = pedidoRepository.domiciliariosDisponibles(EstadoDomiciliario.DISPONIBLE);
        if (aviableDomiciliary != null) {
            resp = false;
        } else {
            resp = true;
        }

        /*
         * si no hay domiciliarios disponibles no se podra efectuar el pedido y se
         * debera esperar que un domiciliario este disponible.
         */
        return resp;
    }

    @Override
    public Optional<PedidoDTO> partialUpdate(PedidoDTO pedidoDTO) {
        log.debug("Request to partially update Pedido : {}", pedidoDTO);

        return pedidoRepository
            .findById(pedidoDTO.getId())
            .map(existingPedido -> {
                pedidoMapper.partialUpdate(existingPedido, pedidoDTO);

                return existingPedido;
            })
            .map(pedidoRepository::save)
            .map(pedidoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PedidoDTO> findAll() {
        log.debug("Request to get all Pedidos");
        return pedidoRepository.findAll().stream().map(pedidoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PedidoDTO> findOne(Long id) {
        log.debug("Request to get Pedido : {}", id);
        return pedidoRepository.findById(id).map(pedidoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pedido : {}", id);
        pedidoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<FacturaPedidoDTO> facturasLogin() {
        log.debug("Request to get all facturas per userName");

        String userName = userService.getUserWithAuthorities().get().getLogin();

        List<Object[]> valoresFactura = pedidoRepository.facturasCliente(userName);

        List<FacturaPedidoDTO> facturasReturn = new ArrayList<>();
        FacturaPedidoDTO facturaPedido = null;

        for (Object[] valorFactura : valoresFactura) {
            facturaPedido = new FacturaPedidoDTO();
            facturaPedido.setInfoCliente(valorFactura[0].toString());
            facturaPedido.setNumeroFactura(valorFactura[1].toString());
            facturaPedido.setValorFactura(new BigDecimal(valorFactura[2].toString()));
            facturaPedido.setEstadoFactura(valorFactura[3].toString());
            facturaPedido.setIdFactura(Long.parseLong(valorFactura[4].toString()));
            facturasReturn.add(facturaPedido);
        }

        return facturasReturn.stream().collect(Collectors.toCollection(LinkedList::new));
    }
}
