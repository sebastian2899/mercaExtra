package com.mercaextra.app.web.rest;

import com.mercaextra.app.repository.ReembolsoRepository;
import com.mercaextra.app.service.ReembolsoService;
import com.mercaextra.app.service.dto.PedidoDTO;
import com.mercaextra.app.service.dto.ReembolsoDTO;
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
 * REST controller for managing {@link com.mercaextra.app.domain.Reembolso}.
 */
@RestController
@RequestMapping("/api")
public class ReembolsoResource {

    private final Logger log = LoggerFactory.getLogger(ReembolsoResource.class);

    private static final String ENTITY_NAME = "reembolso";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReembolsoService reembolsoService;

    private final ReembolsoRepository reembolsoRepository;

    public ReembolsoResource(ReembolsoService reembolsoService, ReembolsoRepository reembolsoRepository) {
        this.reembolsoService = reembolsoService;
        this.reembolsoRepository = reembolsoRepository;
    }

    /**
     * {@code POST  /reembolsos} : Create a new reembolso.
     *
     * @param reembolsoDTO the reembolsoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reembolsoDTO, or with status {@code 400 (Bad Request)} if the reembolso has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reembolsos")
    public ResponseEntity<ReembolsoDTO> createReembolso(@RequestBody ReembolsoDTO reembolsoDTO) throws URISyntaxException {
        log.debug("REST request to save Reembolso : {}", reembolsoDTO);
        if (reembolsoDTO.getId() != null) {
            throw new BadRequestAlertException("A new reembolso cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReembolsoDTO result = reembolsoService.save(reembolsoDTO);
        return ResponseEntity
            .created(new URI("/api/reembolsos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reembolsos/:id} : Updates an existing reembolso.
     *
     * @param id the id of the reembolsoDTO to save.
     * @param reembolsoDTO the reembolsoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reembolsoDTO,
     * or with status {@code 400 (Bad Request)} if the reembolsoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reembolsoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reembolsos/{id}")
    public ResponseEntity<ReembolsoDTO> updateReembolso(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReembolsoDTO reembolsoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Reembolso : {}, {}", id, reembolsoDTO);
        if (reembolsoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reembolsoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reembolsoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReembolsoDTO result = reembolsoService.save(reembolsoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reembolsoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /reembolsos/:id} : Partial updates given fields of an existing reembolso, field will ignore if it is null
     *
     * @param id the id of the reembolsoDTO to save.
     * @param reembolsoDTO the reembolsoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reembolsoDTO,
     * or with status {@code 400 (Bad Request)} if the reembolsoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reembolsoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reembolsoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/reembolsos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReembolsoDTO> partialUpdateReembolso(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReembolsoDTO reembolsoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Reembolso partially : {}, {}", id, reembolsoDTO);
        if (reembolsoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reembolsoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reembolsoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReembolsoDTO> result = reembolsoService.partialUpdate(reembolsoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reembolsoDTO.getId().toString())
        );
    }

    @GetMapping("/reembolsos-pedidos")
    public List<PedidoDTO> pedidosExpirados() {
        log.debug("REST request to get all expired orders");
        return reembolsoService.pedidosExpirados();
    }

    /**
     * {@code GET  /reembolsos} : get all the reembolsos.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reembolsos in body.
     */
    @GetMapping("/reembolsos")
    public List<ReembolsoDTO> getAllReembolsos() {
        log.debug("REST request to get all Reembolsos");
        return reembolsoService.findAll();
    }

    /**
     * {@code GET  /reembolsos/:id} : get the "id" reembolso.
     *
     * @param id the id of the reembolsoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reembolsoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reembolsos/{id}")
    public ResponseEntity<ReembolsoDTO> getReembolso(@PathVariable Long id) {
        log.debug("REST request to get Reembolso : {}", id);
        Optional<ReembolsoDTO> reembolsoDTO = reembolsoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reembolsoDTO);
    }

    /**
     * {@code DELETE  /reembolsos/:id} : delete the "id" reembolso.
     *
     * @param id the id of the reembolsoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reembolsos/{id}")
    public ResponseEntity<Void> deleteReembolso(@PathVariable Long id) {
        log.debug("REST request to delete Reembolso : {}", id);
        reembolsoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
