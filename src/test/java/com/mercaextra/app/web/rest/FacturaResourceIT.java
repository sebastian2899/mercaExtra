package com.mercaextra.app.web.rest;

import static com.mercaextra.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mercaextra.app.IntegrationTest;
import com.mercaextra.app.domain.Factura;
import com.mercaextra.app.domain.enumeration.MetodoPago;
import com.mercaextra.app.domain.enumeration.TipoFactura;
import com.mercaextra.app.repository.FacturaRepository;
import com.mercaextra.app.service.dto.FacturaDTO;
import com.mercaextra.app.service.mapper.FacturaMapper;
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
 * Integration tests for the {@link FacturaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FacturaResourceIT {

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_INFO_CIENTE = "AAAAAAAAAA";
    private static final String UPDATED_INFO_CIENTE = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_FACTURA = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_FACTURA = "BBBBBBBBBB";

    private static final TipoFactura DEFAULT_TIPO_FACTURA = TipoFactura.CREDITO;
    private static final TipoFactura UPDATED_TIPO_FACTURA = TipoFactura.DEBITO;

    private static final BigDecimal DEFAULT_VALOR_FACTURA = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_FACTURA = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_PAGADO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_PAGADO = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_DEUDA = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_DEUDA = new BigDecimal(2);

    private static final BigDecimal DEFAULT_ESTADO_FACTURA = new BigDecimal(1);
    private static final BigDecimal UPDATED_ESTADO_FACTURA = new BigDecimal(2);

    private static final MetodoPago DEFAULT_METODO_PAGO = MetodoPago.CONTRA_ENTREGA;
    private static final MetodoPago UPDATED_METODO_PAGO = MetodoPago.TARJETA_CREDITO;

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/facturas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private FacturaMapper facturaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFacturaMockMvc;

    private Factura factura;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Factura createEntity(EntityManager em) {
        Factura factura = new Factura()
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .infoCiente(DEFAULT_INFO_CIENTE)
            .numeroFactura(DEFAULT_NUMERO_FACTURA)
            .tipoFactura(DEFAULT_TIPO_FACTURA)
            .valorFactura(DEFAULT_VALOR_FACTURA)
            .valorPagado(DEFAULT_VALOR_PAGADO)
            .valorDeuda(DEFAULT_VALOR_DEUDA)
            .estadoFactura(DEFAULT_ESTADO_FACTURA)
            .metodoPago(DEFAULT_METODO_PAGO)
            .userName(DEFAULT_USER_NAME);
        return factura;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Factura createUpdatedEntity(EntityManager em) {
        Factura factura = new Factura()
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .infoCiente(UPDATED_INFO_CIENTE)
            .numeroFactura(UPDATED_NUMERO_FACTURA)
            .tipoFactura(UPDATED_TIPO_FACTURA)
            .valorFactura(UPDATED_VALOR_FACTURA)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .valorDeuda(UPDATED_VALOR_DEUDA)
            .estadoFactura(UPDATED_ESTADO_FACTURA)
            .metodoPago(UPDATED_METODO_PAGO)
            .userName(UPDATED_USER_NAME);
        return factura;
    }

    @BeforeEach
    public void initTest() {
        factura = createEntity(em);
    }

    @Test
    @Transactional
    void createFactura() throws Exception {
        int databaseSizeBeforeCreate = facturaRepository.findAll().size();
        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);
        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturaDTO)))
            .andExpect(status().isCreated());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeCreate + 1);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testFactura.getInfoCiente()).isEqualTo(DEFAULT_INFO_CIENTE);
        assertThat(testFactura.getNumeroFactura()).isEqualTo(DEFAULT_NUMERO_FACTURA);
        assertThat(testFactura.getTipoFactura()).isEqualTo(DEFAULT_TIPO_FACTURA);
        assertThat(testFactura.getValorFactura()).isEqualByComparingTo(DEFAULT_VALOR_FACTURA);
        assertThat(testFactura.getValorPagado()).isEqualByComparingTo(DEFAULT_VALOR_PAGADO);
        assertThat(testFactura.getValorDeuda()).isEqualByComparingTo(DEFAULT_VALOR_DEUDA);
        assertThat(testFactura.getEstadoFactura()).isEqualByComparingTo(DEFAULT_ESTADO_FACTURA);
        assertThat(testFactura.getMetodoPago()).isEqualTo(DEFAULT_METODO_PAGO);
        assertThat(testFactura.getUserName()).isEqualTo(DEFAULT_USER_NAME);
    }

    @Test
    @Transactional
    void createFacturaWithExistingId() throws Exception {
        // Create the Factura with an existing ID
        factura.setId(1L);
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        int databaseSizeBeforeCreate = facturaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFacturaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFacturas() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get all the facturaList
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(factura.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION.toString())))
            .andExpect(jsonPath("$.[*].infoCiente").value(hasItem(DEFAULT_INFO_CIENTE)))
            .andExpect(jsonPath("$.[*].numeroFactura").value(hasItem(DEFAULT_NUMERO_FACTURA)))
            .andExpect(jsonPath("$.[*].tipoFactura").value(hasItem(DEFAULT_TIPO_FACTURA.toString())))
            .andExpect(jsonPath("$.[*].valorFactura").value(hasItem(sameNumber(DEFAULT_VALOR_FACTURA))))
            .andExpect(jsonPath("$.[*].valorPagado").value(hasItem(sameNumber(DEFAULT_VALOR_PAGADO))))
            .andExpect(jsonPath("$.[*].valorDeuda").value(hasItem(sameNumber(DEFAULT_VALOR_DEUDA))))
            .andExpect(jsonPath("$.[*].estadoFactura").value(hasItem(sameNumber(DEFAULT_ESTADO_FACTURA))))
            .andExpect(jsonPath("$.[*].metodoPago").value(hasItem(DEFAULT_METODO_PAGO.toString())))
            .andExpect(jsonPath("$.[*].userName").value(hasItem(DEFAULT_USER_NAME)));
    }

    @Test
    @Transactional
    void getFactura() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        // Get the factura
        restFacturaMockMvc
            .perform(get(ENTITY_API_URL_ID, factura.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(factura.getId().intValue()))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION.toString()))
            .andExpect(jsonPath("$.infoCiente").value(DEFAULT_INFO_CIENTE))
            .andExpect(jsonPath("$.numeroFactura").value(DEFAULT_NUMERO_FACTURA))
            .andExpect(jsonPath("$.tipoFactura").value(DEFAULT_TIPO_FACTURA.toString()))
            .andExpect(jsonPath("$.valorFactura").value(sameNumber(DEFAULT_VALOR_FACTURA)))
            .andExpect(jsonPath("$.valorPagado").value(sameNumber(DEFAULT_VALOR_PAGADO)))
            .andExpect(jsonPath("$.valorDeuda").value(sameNumber(DEFAULT_VALOR_DEUDA)))
            .andExpect(jsonPath("$.estadoFactura").value(sameNumber(DEFAULT_ESTADO_FACTURA)))
            .andExpect(jsonPath("$.metodoPago").value(DEFAULT_METODO_PAGO.toString()))
            .andExpect(jsonPath("$.userName").value(DEFAULT_USER_NAME));
    }

    @Test
    @Transactional
    void getNonExistingFactura() throws Exception {
        // Get the factura
        restFacturaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFactura() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();

        // Update the factura
        Factura updatedFactura = facturaRepository.findById(factura.getId()).get();
        // Disconnect from session so that the updates on updatedFactura are not directly saved in db
        em.detach(updatedFactura);
        updatedFactura
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .infoCiente(UPDATED_INFO_CIENTE)
            .numeroFactura(UPDATED_NUMERO_FACTURA)
            .tipoFactura(UPDATED_TIPO_FACTURA)
            .valorFactura(UPDATED_VALOR_FACTURA)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .valorDeuda(UPDATED_VALOR_DEUDA)
            .estadoFactura(UPDATED_ESTADO_FACTURA)
            .metodoPago(UPDATED_METODO_PAGO)
            .userName(UPDATED_USER_NAME);
        FacturaDTO facturaDTO = facturaMapper.toDto(updatedFactura);

        restFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facturaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testFactura.getInfoCiente()).isEqualTo(UPDATED_INFO_CIENTE);
        assertThat(testFactura.getNumeroFactura()).isEqualTo(UPDATED_NUMERO_FACTURA);
        assertThat(testFactura.getTipoFactura()).isEqualTo(UPDATED_TIPO_FACTURA);
        assertThat(testFactura.getValorFactura()).isEqualByComparingTo(UPDATED_VALOR_FACTURA);
        assertThat(testFactura.getValorPagado()).isEqualByComparingTo(UPDATED_VALOR_PAGADO);
        assertThat(testFactura.getValorDeuda()).isEqualByComparingTo(UPDATED_VALOR_DEUDA);
        assertThat(testFactura.getEstadoFactura()).isEqualByComparingTo(UPDATED_ESTADO_FACTURA);
        assertThat(testFactura.getMetodoPago()).isEqualTo(UPDATED_METODO_PAGO);
        assertThat(testFactura.getUserName()).isEqualTo(UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void putNonExistingFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, facturaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(facturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(facturaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFacturaWithPatch() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();

        // Update the factura using partial update
        Factura partialUpdatedFactura = new Factura();
        partialUpdatedFactura.setId(factura.getId());

        partialUpdatedFactura
            .numeroFactura(UPDATED_NUMERO_FACTURA)
            .tipoFactura(UPDATED_TIPO_FACTURA)
            .valorFactura(UPDATED_VALOR_FACTURA)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .metodoPago(UPDATED_METODO_PAGO);

        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFactura))
            )
            .andExpect(status().isOk());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testFactura.getInfoCiente()).isEqualTo(DEFAULT_INFO_CIENTE);
        assertThat(testFactura.getNumeroFactura()).isEqualTo(UPDATED_NUMERO_FACTURA);
        assertThat(testFactura.getTipoFactura()).isEqualTo(UPDATED_TIPO_FACTURA);
        assertThat(testFactura.getValorFactura()).isEqualByComparingTo(UPDATED_VALOR_FACTURA);
        assertThat(testFactura.getValorPagado()).isEqualByComparingTo(UPDATED_VALOR_PAGADO);
        assertThat(testFactura.getValorDeuda()).isEqualByComparingTo(DEFAULT_VALOR_DEUDA);
        assertThat(testFactura.getEstadoFactura()).isEqualByComparingTo(DEFAULT_ESTADO_FACTURA);
        assertThat(testFactura.getMetodoPago()).isEqualTo(UPDATED_METODO_PAGO);
        assertThat(testFactura.getUserName()).isEqualTo(DEFAULT_USER_NAME);
    }

    @Test
    @Transactional
    void fullUpdateFacturaWithPatch() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();

        // Update the factura using partial update
        Factura partialUpdatedFactura = new Factura();
        partialUpdatedFactura.setId(factura.getId());

        partialUpdatedFactura
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .infoCiente(UPDATED_INFO_CIENTE)
            .numeroFactura(UPDATED_NUMERO_FACTURA)
            .tipoFactura(UPDATED_TIPO_FACTURA)
            .valorFactura(UPDATED_VALOR_FACTURA)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .valorDeuda(UPDATED_VALOR_DEUDA)
            .estadoFactura(UPDATED_ESTADO_FACTURA)
            .metodoPago(UPDATED_METODO_PAGO)
            .userName(UPDATED_USER_NAME);

        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFactura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFactura))
            )
            .andExpect(status().isOk());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
        Factura testFactura = facturaList.get(facturaList.size() - 1);
        assertThat(testFactura.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testFactura.getInfoCiente()).isEqualTo(UPDATED_INFO_CIENTE);
        assertThat(testFactura.getNumeroFactura()).isEqualTo(UPDATED_NUMERO_FACTURA);
        assertThat(testFactura.getTipoFactura()).isEqualTo(UPDATED_TIPO_FACTURA);
        assertThat(testFactura.getValorFactura()).isEqualByComparingTo(UPDATED_VALOR_FACTURA);
        assertThat(testFactura.getValorPagado()).isEqualByComparingTo(UPDATED_VALOR_PAGADO);
        assertThat(testFactura.getValorDeuda()).isEqualByComparingTo(UPDATED_VALOR_DEUDA);
        assertThat(testFactura.getEstadoFactura()).isEqualByComparingTo(UPDATED_ESTADO_FACTURA);
        assertThat(testFactura.getMetodoPago()).isEqualTo(UPDATED_METODO_PAGO);
        assertThat(testFactura.getUserName()).isEqualTo(UPDATED_USER_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, facturaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(facturaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFactura() throws Exception {
        int databaseSizeBeforeUpdate = facturaRepository.findAll().size();
        factura.setId(count.incrementAndGet());

        // Create the Factura
        FacturaDTO facturaDTO = facturaMapper.toDto(factura);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFacturaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(facturaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Factura in the database
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFactura() throws Exception {
        // Initialize the database
        facturaRepository.saveAndFlush(factura);

        int databaseSizeBeforeDelete = facturaRepository.findAll().size();

        // Delete the factura
        restFacturaMockMvc
            .perform(delete(ENTITY_API_URL_ID, factura.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Factura> facturaList = facturaRepository.findAll();
        assertThat(facturaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
