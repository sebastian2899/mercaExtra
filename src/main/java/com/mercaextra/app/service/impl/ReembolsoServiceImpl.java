package com.mercaextra.app.service.impl;

import com.mercaextra.app.domain.Reembolso;
import com.mercaextra.app.repository.ReembolsoRepository;
import com.mercaextra.app.service.ReembolsoService;
import com.mercaextra.app.service.UserService;
import com.mercaextra.app.service.dto.PedidoDTO;
import com.mercaextra.app.service.dto.ReembolsoDTO;
import com.mercaextra.app.service.mapper.ReembolsoMapper;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
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

    private UserService userService;

    public ReembolsoServiceImpl(ReembolsoRepository reembolsoRepository, ReembolsoMapper reembolsoMapper, UserService userService) {
        this.reembolsoRepository = reembolsoRepository;
        this.reembolsoMapper = reembolsoMapper;
        this.userService = userService;
    }

    @Override
    public ReembolsoDTO save(ReembolsoDTO reembolsoDTO) {
        log.debug("Request to save Reembolso : {}", reembolsoDTO);
        Reembolso reembolso = reembolsoMapper.toEntity(reembolsoDTO);
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
    public List<PedidoDTO> pedidosExpirados() {
        log.debug("Request to get all expired orders");

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        String userName = userService.getUserWithAuthorities().get().getLogin();

        PedidoDTO pedido = null;
        List<PedidoDTO> pedidosExpirados = new ArrayList<>();

        List<Object[]> expirados = reembolsoRepository.pedidosReembolso(userName);

        for (Object[] expirado : expirados) {
            pedido = new PedidoDTO();
            try {
                Instant fecha = format.parse(expirado[0].toString().substring(0, expirado[0].toString().indexOf("T"))).toInstant();
                pedido.setFechaPedido(fecha);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            pedido.setDireccion(expirado[1].toString());
            pedido.setInfoDomicilio(expirado[2].toString());

            pedidosExpirados.add(pedido);
        }

        return pedidosExpirados.stream().collect(Collectors.toCollection(LinkedList::new));
    }
}
