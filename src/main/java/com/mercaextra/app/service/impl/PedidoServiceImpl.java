package com.mercaextra.app.service.impl;

import com.mercaextra.app.config.Constants;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
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

    @PersistenceContext
    private EntityManager entityManager;

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

        if (pedidoDTO.getId() == null) {
            //cambiamos el estado de la factura de lista a comprada
            Factura factura = facturaRepositoty.facturaId(pedido.getIdFactura());
            factura.setEstadoFactura("Comprada");
            facturaRepositoty.save(factura);

            /* De la lista recuperada por el metodo que valida la disponibilidad de
             * domiciliarios disponibles, se usa la clase random para eligir cualquiera de
             * los domicilios disponibles para que haga el pedido.
             */
            Random random_method = new Random();
            Long idDomiciliario = (long) random_method.nextInt(aviableDomiciliary.size());
            Long idDomiciliarioIndex = aviableDomiciliary.get(idDomiciliario.intValue());
            pedido.setIdDomiciliario(idDomiciliarioIndex);

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

    /*
     * METODO PARA VALIDAR EL ENVIO DEL PEDIDO,
     * VALIDACION 1: SI HAY DOMICILIARIOS
     * EN ESTADO DISPONIBLE SE PODRA REALIZAR EL PEDIDO, POR EL CONTRARIO SI NO
     * HAY DISPONIBILIDAD DE DOMICILIARIOS NO SE PUDE REALIZAR. (RETORNAR 1)
     *
     * VALIDACION 2: SI EL USUARIO YA TIENE UN PEDIDO EN CAMINO NO PODRA REALIZAR 2
     * VECES UN PEDIDO, PODRA TENER LAS FACTURAS QUE QUIERA PERO PEDIDOD SOLO TENDRA
     * UNO. (RETORNAR 2)
     *
     * POR ULTIMO (RETORNAR 3) CUANDO EL PEDIDO CUMPLA CON LOS
     * REQUISITOS PARA PODER SER ENTREGADO CON EXITO.
     */
    @Override
    public int validarDomiciliario() {
        log.debug("Request to validate aviable domiciliary");

        int resp;
        //CONSULTAMOS LOS DOMICILIARIOS QUE ESTEN DISPONBILES
        aviableDomiciliary = pedidoRepository.domiciliariosDisponibles(EstadoDomiciliario.DISPONIBLE);

        /*
         * SE VALIDA QUE EL USUSARIO NO TENGA PEDIDOS EN ENTREGA, SI ESTE LOS TIENE,
         * TENDRA QUE ESPERAR A QUE SU PEDIDO SEA ENTREGADO PARA PODER REALIZAR OTRO
         * PEDIDO
         */

        //SE RECUPERA EL USER
        String login = userService.getUserWithAuthorities().get().getLogin();

        //SE CASTEA LA RESPUESTA DEL REPOSITOTY DE STRING A BOOLEAN
        boolean answerOrderComming = Boolean.parseBoolean(pedidoRepository.existingOrder(login));

        //SE HACE LAS VALIDACIONES, SI LA LISTA DE LOS DOMICILIARIOS ESTA VACIA Y SI EXISTE UN PEDIDO EN PROGRESO
        if (aviableDomiciliary.isEmpty()) {
            resp = 1;
        } else if (answerOrderComming) {
            resp = 2;
        } else {
            resp = 3;
        }

        /*
         * si no hay domiciliarios disponibles no se podra efectuar el pedido y se
         * debera esperar que un domiciliario este disponible.
         */
        return resp;
    }

    private String descpNotificacion(Long id) {
        log.debug("Request to asignate description message notification");
        return pedidoRepository.descripcionNotificacion(id);
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

        /*
         * Para el metodo findAll que es llamado por el componente pedido en el front,
         * tenenos que validar que usuario esta realizando la peticion, y de acuerdo con
         * el usuario que haga dicha peticion, se le retornas los datos correspondientes
         * de ese usuario.
         */

        String userName = userService.getUserWithAuthorities().get().getLogin();

        if (userName.equals("admin")) {
            return pedidoRepository.findAll().stream().map(pedidoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
        } else {
            Query q = entityManager.createQuery(Constants.TRAER_PEDIDOS_POR_USUARIO).setParameter("userName", userName);

            List<Object[]> pedidosObject = q.getResultList();

            PedidoDTO pedido = null;
            List<PedidoDTO> pedidosDTO = new ArrayList<>();

            for (Object[] pedidoObject : pedidosObject) {
                pedido = new PedidoDTO();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Instant pedidoInstant = format
                        .parse(pedidoObject[0].toString().substring(0, pedidoObject[0].toString().indexOf("T")))
                        .toInstant();
                    pedido.setFechaPedido(pedidoInstant);
                    pedido.setDireccion(pedidoObject[1].toString());
                    pedido.setInfoDomicilio(pedidoObject[2].toString());
                    pedidosDTO.add(pedido);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            return pedidosDTO;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PedidoDTO> findOne(Long id) {
        log.debug("Request to get Pedido : {}", id);
        Optional<Pedido> pedido = pedidoRepository.findById(id);
        pedido.get().setDescripcionNotificacion(descpNotificacion(pedido.get().getIdNotificacion()));
        return pedido.map(pedidoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pedido : {}", id);
        pedidoRepository.deleteById(id);
    }

    /*
     * metodo para consultar los factura que estan listas para poder realizae un
     * pedido en la seccion update de angular en la entidad pedido.
     * Se crea un dto con los datos especififcos para no consultar todos los campos de la factuira
     * si no slo los campos necesarios.
     */
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

    @Override
    public void pedidoFinalizado(PedidoDTO pedidoDTO) {
        log.debug("Request to change state pedido to finalised");

        //Se asigna el usuario logueado.
        String userName = userService.getUserWithAuthorities().get().getLogin();

        Pedido pedido = pedidoMapper.toEntity(pedidoDTO);

        /*
         * Cuando el pedido ya este finalizado, el estado camnbia a finalizado, tambien
         * cambie la notificacion y el estado del domiciliario que estaba repartiendo el
         * pedido, pasara a disponible nuevamente.
         */

        //se consulta el domiciliario que fue asignado para repartir el pedido.
        Domiciliario domiciliario = domiciliarioRepository.domiciliarioPorId(pedido.getIdDomiciliario());
        domiciliario.setEstado(EstadoDomiciliario.DISPONIBLE);
        domiciliarioRepository.save(domiciliario);

        pedido.setEstado("Finalizado");
        pedido.setUserName(userName);
        pedido.setIdNotificacion(2L);

        pedidoRepository.save(pedido);
    }

    @Override
    public List<PedidoDTO> pedidosFecha(String fecha) {
        log.debug("Request to get all pedidos per fecha", fecha);

        //Se formatea la fecha de instant a String para hacer la consulta con tipo de dato String y no Instant
        String fechaFormat = fecha.substring(0, fecha.indexOf("T"));
        String userName = userService.getUserWithAuthorities().get().getLogin();

        //Se recupera la lista object
        List<Object[]> pedidosObject = pedidoRepository.pedidosFecha(fechaFormat, userName);

        //Se instancia una clase Pedido y un arrayList de Pedido que sera retornada
        PedidoDTO pedidoNew = null;
        List<PedidoDTO> pedidosDTO = new ArrayList<>();

        //Se recorre a lista y se setan los valores correspondientes a la clase instanciada
        for (Object[] pedido : pedidosObject) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            pedidoNew = new PedidoDTO();
            try {
                Instant fechaInstant = format.parse(pedido[0].toString().substring(0, pedido[0].toString().indexOf("T"))).toInstant();
                pedidoNew.setFechaPedido(fechaInstant);
                pedidoNew.setDireccion(pedido[1].toString());
                pedidoNew.setInfoDomicilio(pedido[2].toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            pedidosDTO.add(pedidoNew);
        }

        return pedidosDTO;
    }
    /*
     * CREAR TAREA PROGRAMADA PARA MONITOREAR LA ENTREGA DEL PEDIDO, CADA 30 MINUTOS
     * EN CASO DEL PEDIDO NO SER ENTREGADO EN EL TIEMPO ESTIMADO, AUTOMATICAMENTE
     * DESPUES DE 30 MINUTOS EL PEDIDO CAMBIARA DE ESTO
     */

    /*
     * public void expiredOrder() {
     * log.debug("Request to expired order by time out");
     *
     *
     *
     * Pedido pedido = pedidoRepository.pedidoEntrega();
     *
     * if(pedido != null) { pedido.setEstado("Finalizado");
     * pedidoRepository.save(pedido); } }
     */
}
