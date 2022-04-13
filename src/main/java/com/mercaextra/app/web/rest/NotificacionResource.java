package com.mercaextra.app.web.rest;

import com.mercaextra.app.repository.NotificacionRepository;
import com.mercaextra.app.service.NotificacionService;
import com.mercaextra.app.service.dto.NotificacionDTO;
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
 * REST controller for managing {@link com.mercaextra.app.domain.Notificacion}.
 */
@RestController
@RequestMapping("/api")
public class NotificacionResource {

    private final Logger log = LoggerFactory.getLogger(NotificacionResource.class);

    private static final String ENTITY_NAME = "notificacion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificacionService notificacionService;

    private final NotificacionRepository notificacionRepository;

    public NotificacionResource(NotificacionService notificacionService, NotificacionRepository notificacionRepository) {
        this.notificacionService = notificacionService;
        this.notificacionRepository = notificacionRepository;
    }

    /**
     * {@code POST  /notificacions} : Create a new notificacion.
     *
     * @param notificacionDTO the notificacionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificacionDTO, or with status {@code 400 (Bad Request)} if the notificacion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/notificacions")
    public ResponseEntity<NotificacionDTO> createNotificacion(@RequestBody NotificacionDTO notificacionDTO) throws URISyntaxException {
        log.debug("REST request to save Notificacion : {}", notificacionDTO);
        if (notificacionDTO.getId() != null) {
            throw new BadRequestAlertException("A new notificacion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NotificacionDTO result = notificacionService.save(notificacionDTO);
        return ResponseEntity
            .created(new URI("/api/notificacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /notificacions/:id} : Updates an existing notificacion.
     *
     * @param id the id of the notificacionDTO to save.
     * @param notificacionDTO the notificacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificacionDTO,
     * or with status {@code 400 (Bad Request)} if the notificacionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/notificacions/{id}")
    public ResponseEntity<NotificacionDTO> updateNotificacion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NotificacionDTO notificacionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Notificacion : {}, {}", id, notificacionDTO);
        if (notificacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NotificacionDTO result = notificacionService.save(notificacionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificacionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /notificacions/:id} : Partial updates given fields of an existing notificacion, field will ignore if it is null
     *
     * @param id the id of the notificacionDTO to save.
     * @param notificacionDTO the notificacionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificacionDTO,
     * or with status {@code 400 (Bad Request)} if the notificacionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the notificacionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the notificacionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/notificacions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NotificacionDTO> partialUpdateNotificacion(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NotificacionDTO notificacionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Notificacion partially : {}, {}", id, notificacionDTO);
        if (notificacionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificacionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificacionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NotificacionDTO> result = notificacionService.partialUpdate(notificacionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificacionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /notificacions} : get all the notificacions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notificacions in body.
     */
    @GetMapping("/notificacions")
    public List<NotificacionDTO> getAllNotificacions() {
        log.debug("REST request to get all Notificacions");
        return notificacionService.findAll();
    }

    /**
     * {@code GET  /notificacions/:id} : get the "id" notificacion.
     *
     * @param id the id of the notificacionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificacionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/notificacions/{id}")
    public ResponseEntity<NotificacionDTO> getNotificacion(@PathVariable Long id) {
        log.debug("REST request to get Notificacion : {}", id);
        Optional<NotificacionDTO> notificacionDTO = notificacionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notificacionDTO);
    }

    /**
     * {@code DELETE  /notificacions/:id} : delete the "id" notificacion.
     *
     * @param id the id of the notificacionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/notificacions/{id}")
    public ResponseEntity<Void> deleteNotificacion(@PathVariable Long id) {
        log.debug("REST request to delete Notificacion : {}", id);
        notificacionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
