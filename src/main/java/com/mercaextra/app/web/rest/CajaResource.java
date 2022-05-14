package com.mercaextra.app.web.rest;

import com.mercaextra.app.repository.CajaRepository;
import com.mercaextra.app.service.CajaService;
import com.mercaextra.app.service.dto.CajaDTO;
import com.mercaextra.app.web.rest.errors.BadRequestAlertException;
import java.math.BigDecimal;
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
 * REST controller for managing {@link com.mercaextra.app.domain.Caja}.
 */
@RestController
@RequestMapping("/api")
public class CajaResource {

    private final Logger log = LoggerFactory.getLogger(CajaResource.class);

    private static final String ENTITY_NAME = "caja";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CajaService cajaService;

    private final CajaRepository cajaRepository;

    public CajaResource(CajaService cajaService, CajaRepository cajaRepository) {
        this.cajaService = cajaService;
        this.cajaRepository = cajaRepository;
    }

    /**
     * {@code POST  /cajas} : Create a new caja.
     *
     * @param cajaDTO the cajaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cajaDTO, or with status {@code 400 (Bad Request)} if the caja has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cajas")
    public ResponseEntity<CajaDTO> createCaja(@RequestBody CajaDTO cajaDTO) throws URISyntaxException {
        log.debug("REST request to save Caja : {}", cajaDTO);
        if (cajaDTO.getId() != null) {
            throw new BadRequestAlertException("A new caja cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CajaDTO result = cajaService.save(cajaDTO);
        return ResponseEntity
            .created(new URI("/api/cajas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cajas/:id} : Updates an existing caja.
     *
     * @param id the id of the cajaDTO to save.
     * @param cajaDTO the cajaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cajaDTO,
     * or with status {@code 400 (Bad Request)} if the cajaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cajaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cajas/{id}")
    public ResponseEntity<CajaDTO> updateCaja(@PathVariable(value = "id", required = false) final Long id, @RequestBody CajaDTO cajaDTO)
        throws URISyntaxException {
        log.debug("REST request to update Caja : {}, {}", id, cajaDTO);
        if (cajaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cajaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cajaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CajaDTO result = cajaService.save(cajaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cajaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cajas/:id} : Partial updates given fields of an existing caja, field will ignore if it is null
     *
     * @param id the id of the cajaDTO to save.
     * @param cajaDTO the cajaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cajaDTO,
     * or with status {@code 400 (Bad Request)} if the cajaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cajaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cajaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cajas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CajaDTO> partialUpdateCaja(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CajaDTO cajaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Caja partially : {}, {}", id, cajaDTO);
        if (cajaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cajaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cajaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CajaDTO> result = cajaService.partialUpdate(cajaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cajaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cajas} : get all the cajas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cajas in body.
     */
    @GetMapping("/cajas")
    public List<CajaDTO> getAllCajas() {
        log.debug("REST request to get all Cajas");
        return cajaService.findAll();
    }

    @GetMapping("cajas-valor-dia")
    public ResponseEntity<BigDecimal> valorVendidoDia() {
        log.debug("REST request to get total daily value (Invoice)");

        BigDecimal valorVendidoDia = cajaService.valorVendidoDia();

        return new ResponseEntity<BigDecimal>(valorVendidoDia, HttpStatus.OK);
    }

    @GetMapping("/cajas-remember-creation")
    public ResponseEntity<Integer> RememberCreationCaja() {
        log.debug("REST request to remember creation caja");
        int resp = cajaService.RememberCreationCaja();
        return ResponseEntity.ok().body(resp);
    }

    /**
     * {@code GET  /cajas/:id} : get the "id" caja.
     *
     * @param id the id of the cajaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cajaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cajas/{id}")
    public ResponseEntity<CajaDTO> getCaja(@PathVariable Long id) {
        log.debug("REST request to get Caja : {}", id);
        Optional<CajaDTO> cajaDTO = cajaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cajaDTO);
    }

    /**
     * {@code DELETE  /cajas/:id} : delete the "id" caja.
     *
     * @param id the id of the cajaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cajas/{id}")
    public ResponseEntity<Void> deleteCaja(@PathVariable Long id) {
        log.debug("REST request to delete Caja : {}", id);
        cajaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
