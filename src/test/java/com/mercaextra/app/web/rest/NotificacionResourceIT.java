package com.mercaextra.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mercaextra.app.IntegrationTest;
import com.mercaextra.app.domain.Notificacion;
import com.mercaextra.app.repository.NotificacionRepository;
import com.mercaextra.app.service.dto.NotificacionDTO;
import com.mercaextra.app.service.mapper.NotificacionMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link NotificacionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NotificacionResourceIT {

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/notificacions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private NotificacionMapper notificacionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificacionMockMvc;

    private Notificacion notificacion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notificacion createEntity(EntityManager em) {
        Notificacion notificacion = new Notificacion().descripcion(DEFAULT_DESCRIPCION).fecha(DEFAULT_FECHA);
        return notificacion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notificacion createUpdatedEntity(EntityManager em) {
        Notificacion notificacion = new Notificacion().descripcion(UPDATED_DESCRIPCION).fecha(UPDATED_FECHA);
        return notificacion;
    }

    @BeforeEach
    public void initTest() {
        notificacion = createEntity(em);
    }

    @Test
    @Transactional
    void createNotificacion() throws Exception {
        int databaseSizeBeforeCreate = notificacionRepository.findAll().size();
        // Create the Notificacion
        NotificacionDTO notificacionDTO = notificacionMapper.toDto(notificacion);
        restNotificacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificacionDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeCreate + 1);
        Notificacion testNotificacion = notificacionList.get(notificacionList.size() - 1);
        assertThat(testNotificacion.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testNotificacion.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    @Transactional
    void createNotificacionWithExistingId() throws Exception {
        // Create the Notificacion with an existing ID
        notificacion.setId(1L);
        NotificacionDTO notificacionDTO = notificacionMapper.toDto(notificacion);

        int databaseSizeBeforeCreate = notificacionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificacionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllNotificacions() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get all the notificacionList
        restNotificacionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificacion.getId().intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA.toString())));
    }

    @Test
    @Transactional
    void getNotificacion() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        // Get the notificacion
        restNotificacionMockMvc
            .perform(get(ENTITY_API_URL_ID, notificacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notificacion.getId().intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingNotificacion() throws Exception {
        // Get the notificacion
        restNotificacionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNotificacion() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();

        // Update the notificacion
        Notificacion updatedNotificacion = notificacionRepository.findById(notificacion.getId()).get();
        // Disconnect from session so that the updates on updatedNotificacion are not directly saved in db
        em.detach(updatedNotificacion);
        updatedNotificacion.descripcion(UPDATED_DESCRIPCION).fecha(UPDATED_FECHA);
        NotificacionDTO notificacionDTO = notificacionMapper.toDto(updatedNotificacion);

        restNotificacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificacionDTO))
            )
            .andExpect(status().isOk());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
        Notificacion testNotificacion = notificacionList.get(notificacionList.size() - 1);
        assertThat(testNotificacion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testNotificacion.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void putNonExistingNotificacion() throws Exception {
        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();
        notificacion.setId(count.incrementAndGet());

        // Create the Notificacion
        NotificacionDTO notificacionDTO = notificacionMapper.toDto(notificacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificacionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotificacion() throws Exception {
        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();
        notificacion.setId(count.incrementAndGet());

        // Create the Notificacion
        NotificacionDTO notificacionDTO = notificacionMapper.toDto(notificacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificacionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(notificacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotificacion() throws Exception {
        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();
        notificacion.setId(count.incrementAndGet());

        // Create the Notificacion
        NotificacionDTO notificacionDTO = notificacionMapper.toDto(notificacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificacionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(notificacionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNotificacionWithPatch() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();

        // Update the notificacion using partial update
        Notificacion partialUpdatedNotificacion = new Notificacion();
        partialUpdatedNotificacion.setId(notificacion.getId());

        restNotificacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotificacion))
            )
            .andExpect(status().isOk());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
        Notificacion testNotificacion = notificacionList.get(notificacionList.size() - 1);
        assertThat(testNotificacion.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testNotificacion.getFecha()).isEqualTo(DEFAULT_FECHA);
    }

    @Test
    @Transactional
    void fullUpdateNotificacionWithPatch() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();

        // Update the notificacion using partial update
        Notificacion partialUpdatedNotificacion = new Notificacion();
        partialUpdatedNotificacion.setId(notificacion.getId());

        partialUpdatedNotificacion.descripcion(UPDATED_DESCRIPCION).fecha(UPDATED_FECHA);

        restNotificacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificacion.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNotificacion))
            )
            .andExpect(status().isOk());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
        Notificacion testNotificacion = notificacionList.get(notificacionList.size() - 1);
        assertThat(testNotificacion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testNotificacion.getFecha()).isEqualTo(UPDATED_FECHA);
    }

    @Test
    @Transactional
    void patchNonExistingNotificacion() throws Exception {
        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();
        notificacion.setId(count.incrementAndGet());

        // Create the Notificacion
        NotificacionDTO notificacionDTO = notificacionMapper.toDto(notificacion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notificacionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotificacion() throws Exception {
        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();
        notificacion.setId(count.incrementAndGet());

        // Create the Notificacion
        NotificacionDTO notificacionDTO = notificacionMapper.toDto(notificacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificacionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificacionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotificacion() throws Exception {
        int databaseSizeBeforeUpdate = notificacionRepository.findAll().size();
        notificacion.setId(count.incrementAndGet());

        // Create the Notificacion
        NotificacionDTO notificacionDTO = notificacionMapper.toDto(notificacion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificacionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(notificacionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notificacion in the database
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNotificacion() throws Exception {
        // Initialize the database
        notificacionRepository.saveAndFlush(notificacion);

        int databaseSizeBeforeDelete = notificacionRepository.findAll().size();

        // Delete the notificacion
        restNotificacionMockMvc
            .perform(delete(ENTITY_API_URL_ID, notificacion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Notificacion> notificacionList = notificacionRepository.findAll();
        assertThat(notificacionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
