package com.mercaextra.app.service.impl;

import com.mercaextra.app.domain.Pedido;
import com.mercaextra.app.repository.PedidoRepository;
import com.mercaextra.app.service.PedidoService;
import com.mercaextra.app.service.dto.PedidoDTO;
import com.mercaextra.app.service.mapper.PedidoMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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

    public PedidoServiceImpl(PedidoRepository pedidoRepository, PedidoMapper pedidoMapper) {
        this.pedidoRepository = pedidoRepository;
        this.pedidoMapper = pedidoMapper;
    }

    @Override
    public PedidoDTO save(PedidoDTO pedidoDTO) {
        log.debug("Request to save Pedido : {}", pedidoDTO);
        Pedido pedido = pedidoMapper.toEntity(pedidoDTO);
        pedido = pedidoRepository.save(pedido);
        return pedidoMapper.toDto(pedido);
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
}
