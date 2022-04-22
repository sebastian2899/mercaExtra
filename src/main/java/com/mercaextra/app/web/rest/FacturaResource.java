package com.mercaextra.app.web.rest;

import com.mercaextra.app.repository.FacturaRepository;
import com.mercaextra.app.service.FacturaService;
import com.mercaextra.app.service.dto.FacturaDTO;
import com.mercaextra.app.service.dto.ProductoDTO;
import com.mercaextra.app.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mercaextra.app.domain.Factura}.
 */
@RestController
@RequestMapping("/api")
public class FacturaResource {

    private final Logger log = LoggerFactory.getLogger(FacturaResource.class);

    private static final String ENTITY_NAME = "factura";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FacturaService facturaService;

    private final FacturaRepository facturaRepository;

    public FacturaResource(FacturaService facturaService, FacturaRepository facturaRepository) {
        this.facturaService = facturaService;
        this.facturaRepository = facturaRepository;
    }

    /**
     * {@code POST  /facturas} : Create a new factura.
     *
     * @param facturaDTO the facturaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new facturaDTO, or with status {@code 400 (Bad Request)} if the factura has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/facturas")
    public ResponseEntity<FacturaDTO> createFactura(@RequestBody FacturaDTO facturaDTO) throws URISyntaxException {
        log.debug("REST request to save Factura : {}", facturaDTO);
        if (facturaDTO.getId() != null) {
            throw new BadRequestAlertException("A new factura cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FacturaDTO result = facturaService.save(facturaDTO);
        return ResponseEntity
            .created(new URI("/api/facturas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @PostMapping("factura-rePurcharse")
    public ResponseEntity<FacturaDTO> rePurcharseFactura(@RequestBody FacturaDTO facturaDTO) throws URISyntaxException {
        log.debug("REST request to repurchase factura", facturaDTO);
        if (facturaDTO.getId() == null) {
            throw new BadRequestAlertException("A new factura cannot already have an id", ENTITY_NAME, "idexist");
        }
        FacturaDTO factura = facturaService.repurcharseInvoice(facturaDTO);
        return ResponseEntity
            .created(new URI("api/factura-rePurcharse" + factura.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, factura.getId().toString()))
            .body(factura);
    }

    /**
     * {@code PUT  /facturas/:id} : Updates an existing factura.
     *
     * @param id the id of the facturaDTO to save.
     * @param facturaDTO the facturaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facturaDTO,
     * or with status {@code 400 (Bad Request)} if the facturaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the facturaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/facturas/{id}")
    public ResponseEntity<FacturaDTO> updateFactura(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FacturaDTO facturaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Factura : {}, {}", id, facturaDTO);
        if (facturaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facturaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facturaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FacturaDTO result = facturaService.save(facturaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facturaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /facturas/:id} : Partial updates given fields of an existing factura, field will ignore if it is null
     *
     * @param id the id of the facturaDTO to save.
     * @param facturaDTO the facturaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated facturaDTO,
     * or with status {@code 400 (Bad Request)} if the facturaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the facturaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the facturaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/facturas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FacturaDTO> partialUpdateFactura(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FacturaDTO facturaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Factura partially : {}, {}", id, facturaDTO);
        if (facturaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, facturaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!facturaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FacturaDTO> result = facturaService.partialUpdate(facturaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, facturaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /facturas} : get all the facturas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of facturas in body.
     */
    @GetMapping("/facturas")
    public List<FacturaDTO> getAllFacturas() {
        log.debug("REST request to get all Facturas");
        return facturaService.findAll();
    }

    @GetMapping("/facturas-usuario")
    public List<FacturaDTO> facturasUsuario() {
        log.debug("REST request to get facturas per user");
        return facturaService.facturasPorUsuario();
    }

    @GetMapping("/facturas-productos-disponibles")
    public List<ProductoDTO> productosDisponibles() {
        return facturaService.productosDisponibles();
    }

    @GetMapping("/factura-productos-categoria/{categoria}")
    public List<ProductoDTO> productosCategoria(@PathVariable String categoria) {
        log.debug("REST request to get all productos per categoria", categoria);
        return facturaService.productosCategoria(categoria);
    }

    /**
     * {@code GET  /facturas/:id} : get the "id" factura.
     *
     * @param id the id of the facturaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the facturaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/facturas/{id}")
    public ResponseEntity<FacturaDTO> getFactura(@PathVariable Long id) {
        log.debug("REST request to get Factura : {}", id);
        FacturaDTO facturaDTO = facturaService.findOne(id);
        return new ResponseEntity<FacturaDTO>(facturaDTO, HttpStatus.OK);
    }

    /**
     * {@code DELETE  /facturas/:id} : delete the "id" factura.
     *
     * @param id the id of the facturaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/facturas/{id}")
    public ResponseEntity<Void> deleteFactura(@PathVariable Long id) {
        log.debug("REST request to delete Factura : {}", id);
        facturaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
