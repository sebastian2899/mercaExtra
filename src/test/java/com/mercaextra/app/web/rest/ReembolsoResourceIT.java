package com.mercaextra.app.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mercaextra.app.IntegrationTest;
import com.mercaextra.app.domain.Reembolso;
import com.mercaextra.app.repository.ReembolsoRepository;
import com.mercaextra.app.service.dto.ReembolsoDTO;
import com.mercaextra.app.service.mapper.ReembolsoMapper;
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
 * Integration tests for the {@link ReembolsoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReembolsoResourceIT {

    private static final Long DEFAULT_ID_PEDIDO = 1L;
    private static final Long UPDATED_ID_PEDIDO = 2L;

    private static final Long DEFAULT_ID_DOMICILIARIO = 1L;
    private static final Long UPDATED_ID_DOMICILIARIO = 2L;

    private static final Long DEFAULT_ID_FACTURA = 1L;
    private static final Long UPDATED_ID_FACTURA = 2L;

    private static final String DEFAULT_DESCRIPCION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reembolsos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ReembolsoRepository reembolsoRepository;

    @Autowired
    private ReembolsoMapper reembolsoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReembolsoMockMvc;

    private Reembolso reembolso;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reembolso createEntity(EntityManager em) {
        Reembolso reembolso = new Reembolso()
            .idPedido(DEFAULT_ID_PEDIDO)
            .idDomiciliario(DEFAULT_ID_DOMICILIARIO)
            .idFactura(DEFAULT_ID_FACTURA)
            .descripcion(DEFAULT_DESCRIPCION)
            .estado(DEFAULT_ESTADO);
        return reembolso;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reembolso createUpdatedEntity(EntityManager em) {
        Reembolso reembolso = new Reembolso()
            .idPedido(UPDATED_ID_PEDIDO)
            .idDomiciliario(UPDATED_ID_DOMICILIARIO)
            .idFactura(UPDATED_ID_FACTURA)
            .descripcion(UPDATED_DESCRIPCION)
            .estado(UPDATED_ESTADO);
        return reembolso;
    }

    @BeforeEach
    public void initTest() {
        reembolso = createEntity(em);
    }

    @Test
    @Transactional
    void createReembolso() throws Exception {
        int databaseSizeBeforeCreate = reembolsoRepository.findAll().size();
        // Create the Reembolso
        ReembolsoDTO reembolsoDTO = reembolsoMapper.toDto(reembolso);
        restReembolsoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reembolsoDTO)))
            .andExpect(status().isCreated());

        // Validate the Reembolso in the database
        List<Reembolso> reembolsoList = reembolsoRepository.findAll();
        assertThat(reembolsoList).hasSize(databaseSizeBeforeCreate + 1);
        Reembolso testReembolso = reembolsoList.get(reembolsoList.size() - 1);
        assertThat(testReembolso.getIdPedido()).isEqualTo(DEFAULT_ID_PEDIDO);
        assertThat(testReembolso.getIdDomiciliario()).isEqualTo(DEFAULT_ID_DOMICILIARIO);
        assertThat(testReembolso.getIdFactura()).isEqualTo(DEFAULT_ID_FACTURA);
        assertThat(testReembolso.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testReembolso.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void createReembolsoWithExistingId() throws Exception {
        // Create the Reembolso with an existing ID
        reembolso.setId(1L);
        ReembolsoDTO reembolsoDTO = reembolsoMapper.toDto(reembolso);

        int databaseSizeBeforeCreate = reembolsoRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReembolsoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reembolsoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reembolso in the database
        List<Reembolso> reembolsoList = reembolsoRepository.findAll();
        assertThat(reembolsoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllReembolsos() throws Exception {
        // Initialize the database
        reembolsoRepository.saveAndFlush(reembolso);

        // Get all the reembolsoList
        restReembolsoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reembolso.getId().intValue())))
            .andExpect(jsonPath("$.[*].idPedido").value(hasItem(DEFAULT_ID_PEDIDO.intValue())))
            .andExpect(jsonPath("$.[*].idDomiciliario").value(hasItem(DEFAULT_ID_DOMICILIARIO.intValue())))
            .andExpect(jsonPath("$.[*].idFactura").value(hasItem(DEFAULT_ID_FACTURA.intValue())))
            .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)));
    }

    @Test
    @Transactional
    void getReembolso() throws Exception {
        // Initialize the database
        reembolsoRepository.saveAndFlush(reembolso);

        // Get the reembolso
        restReembolsoMockMvc
            .perform(get(ENTITY_API_URL_ID, reembolso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reembolso.getId().intValue()))
            .andExpect(jsonPath("$.idPedido").value(DEFAULT_ID_PEDIDO.intValue()))
            .andExpect(jsonPath("$.idDomiciliario").value(DEFAULT_ID_DOMICILIARIO.intValue()))
            .andExpect(jsonPath("$.idFactura").value(DEFAULT_ID_FACTURA.intValue()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO));
    }

    @Test
    @Transactional
    void getNonExistingReembolso() throws Exception {
        // Get the reembolso
        restReembolsoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewReembolso() throws Exception {
        // Initialize the database
        reembolsoRepository.saveAndFlush(reembolso);

        int databaseSizeBeforeUpdate = reembolsoRepository.findAll().size();

        // Update the reembolso
        Reembolso updatedReembolso = reembolsoRepository.findById(reembolso.getId()).get();
        // Disconnect from session so that the updates on updatedReembolso are not directly saved in db
        em.detach(updatedReembolso);
        updatedReembolso
            .idPedido(UPDATED_ID_PEDIDO)
            .idDomiciliario(UPDATED_ID_DOMICILIARIO)
            .idFactura(UPDATED_ID_FACTURA)
            .descripcion(UPDATED_DESCRIPCION)
            .estado(UPDATED_ESTADO);
        ReembolsoDTO reembolsoDTO = reembolsoMapper.toDto(updatedReembolso);

        restReembolsoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reembolsoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reembolsoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Reembolso in the database
        List<Reembolso> reembolsoList = reembolsoRepository.findAll();
        assertThat(reembolsoList).hasSize(databaseSizeBeforeUpdate);
        Reembolso testReembolso = reembolsoList.get(reembolsoList.size() - 1);
        assertThat(testReembolso.getIdPedido()).isEqualTo(UPDATED_ID_PEDIDO);
        assertThat(testReembolso.getIdDomiciliario()).isEqualTo(UPDATED_ID_DOMICILIARIO);
        assertThat(testReembolso.getIdFactura()).isEqualTo(UPDATED_ID_FACTURA);
        assertThat(testReembolso.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testReembolso.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingReembolso() throws Exception {
        int databaseSizeBeforeUpdate = reembolsoRepository.findAll().size();
        reembolso.setId(count.incrementAndGet());

        // Create the Reembolso
        ReembolsoDTO reembolsoDTO = reembolsoMapper.toDto(reembolso);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReembolsoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reembolsoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reembolsoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reembolso in the database
        List<Reembolso> reembolsoList = reembolsoRepository.findAll();
        assertThat(reembolsoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReembolso() throws Exception {
        int databaseSizeBeforeUpdate = reembolsoRepository.findAll().size();
        reembolso.setId(count.incrementAndGet());

        // Create the Reembolso
        ReembolsoDTO reembolsoDTO = reembolsoMapper.toDto(reembolso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReembolsoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(reembolsoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reembolso in the database
        List<Reembolso> reembolsoList = reembolsoRepository.findAll();
        assertThat(reembolsoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReembolso() throws Exception {
        int databaseSizeBeforeUpdate = reembolsoRepository.findAll().size();
        reembolso.setId(count.incrementAndGet());

        // Create the Reembolso
        ReembolsoDTO reembolsoDTO = reembolsoMapper.toDto(reembolso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReembolsoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(reembolsoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reembolso in the database
        List<Reembolso> reembolsoList = reembolsoRepository.findAll();
        assertThat(reembolsoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReembolsoWithPatch() throws Exception {
        // Initialize the database
        reembolsoRepository.saveAndFlush(reembolso);

        int databaseSizeBeforeUpdate = reembolsoRepository.findAll().size();

        // Update the reembolso using partial update
        Reembolso partialUpdatedReembolso = new Reembolso();
        partialUpdatedReembolso.setId(reembolso.getId());

        partialUpdatedReembolso.idFactura(UPDATED_ID_FACTURA).estado(UPDATED_ESTADO);

        restReembolsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReembolso.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReembolso))
            )
            .andExpect(status().isOk());

        // Validate the Reembolso in the database
        List<Reembolso> reembolsoList = reembolsoRepository.findAll();
        assertThat(reembolsoList).hasSize(databaseSizeBeforeUpdate);
        Reembolso testReembolso = reembolsoList.get(reembolsoList.size() - 1);
        assertThat(testReembolso.getIdPedido()).isEqualTo(DEFAULT_ID_PEDIDO);
        assertThat(testReembolso.getIdDomiciliario()).isEqualTo(DEFAULT_ID_DOMICILIARIO);
        assertThat(testReembolso.getIdFactura()).isEqualTo(UPDATED_ID_FACTURA);
        assertThat(testReembolso.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testReembolso.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void fullUpdateReembolsoWithPatch() throws Exception {
        // Initialize the database
        reembolsoRepository.saveAndFlush(reembolso);

        int databaseSizeBeforeUpdate = reembolsoRepository.findAll().size();

        // Update the reembolso using partial update
        Reembolso partialUpdatedReembolso = new Reembolso();
        partialUpdatedReembolso.setId(reembolso.getId());

        partialUpdatedReembolso
            .idPedido(UPDATED_ID_PEDIDO)
            .idDomiciliario(UPDATED_ID_DOMICILIARIO)
            .idFactura(UPDATED_ID_FACTURA)
            .descripcion(UPDATED_DESCRIPCION)
            .estado(UPDATED_ESTADO);

        restReembolsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReembolso.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedReembolso))
            )
            .andExpect(status().isOk());

        // Validate the Reembolso in the database
        List<Reembolso> reembolsoList = reembolsoRepository.findAll();
        assertThat(reembolsoList).hasSize(databaseSizeBeforeUpdate);
        Reembolso testReembolso = reembolsoList.get(reembolsoList.size() - 1);
        assertThat(testReembolso.getIdPedido()).isEqualTo(UPDATED_ID_PEDIDO);
        assertThat(testReembolso.getIdDomiciliario()).isEqualTo(UPDATED_ID_DOMICILIARIO);
        assertThat(testReembolso.getIdFactura()).isEqualTo(UPDATED_ID_FACTURA);
        assertThat(testReembolso.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testReembolso.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingReembolso() throws Exception {
        int databaseSizeBeforeUpdate = reembolsoRepository.findAll().size();
        reembolso.setId(count.incrementAndGet());

        // Create the Reembolso
        ReembolsoDTO reembolsoDTO = reembolsoMapper.toDto(reembolso);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReembolsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reembolsoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reembolsoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reembolso in the database
        List<Reembolso> reembolsoList = reembolsoRepository.findAll();
        assertThat(reembolsoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReembolso() throws Exception {
        int databaseSizeBeforeUpdate = reembolsoRepository.findAll().size();
        reembolso.setId(count.incrementAndGet());

        // Create the Reembolso
        ReembolsoDTO reembolsoDTO = reembolsoMapper.toDto(reembolso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReembolsoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(reembolsoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reembolso in the database
        List<Reembolso> reembolsoList = reembolsoRepository.findAll();
        assertThat(reembolsoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReembolso() throws Exception {
        int databaseSizeBeforeUpdate = reembolsoRepository.findAll().size();
        reembolso.setId(count.incrementAndGet());

        // Create the Reembolso
        ReembolsoDTO reembolsoDTO = reembolsoMapper.toDto(reembolso);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReembolsoMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(reembolsoDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reembolso in the database
        List<Reembolso> reembolsoList = reembolsoRepository.findAll();
        assertThat(reembolsoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReembolso() throws Exception {
        // Initialize the database
        reembolsoRepository.saveAndFlush(reembolso);

        int databaseSizeBeforeDelete = reembolsoRepository.findAll().size();

        // Delete the reembolso
        restReembolsoMockMvc
            .perform(delete(ENTITY_API_URL_ID, reembolso.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reembolso> reembolsoList = reembolsoRepository.findAll();
        assertThat(reembolsoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
