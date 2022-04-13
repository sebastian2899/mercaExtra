package com.mercaextra.app.web.rest;

import static com.mercaextra.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mercaextra.app.IntegrationTest;
import com.mercaextra.app.domain.Domiciliario;
import com.mercaextra.app.domain.enumeration.EstadoDomiciliario;
import com.mercaextra.app.domain.enumeration.TipoSalario;
import com.mercaextra.app.repository.DomiciliarioRepository;
import com.mercaextra.app.service.dto.DomiciliarioDTO;
import com.mercaextra.app.service.mapper.DomiciliarioMapper;
import java.math.BigDecimal;
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
 * Integration tests for the {@link DomiciliarioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DomiciliarioResourceIT {

    private static final String DEFAULT_NOMBRE = "AAAAAAAAAA";
    private static final String UPDATED_NOMBRE = "BBBBBBBBBB";

    private static final String DEFAULT_APELLIDO = "AAAAAAAAAA";
    private static final String UPDATED_APELLIDO = "BBBBBBBBBB";

    private static final TipoSalario DEFAULT_SALARIO = TipoSalario.SALARIO_NORMAL;
    private static final TipoSalario UPDATED_SALARIO = TipoSalario.SALARIO_POR_DOMICILIO;

    private static final String DEFAULT_TELEFONO = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONO = "BBBBBBBBBB";

    private static final String DEFAULT_HORARIO = "AAAAAAAAAA";
    private static final String UPDATED_HORARIO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_SUELDO = new BigDecimal(1);
    private static final BigDecimal UPDATED_SUELDO = new BigDecimal(2);

    private static final EstadoDomiciliario DEFAULT_ESTADO = EstadoDomiciliario.EN_ENTREGA;
    private static final EstadoDomiciliario UPDATED_ESTADO = EstadoDomiciliario.DISPONIBLE;

    private static final String ENTITY_API_URL = "/api/domiciliarios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private DomiciliarioRepository domiciliarioRepository;

    @Autowired
    private DomiciliarioMapper domiciliarioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDomiciliarioMockMvc;

    private Domiciliario domiciliario;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Domiciliario createEntity(EntityManager em) {
        Domiciliario domiciliario = new Domiciliario()
            .nombre(DEFAULT_NOMBRE)
            .apellido(DEFAULT_APELLIDO)
            .salario(DEFAULT_SALARIO)
            .telefono(DEFAULT_TELEFONO)
            .horario(DEFAULT_HORARIO)
            .sueldo(DEFAULT_SUELDO)
            .estado(DEFAULT_ESTADO);
        return domiciliario;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Domiciliario createUpdatedEntity(EntityManager em) {
        Domiciliario domiciliario = new Domiciliario()
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .salario(UPDATED_SALARIO)
            .telefono(UPDATED_TELEFONO)
            .horario(UPDATED_HORARIO)
            .sueldo(UPDATED_SUELDO)
            .estado(UPDATED_ESTADO);
        return domiciliario;
    }

    @BeforeEach
    public void initTest() {
        domiciliario = createEntity(em);
    }

    @Test
    @Transactional
    void createDomiciliario() throws Exception {
        int databaseSizeBeforeCreate = domiciliarioRepository.findAll().size();
        // Create the Domiciliario
        DomiciliarioDTO domiciliarioDTO = domiciliarioMapper.toDto(domiciliario);
        restDomiciliarioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(domiciliarioDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Domiciliario in the database
        List<Domiciliario> domiciliarioList = domiciliarioRepository.findAll();
        assertThat(domiciliarioList).hasSize(databaseSizeBeforeCreate + 1);
        Domiciliario testDomiciliario = domiciliarioList.get(domiciliarioList.size() - 1);
        assertThat(testDomiciliario.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testDomiciliario.getApellido()).isEqualTo(DEFAULT_APELLIDO);
        assertThat(testDomiciliario.getSalario()).isEqualTo(DEFAULT_SALARIO);
        assertThat(testDomiciliario.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testDomiciliario.getHorario()).isEqualTo(DEFAULT_HORARIO);
        assertThat(testDomiciliario.getSueldo()).isEqualByComparingTo(DEFAULT_SUELDO);
        assertThat(testDomiciliario.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void createDomiciliarioWithExistingId() throws Exception {
        // Create the Domiciliario with an existing ID
        domiciliario.setId(1L);
        DomiciliarioDTO domiciliarioDTO = domiciliarioMapper.toDto(domiciliario);

        int databaseSizeBeforeCreate = domiciliarioRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDomiciliarioMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(domiciliarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domiciliario in the database
        List<Domiciliario> domiciliarioList = domiciliarioRepository.findAll();
        assertThat(domiciliarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllDomiciliarios() throws Exception {
        // Initialize the database
        domiciliarioRepository.saveAndFlush(domiciliario);

        // Get all the domiciliarioList
        restDomiciliarioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(domiciliario.getId().intValue())))
            .andExpect(jsonPath("$.[*].nombre").value(hasItem(DEFAULT_NOMBRE)))
            .andExpect(jsonPath("$.[*].apellido").value(hasItem(DEFAULT_APELLIDO)))
            .andExpect(jsonPath("$.[*].salario").value(hasItem(DEFAULT_SALARIO.toString())))
            .andExpect(jsonPath("$.[*].telefono").value(hasItem(DEFAULT_TELEFONO)))
            .andExpect(jsonPath("$.[*].horario").value(hasItem(DEFAULT_HORARIO)))
            .andExpect(jsonPath("$.[*].sueldo").value(hasItem(sameNumber(DEFAULT_SUELDO))))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())));
    }

    @Test
    @Transactional
    void getDomiciliario() throws Exception {
        // Initialize the database
        domiciliarioRepository.saveAndFlush(domiciliario);

        // Get the domiciliario
        restDomiciliarioMockMvc
            .perform(get(ENTITY_API_URL_ID, domiciliario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(domiciliario.getId().intValue()))
            .andExpect(jsonPath("$.nombre").value(DEFAULT_NOMBRE))
            .andExpect(jsonPath("$.apellido").value(DEFAULT_APELLIDO))
            .andExpect(jsonPath("$.salario").value(DEFAULT_SALARIO.toString()))
            .andExpect(jsonPath("$.telefono").value(DEFAULT_TELEFONO))
            .andExpect(jsonPath("$.horario").value(DEFAULT_HORARIO))
            .andExpect(jsonPath("$.sueldo").value(sameNumber(DEFAULT_SUELDO)))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDomiciliario() throws Exception {
        // Get the domiciliario
        restDomiciliarioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewDomiciliario() throws Exception {
        // Initialize the database
        domiciliarioRepository.saveAndFlush(domiciliario);

        int databaseSizeBeforeUpdate = domiciliarioRepository.findAll().size();

        // Update the domiciliario
        Domiciliario updatedDomiciliario = domiciliarioRepository.findById(domiciliario.getId()).get();
        // Disconnect from session so that the updates on updatedDomiciliario are not directly saved in db
        em.detach(updatedDomiciliario);
        updatedDomiciliario
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .salario(UPDATED_SALARIO)
            .telefono(UPDATED_TELEFONO)
            .horario(UPDATED_HORARIO)
            .sueldo(UPDATED_SUELDO)
            .estado(UPDATED_ESTADO);
        DomiciliarioDTO domiciliarioDTO = domiciliarioMapper.toDto(updatedDomiciliario);

        restDomiciliarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, domiciliarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(domiciliarioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Domiciliario in the database
        List<Domiciliario> domiciliarioList = domiciliarioRepository.findAll();
        assertThat(domiciliarioList).hasSize(databaseSizeBeforeUpdate);
        Domiciliario testDomiciliario = domiciliarioList.get(domiciliarioList.size() - 1);
        assertThat(testDomiciliario.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testDomiciliario.getApellido()).isEqualTo(UPDATED_APELLIDO);
        assertThat(testDomiciliario.getSalario()).isEqualTo(UPDATED_SALARIO);
        assertThat(testDomiciliario.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testDomiciliario.getHorario()).isEqualTo(UPDATED_HORARIO);
        assertThat(testDomiciliario.getSueldo()).isEqualByComparingTo(UPDATED_SUELDO);
        assertThat(testDomiciliario.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingDomiciliario() throws Exception {
        int databaseSizeBeforeUpdate = domiciliarioRepository.findAll().size();
        domiciliario.setId(count.incrementAndGet());

        // Create the Domiciliario
        DomiciliarioDTO domiciliarioDTO = domiciliarioMapper.toDto(domiciliario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDomiciliarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, domiciliarioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(domiciliarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domiciliario in the database
        List<Domiciliario> domiciliarioList = domiciliarioRepository.findAll();
        assertThat(domiciliarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDomiciliario() throws Exception {
        int databaseSizeBeforeUpdate = domiciliarioRepository.findAll().size();
        domiciliario.setId(count.incrementAndGet());

        // Create the Domiciliario
        DomiciliarioDTO domiciliarioDTO = domiciliarioMapper.toDto(domiciliario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomiciliarioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(domiciliarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domiciliario in the database
        List<Domiciliario> domiciliarioList = domiciliarioRepository.findAll();
        assertThat(domiciliarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDomiciliario() throws Exception {
        int databaseSizeBeforeUpdate = domiciliarioRepository.findAll().size();
        domiciliario.setId(count.incrementAndGet());

        // Create the Domiciliario
        DomiciliarioDTO domiciliarioDTO = domiciliarioMapper.toDto(domiciliario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomiciliarioMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(domiciliarioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Domiciliario in the database
        List<Domiciliario> domiciliarioList = domiciliarioRepository.findAll();
        assertThat(domiciliarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDomiciliarioWithPatch() throws Exception {
        // Initialize the database
        domiciliarioRepository.saveAndFlush(domiciliario);

        int databaseSizeBeforeUpdate = domiciliarioRepository.findAll().size();

        // Update the domiciliario using partial update
        Domiciliario partialUpdatedDomiciliario = new Domiciliario();
        partialUpdatedDomiciliario.setId(domiciliario.getId());

        partialUpdatedDomiciliario.salario(UPDATED_SALARIO);

        restDomiciliarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDomiciliario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDomiciliario))
            )
            .andExpect(status().isOk());

        // Validate the Domiciliario in the database
        List<Domiciliario> domiciliarioList = domiciliarioRepository.findAll();
        assertThat(domiciliarioList).hasSize(databaseSizeBeforeUpdate);
        Domiciliario testDomiciliario = domiciliarioList.get(domiciliarioList.size() - 1);
        assertThat(testDomiciliario.getNombre()).isEqualTo(DEFAULT_NOMBRE);
        assertThat(testDomiciliario.getApellido()).isEqualTo(DEFAULT_APELLIDO);
        assertThat(testDomiciliario.getSalario()).isEqualTo(UPDATED_SALARIO);
        assertThat(testDomiciliario.getTelefono()).isEqualTo(DEFAULT_TELEFONO);
        assertThat(testDomiciliario.getHorario()).isEqualTo(DEFAULT_HORARIO);
        assertThat(testDomiciliario.getSueldo()).isEqualByComparingTo(DEFAULT_SUELDO);
        assertThat(testDomiciliario.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void fullUpdateDomiciliarioWithPatch() throws Exception {
        // Initialize the database
        domiciliarioRepository.saveAndFlush(domiciliario);

        int databaseSizeBeforeUpdate = domiciliarioRepository.findAll().size();

        // Update the domiciliario using partial update
        Domiciliario partialUpdatedDomiciliario = new Domiciliario();
        partialUpdatedDomiciliario.setId(domiciliario.getId());

        partialUpdatedDomiciliario
            .nombre(UPDATED_NOMBRE)
            .apellido(UPDATED_APELLIDO)
            .salario(UPDATED_SALARIO)
            .telefono(UPDATED_TELEFONO)
            .horario(UPDATED_HORARIO)
            .sueldo(UPDATED_SUELDO)
            .estado(UPDATED_ESTADO);

        restDomiciliarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDomiciliario.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedDomiciliario))
            )
            .andExpect(status().isOk());

        // Validate the Domiciliario in the database
        List<Domiciliario> domiciliarioList = domiciliarioRepository.findAll();
        assertThat(domiciliarioList).hasSize(databaseSizeBeforeUpdate);
        Domiciliario testDomiciliario = domiciliarioList.get(domiciliarioList.size() - 1);
        assertThat(testDomiciliario.getNombre()).isEqualTo(UPDATED_NOMBRE);
        assertThat(testDomiciliario.getApellido()).isEqualTo(UPDATED_APELLIDO);
        assertThat(testDomiciliario.getSalario()).isEqualTo(UPDATED_SALARIO);
        assertThat(testDomiciliario.getTelefono()).isEqualTo(UPDATED_TELEFONO);
        assertThat(testDomiciliario.getHorario()).isEqualTo(UPDATED_HORARIO);
        assertThat(testDomiciliario.getSueldo()).isEqualByComparingTo(UPDATED_SUELDO);
        assertThat(testDomiciliario.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingDomiciliario() throws Exception {
        int databaseSizeBeforeUpdate = domiciliarioRepository.findAll().size();
        domiciliario.setId(count.incrementAndGet());

        // Create the Domiciliario
        DomiciliarioDTO domiciliarioDTO = domiciliarioMapper.toDto(domiciliario);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDomiciliarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, domiciliarioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(domiciliarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domiciliario in the database
        List<Domiciliario> domiciliarioList = domiciliarioRepository.findAll();
        assertThat(domiciliarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDomiciliario() throws Exception {
        int databaseSizeBeforeUpdate = domiciliarioRepository.findAll().size();
        domiciliario.setId(count.incrementAndGet());

        // Create the Domiciliario
        DomiciliarioDTO domiciliarioDTO = domiciliarioMapper.toDto(domiciliario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomiciliarioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(domiciliarioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Domiciliario in the database
        List<Domiciliario> domiciliarioList = domiciliarioRepository.findAll();
        assertThat(domiciliarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDomiciliario() throws Exception {
        int databaseSizeBeforeUpdate = domiciliarioRepository.findAll().size();
        domiciliario.setId(count.incrementAndGet());

        // Create the Domiciliario
        DomiciliarioDTO domiciliarioDTO = domiciliarioMapper.toDto(domiciliario);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDomiciliarioMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(domiciliarioDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Domiciliario in the database
        List<Domiciliario> domiciliarioList = domiciliarioRepository.findAll();
        assertThat(domiciliarioList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDomiciliario() throws Exception {
        // Initialize the database
        domiciliarioRepository.saveAndFlush(domiciliario);

        int databaseSizeBeforeDelete = domiciliarioRepository.findAll().size();

        // Delete the domiciliario
        restDomiciliarioMockMvc
            .perform(delete(ENTITY_API_URL_ID, domiciliario.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Domiciliario> domiciliarioList = domiciliarioRepository.findAll();
        assertThat(domiciliarioList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
