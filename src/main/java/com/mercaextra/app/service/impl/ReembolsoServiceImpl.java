package com.mercaextra.app.service.impl;

import com.mercaextra.app.domain.Pedido;
import com.mercaextra.app.domain.Reembolso;
import com.mercaextra.app.repository.PedidoRepository;
import com.mercaextra.app.repository.ReembolsoRepository;
import com.mercaextra.app.service.ReembolsoService;
import com.mercaextra.app.service.UserService;
import com.mercaextra.app.service.dto.DatosPedidoReembolsoDTO;
import com.mercaextra.app.service.dto.ReembolsoDTO;
import com.mercaextra.app.service.mapper.ReembolsoMapper;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Reembolso}.
 */
@Service
@Transactional
public class ReembolsoServiceImpl implements ReembolsoService {

    private final Logger log = LoggerFactory.getLogger(ReembolsoServiceImpl.class);

    private final ReembolsoRepository reembolsoRepository;

    private final ReembolsoMapper reembolsoMapper;

    private final PedidoRepository pedidoRepository;

    private UserService userService;

    public ReembolsoServiceImpl(
        ReembolsoRepository reembolsoRepository,
        ReembolsoMapper reembolsoMapper,
        UserService userService,
        PedidoRepository pedidoRepository
    ) {
        this.reembolsoRepository = reembolsoRepository;
        this.reembolsoMapper = reembolsoMapper;
        this.userService = userService;
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public ReembolsoDTO save(ReembolsoDTO reembolsoDTO) {
        log.debug("Request to save Reembolso : {}", reembolsoDTO);
        Reembolso reembolso = reembolsoMapper.toEntity(reembolsoDTO);

        // Consultamos el pedido para cambiarlo de estado.
        if (reembolso.getIdPedido() != null) {
            Pedido pedido = pedidoRepository.getById(reembolso.getIdPedido());
            pedido.setEstado("Reembolso en estudio");
            pedidoRepository.save(pedido);
        }

        if (reembolsoDTO.getId() == null) {
            reembolso.setEstado("Reembolso en estudio");
        }

        reembolso = reembolsoRepository.save(reembolso);
        return reembolsoMapper.toDto(reembolso);
    }

    @Override
    public Optional<ReembolsoDTO> partialUpdate(ReembolsoDTO reembolsoDTO) {
        log.debug("Request to partially update Reembolso : {}", reembolsoDTO);

        return reembolsoRepository
            .findById(reembolsoDTO.getId())
            .map(existingReembolso -> {
                reembolsoMapper.partialUpdate(existingReembolso, reembolsoDTO);

                return existingReembolso;
            })
            .map(reembolsoRepository::save)
            .map(reembolsoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReembolsoDTO> findAll() {
        log.debug("Request to get all Reembolsos");
        return reembolsoRepository.findAll().stream().map(reembolsoMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ReembolsoDTO> findOne(Long id) {
        log.debug("Request to get Reembolso : {}", id);
        return reembolsoRepository.findById(id).map(reembolsoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Reembolso : {}", id);
        reembolsoRepository.deleteById(id);
    }

    @Override
    public List<DatosPedidoReembolsoDTO> pedidosExpirados() {
        log.debug("Request to get all expired orders");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String userName = userService.getUserWithAuthorities().get().getLogin();

        DatosPedidoReembolsoDTO pedido = null;
        List<DatosPedidoReembolsoDTO> pedidosExpirados = new ArrayList<>();

        List<Object[]> expirados = reembolsoRepository.pedidosReembolso(userName);

        for (Object[] expirado : expirados) {
            pedido = new DatosPedidoReembolsoDTO();
            pedido.setIdPedido(Long.parseLong(expirado[0].toString()));
            String fecha = expirado[1].toString().substring(0, expirado[1].toString().indexOf("T")).toString();
            pedido.setFechaPedido(fecha);
            pedido.setPedidoDireccion(expirado[2].toString());
            pedido.setValorFactura(new BigDecimal(expirado[3].toString()));
            pedido.setIdFactura(Long.parseLong(expirado[4].toString()));
            pedido.setDomiciliario(expirado[5].toString());
            pedido.setIdDomiciliario(Long.parseLong(expirado[6].toString()));
            pedidosExpirados.add(pedido);
        }

        return pedidosExpirados.stream().collect(Collectors.toCollection(LinkedList::new));
    }
}
