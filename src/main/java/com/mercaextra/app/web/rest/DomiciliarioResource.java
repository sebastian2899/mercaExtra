package com.mercaextra.app.web.rest;

import com.mercaextra.app.repository.DomiciliarioRepository;
import com.mercaextra.app.service.DomiciliarioService;
import com.mercaextra.app.service.dto.DomiciliarioDTO;
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
 * REST controller for managing {@link com.mercaextra.app.domain.Domiciliario}.
 */
@RestController
@RequestMapping("/api")
public class DomiciliarioResource {

    private final Logger log = LoggerFactory.getLogger(DomiciliarioResource.class);

    private static final String ENTITY_NAME = "domiciliario";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DomiciliarioService domiciliarioService;

    private final DomiciliarioRepository domiciliarioRepository;

    public DomiciliarioResource(DomiciliarioService domiciliarioService, DomiciliarioRepository domiciliarioRepository) {
        this.domiciliarioService = domiciliarioService;
        this.domiciliarioRepository = domiciliarioRepository;
    }

    /**
     * {@code POST  /domiciliarios} : Create a new domiciliario.
     *
     * @param domiciliarioDTO the domiciliarioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new domiciliarioDTO, or with status {@code 400 (Bad Request)} if the domiciliario has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/domiciliarios")
    public ResponseEntity<DomiciliarioDTO> createDomiciliario(@RequestBody DomiciliarioDTO domiciliarioDTO) throws URISyntaxException {
        log.debug("REST request to save Domiciliario : {}", domiciliarioDTO);
        if (domiciliarioDTO.getId() != null) {
            throw new BadRequestAlertException("A new domiciliario cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DomiciliarioDTO result = domiciliarioService.save(domiciliarioDTO);
        return ResponseEntity
            .created(new URI("/api/domiciliarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /domiciliarios/:id} : Updates an existing domiciliario.
     *
     * @param id the id of the domiciliarioDTO to save.
     * @param domiciliarioDTO the domiciliarioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated domiciliarioDTO,
     * or with status {@code 400 (Bad Request)} if the domiciliarioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the domiciliarioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/domiciliarios/{id}")
    public ResponseEntity<DomiciliarioDTO> updateDomiciliario(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DomiciliarioDTO domiciliarioDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Domiciliario : {}, {}", id, domiciliarioDTO);
        if (domiciliarioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, domiciliarioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!domiciliarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DomiciliarioDTO result = domiciliarioService.save(domiciliarioDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, domiciliarioDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /domiciliarios/:id} : Partial updates given fields of an existing domiciliario, field will ignore if it is null
     *
     * @param id the id of the domiciliarioDTO to save.
     * @param domiciliarioDTO the domiciliarioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated domiciliarioDTO,
     * or with status {@code 400 (Bad Request)} if the domiciliarioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the domiciliarioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the domiciliarioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/domiciliarios/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DomiciliarioDTO> partialUpdateDomiciliario(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DomiciliarioDTO domiciliarioDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Domiciliario partially : {}, {}", id, domiciliarioDTO);
        if (domiciliarioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, domiciliarioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!domiciliarioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DomiciliarioDTO> result = domiciliarioService.partialUpdate(domiciliarioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, domiciliarioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /domiciliarios} : get all the domiciliarios.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of domiciliarios in body.
     */
    @GetMapping("/domiciliarios")
    public List<DomiciliarioDTO> getAllDomiciliarios() {
        log.debug("REST request to get all Domiciliarios");
        return domiciliarioService.findAll();
    }

    /**
     * {@code GET  /domiciliarios/:id} : get the "id" domiciliario.
     *
     * @param id the id of the domiciliarioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the domiciliarioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/domiciliarios/{id}")
    public ResponseEntity<DomiciliarioDTO> getDomiciliario(@PathVariable Long id) {
        log.debug("REST request to get Domiciliario : {}", id);
        Optional<DomiciliarioDTO> domiciliarioDTO = domiciliarioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(domiciliarioDTO);
    }

    /**
     * {@code DELETE  /domiciliarios/:id} : delete the "id" domiciliario.
     *
     * @param id the id of the domiciliarioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/domiciliarios/{id}")
    public ResponseEntity<Void> deleteDomiciliario(@PathVariable Long id) {
        log.debug("REST request to delete Domiciliario : {}", id);
        domiciliarioService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
