package com.mercaextra.app.web.rest;

import com.mercaextra.app.repository.ItemFacturaVentaRepository;
import com.mercaextra.app.service.ItemFacturaVentaService;
import com.mercaextra.app.service.dto.ItemFacturaVentaDTO;
import com.mercaextra.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mercaextra.app.domain.ItemFacturaVenta}.
 */
@RestController
@RequestMapping("/api")
public class ItemFacturaVentaResource {

    private final Logger log = LoggerFactory.getLogger(ItemFacturaVentaResource.class);

    private static final String ENTITY_NAME = "itemFacturaVenta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemFacturaVentaService itemFacturaVentaService;

    private final ItemFacturaVentaRepository itemFacturaVentaRepository;

    public ItemFacturaVentaResource(
        ItemFacturaVentaService itemFacturaVentaService,
        ItemFacturaVentaRepository itemFacturaVentaRepository
    ) {
        this.itemFacturaVentaService = itemFacturaVentaService;
        this.itemFacturaVentaRepository = itemFacturaVentaRepository;
    }

    /**
     * {@code POST  /item-factura-ventas} : Create a new itemFacturaVenta.
     *
     * @param itemFacturaVentaDTO the itemFacturaVentaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemFacturaVentaDTO, or with status {@code 400 (Bad Request)} if the itemFacturaVenta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-factura-ventas")
    public ResponseEntity<ItemFacturaVentaDTO> createItemFacturaVenta(@RequestBody ItemFacturaVentaDTO itemFacturaVentaDTO)
        throws URISyntaxException {
        log.debug("REST request to save ItemFacturaVenta : {}", itemFacturaVentaDTO);
        if (itemFacturaVentaDTO.getId() != null) {
            throw new BadRequestAlertException("A new itemFacturaVenta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemFacturaVentaDTO result = itemFacturaVentaService.save(itemFacturaVentaDTO);
        return ResponseEntity
            .created(new URI("/api/item-factura-ventas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-factura-ventas/:id} : Updates an existing itemFacturaVenta.
     *
     * @param id the id of the itemFacturaVentaDTO to save.
     * @param itemFacturaVentaDTO the itemFacturaVentaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemFacturaVentaDTO,
     * or with status {@code 400 (Bad Request)} if the itemFacturaVentaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemFacturaVentaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-factura-ventas/{id}")
    public ResponseEntity<ItemFacturaVentaDTO> updateItemFacturaVenta(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ItemFacturaVentaDTO itemFacturaVentaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ItemFacturaVenta : {}, {}", id, itemFacturaVentaDTO);
        if (itemFacturaVentaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemFacturaVentaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemFacturaVentaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ItemFacturaVentaDTO result = itemFacturaVentaService.save(itemFacturaVentaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemFacturaVentaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /item-factura-ventas/:id} : Partial updates given fields of an existing itemFacturaVenta, field will ignore if it is null
     *
     * @param id the id of the itemFacturaVentaDTO to save.
     * @param itemFacturaVentaDTO the itemFacturaVentaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemFacturaVentaDTO,
     * or with status {@code 400 (Bad Request)} if the itemFacturaVentaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the itemFacturaVentaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemFacturaVentaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/item-factura-ventas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ItemFacturaVentaDTO> partialUpdateItemFacturaVenta(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ItemFacturaVentaDTO itemFacturaVentaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemFacturaVenta partially : {}, {}", id, itemFacturaVentaDTO);
        if (itemFacturaVentaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemFacturaVentaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemFacturaVentaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ItemFacturaVentaDTO> result = itemFacturaVentaService.partialUpdate(itemFacturaVentaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemFacturaVentaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /item-factura-ventas} : get all the itemFacturaVentas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemFacturaVentas in body.
     */
    @GetMapping("/item-factura-ventas")
    public List<ItemFacturaVentaDTO> getAllItemFacturaVentas() {
        log.debug("REST request to get all ItemFacturaVentas");
        return itemFacturaVentaService.findAll();
    }

    /**
     * {@code GET  /item-factura-ventas/:id} : get the "id" itemFacturaVenta.
     *
     * @param id the id of the itemFacturaVentaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemFacturaVentaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-factura-ventas/{id}")
    public ResponseEntity<ItemFacturaVentaDTO> getItemFacturaVenta(@PathVariable Long id) {
        log.debug("REST request to get ItemFacturaVenta : {}", id);
        Optional<ItemFacturaVentaDTO> itemFacturaVentaDTO = itemFacturaVentaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemFacturaVentaDTO);
    }

    /**
     * {@code DELETE  /item-factura-ventas/:id} : delete the "id" itemFacturaVenta.
     *
     * @param id the id of the itemFacturaVentaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-factura-ventas/{id}")
    public ResponseEntity<Void> deleteItemFacturaVenta(@PathVariable Long id) {
        log.debug("REST request to delete ItemFacturaVenta : {}", id);
        itemFacturaVentaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
