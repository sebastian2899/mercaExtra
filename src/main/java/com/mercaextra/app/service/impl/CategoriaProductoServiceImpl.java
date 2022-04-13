package com.mercaextra.app.service.impl;

import com.mercaextra.app.domain.CategoriaProducto;
import com.mercaextra.app.repository.CategoriaProductoRepository;
import com.mercaextra.app.service.CategoriaProductoService;
import com.mercaextra.app.service.dto.CategoriaProductoDTO;
import com.mercaextra.app.service.mapper.CategoriaProductoMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CategoriaProducto}.
 */
@Service
@Transactional
public class CategoriaProductoServiceImpl implements CategoriaProductoService {

    private final Logger log = LoggerFactory.getLogger(CategoriaProductoServiceImpl.class);

    private final CategoriaProductoRepository categoriaProductoRepository;

    private final CategoriaProductoMapper categoriaProductoMapper;

    public CategoriaProductoServiceImpl(
        CategoriaProductoRepository categoriaProductoRepository,
        CategoriaProductoMapper categoriaProductoMapper
    ) {
        this.categoriaProductoRepository = categoriaProductoRepository;
        this.categoriaProductoMapper = categoriaProductoMapper;
    }

    @Override
    public CategoriaProductoDTO save(CategoriaProductoDTO categoriaProductoDTO) {
        log.debug("Request to save CategoriaProducto : {}", categoriaProductoDTO);
        CategoriaProducto categoriaProducto = categoriaProductoMapper.toEntity(categoriaProductoDTO);
        categoriaProducto = categoriaProductoRepository.save(categoriaProducto);
        return categoriaProductoMapper.toDto(categoriaProducto);
    }

    @Override
    public Optional<CategoriaProductoDTO> partialUpdate(CategoriaProductoDTO categoriaProductoDTO) {
        log.debug("Request to partially update CategoriaProducto : {}", categoriaProductoDTO);

        return categoriaProductoRepository
            .findById(categoriaProductoDTO.getId())
            .map(existingCategoriaProducto -> {
                categoriaProductoMapper.partialUpdate(existingCategoriaProducto, categoriaProductoDTO);

                return existingCategoriaProducto;
            })
            .map(categoriaProductoRepository::save)
            .map(categoriaProductoMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaProductoDTO> findAll() {
        log.debug("Request to get all CategoriaProductos");
        return categoriaProductoRepository
            .findAll()
            .stream()
            .map(categoriaProductoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoriaProductoDTO> findOne(Long id) {
        log.debug("Request to get CategoriaProducto : {}", id);
        return categoriaProductoRepository.findById(id).map(categoriaProductoMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CategoriaProducto : {}", id);
        categoriaProductoRepository.deleteById(id);
    }
}
