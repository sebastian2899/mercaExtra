package com.mercaextra.app.web.rest;

import static com.mercaextra.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mercaextra.app.IntegrationTest;
import com.mercaextra.app.domain.Egreso;
import com.mercaextra.app.repository.EgresoRepository;
import com.mercaextra.app.service.dto.EgresoDTO;
import com.mercaextra.app.service.mapper.EgresoMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link EgresoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EgresoResourceIT {

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALOR_EGRESO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_EGRESO = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/egresos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EgresoRepository egresoRepository;

    @Autowired
    private EgresoMapper egresoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEgresoMockMvc;

    private Egreso egreso;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Egreso createEntity(EntityManager em) {
        Egreso egreso = new Egreso()
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .descripcion(DEFAULT_DESCRIPCION)
            .valorEgreso(DEFAULT_VALOR_EGRESO);
        return egreso;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Egreso createUpdatedEntity(EntityManager em) {
        Egreso egreso = new Egreso()
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .descripcion(UPDATED_DESCRIPCION)
            .valorEgreso(UPDATED_VALOR_EGRESO);
        return egreso;
    }

    @BeforeEach
    public void initTest() {
        egreso = createEntity(em);
    }

    @Test
    @Transactional
    void createEgreso() throws Exception {
        int databaseSizeBeforeCreate = egresoRepository.findAll().size();
        // Create the Egreso
        EgresoDTO egresoDTO = egresoMapper.toDto(egreso);
        restEgresoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(egresoDTO)))
            .andExpect(status().isCreated());

        // Validate the Egreso in the database
        List<Egreso> egresoList = egresoRepository.findAll();
        assertThat(egresoList).hasSize(databaseSizeBeforeCreate + 1);
        Egreso testEgreso = egresoList.get(egresoList.size() - 1);
        assertThat(testEgreso.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testEgreso.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testEgreso.getValorEgreso()).isEqualByComparingTo(DEFAULT_VALOR_EGRESO);
    }

    @Test
    @Transactional
    void createEgresoWithExistingId() throws Exception {
        // Create the Egreso with an existing ID
        egreso.setId(1L);
        EgresoDTO egresoDTO = egresoMapper.toDto(egreso);

        int databaseSizeBeforeCreate = egresoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEgresoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(egresoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Egreso in the database
        List<Egreso> egresoList = egresoRepository.findAll();
        assertThat(egresoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEgresos() throws Exception {
        // Initialize the database
        egresoRepository.saveAndFlush(egreso);

        // Get all the egresoList
        restEgresoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(egreso.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].valorEgreso").value(hasItem(sameNumber(DEFAULT_VALOR_EGRESO))));
    }

    @Test
    @Transactional
    void getEgreso() throws Exception {
        // Initialize the database
        egresoRepository.saveAndFlush(egreso);

        // Get the egreso
        restEgresoMockMvc
            .perform(get(ENTITY_API_URL_ID, egreso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(egreso.getId().intValue()))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.valorEgreso").value(sameNumber(DEFAULT_VALOR_EGRESO)));
    }

    @Test
    @Transactional
    void getNonExistingEgreso() throws Exception {
        // Get the egreso
        restEgresoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEgreso() throws Exception {
        // Initialize the database
        egresoRepository.saveAndFlush(egreso);

        int databaseSizeBeforeUpdate = egresoRepository.findAll().size();

        // Update the egreso
        Egreso updatedEgreso = egresoRepository.findById(egreso.getId()).get();
        // Disconnect from session so that the updates on updatedEgreso are not directly saved in db
        em.detach(updatedEgreso);
        updatedEgreso.fechaCreacion(UPDATED_FECHA_CREACION).descripcion(UPDATED_DESCRIPCION).valorEgreso(UPDATED_VALOR_EGRESO);
        EgresoDTO egresoDTO = egresoMapper.toDto(updatedEgreso);

        restEgresoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, egresoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(egresoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Egreso in the database
        List<Egreso> egresoList = egresoRepository.findAll();
        assertThat(egresoList).hasSize(databaseSizeBeforeUpdate);
        Egreso testEgreso = egresoList.get(egresoList.size() - 1);
        assertThat(testEgreso.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testEgreso.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testEgreso.getValorEgreso()).isEqualByComparingTo(UPDATED_VALOR_EGRESO);
    }

    @Test
    @Transactional
    void putNonExistingEgreso() throws Exception {
        int databaseSizeBeforeUpdate = egresoRepository.findAll().size();
        egreso.setId(count.incrementAndGet());

        // Create the Egreso
        EgresoDTO egresoDTO = egresoMapper.toDto(egreso);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEgresoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, egresoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(egresoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Egreso in the database
        List<Egreso> egresoList = egresoRepository.findAll();
        assertThat(egresoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEgreso() throws Exception {
        int databaseSizeBeforeUpdate = egresoRepository.findAll().size();
        egreso.setId(count.incrementAndGet());

        // Create the Egreso
        EgresoDTO egresoDTO = egresoMapper.toDto(egreso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgresoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(egresoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Egreso in the database
        List<Egreso> egresoList = egresoRepository.findAll();
        assertThat(egresoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEgreso() throws Exception {
        int databaseSizeBeforeUpdate = egresoRepository.findAll().size();
        egreso.setId(count.incrementAndGet());

        // Create the Egreso
        EgresoDTO egresoDTO = egresoMapper.toDto(egreso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgresoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(egresoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Egreso in the database
        List<Egreso> egresoList = egresoRepository.findAll();
        assertThat(egresoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEgresoWithPatch() throws Exception {
        // Initialize the database
        egresoRepository.saveAndFlush(egreso);

        int databaseSizeBeforeUpdate = egresoRepository.findAll().size();

        // Update the egreso using partial update
        Egreso partialUpdatedEgreso = new Egreso();
        partialUpdatedEgreso.setId(egreso.getId());

        partialUpdatedEgreso.descripcion(UPDATED_DESCRIPCION).valorEgreso(UPDATED_VALOR_EGRESO);

        restEgresoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEgreso.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEgreso))
            )
            .andExpect(status().isOk());

        // Validate the Egreso in the database
        List<Egreso> egresoList = egresoRepository.findAll();
        assertThat(egresoList).hasSize(databaseSizeBeforeUpdate);
        Egreso testEgreso = egresoList.get(egresoList.size() - 1);
        assertThat(testEgreso.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testEgreso.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testEgreso.getValorEgreso()).isEqualByComparingTo(UPDATED_VALOR_EGRESO);
    }

    @Test
    @Transactional
    void fullUpdateEgresoWithPatch() throws Exception {
        // Initialize the database
        egresoRepository.saveAndFlush(egreso);

        int databaseSizeBeforeUpdate = egresoRepository.findAll().size();

        // Update the egreso using partial update
        Egreso partialUpdatedEgreso = new Egreso();
        partialUpdatedEgreso.setId(egreso.getId());

        partialUpdatedEgreso.fechaCreacion(UPDATED_FECHA_CREACION).descripcion(UPDATED_DESCRIPCION).valorEgreso(UPDATED_VALOR_EGRESO);

        restEgresoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEgreso.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEgreso))
            )
            .andExpect(status().isOk());

        // Validate the Egreso in the database
        List<Egreso> egresoList = egresoRepository.findAll();
        assertThat(egresoList).hasSize(databaseSizeBeforeUpdate);
        Egreso testEgreso = egresoList.get(egresoList.size() - 1);
        assertThat(testEgreso.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testEgreso.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testEgreso.getValorEgreso()).isEqualByComparingTo(UPDATED_VALOR_EGRESO);
    }

    @Test
    @Transactional
    void patchNonExistingEgreso() throws Exception {
        int databaseSizeBeforeUpdate = egresoRepository.findAll().size();
        egreso.setId(count.incrementAndGet());

        // Create the Egreso
        EgresoDTO egresoDTO = egresoMapper.toDto(egreso);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEgresoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, egresoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(egresoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Egreso in the database
        List<Egreso> egresoList = egresoRepository.findAll();
        assertThat(egresoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEgreso() throws Exception {
        int databaseSizeBeforeUpdate = egresoRepository.findAll().size();
        egreso.setId(count.incrementAndGet());

        // Create the Egreso
        EgresoDTO egresoDTO = egresoMapper.toDto(egreso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgresoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(egresoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Egreso in the database
        List<Egreso> egresoList = egresoRepository.findAll();
        assertThat(egresoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEgreso() throws Exception {
        int databaseSizeBeforeUpdate = egresoRepository.findAll().size();
        egreso.setId(count.incrementAndGet());

        // Create the Egreso
        EgresoDTO egresoDTO = egresoMapper.toDto(egreso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEgresoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(egresoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Egreso in the database
        List<Egreso> egresoList = egresoRepository.findAll();
        assertThat(egresoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEgreso() throws Exception {
        // Initialize the database
        egresoRepository.saveAndFlush(egreso);

        int databaseSizeBeforeDelete = egresoRepository.findAll().size();

        // Delete the egreso
        restEgresoMockMvc
            .perform(delete(ENTITY_API_URL_ID, egreso.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Egreso> egresoList = egresoRepository.findAll();
        assertThat(egresoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
