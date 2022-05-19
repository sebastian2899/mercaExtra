package com.mercaextra.app.web.rest;

import static com.mercaextra.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mercaextra.app.IntegrationTest;
import com.mercaextra.app.domain.Compra;
import com.mercaextra.app.domain.enumeration.TipoFactura;
import com.mercaextra.app.repository.CompraRepository;
import com.mercaextra.app.service.dto.CompraDTO;
import com.mercaextra.app.service.mapper.CompraMapper;
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
 * Integration tests for the {@link CompraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompraResourceIT {

    private static final Instant DEFAULT_FECHA_CREACION = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_CREACION = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NUMERO_FACTURA = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_FACTURA = "BBBBBBBBBB";

    private static final TipoFactura DEFAULT_TIPO_FACTURA = TipoFactura.CREDITO;
    private static final TipoFactura UPDATED_TIPO_FACTURA = TipoFactura.DEBITO;

    private static final String DEFAULT_INFORMACION_PROOVEDOR = "AAAAAAAAAA";
    private static final String UPDATED_INFORMACION_PROOVEDOR = "BBBBBBBBBB";

    private static final Long DEFAULT_ID_PROVEEDOR = 1L;
    private static final Long UPDATED_ID_PROVEEDOR = 2L;

    private static final BigDecimal DEFAULT_VALOR_FACTURA = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_FACTURA = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_PAGADO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_PAGADO = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_DEUDA = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_DEUDA = new BigDecimal(2);

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/compras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompraRepository compraRepository;

    @Autowired
    private CompraMapper compraMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompraMockMvc;

    private Compra compra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compra createEntity(EntityManager em) {
        Compra compra = new Compra()
            .fechaCreacion(DEFAULT_FECHA_CREACION)
            .numeroFactura(DEFAULT_NUMERO_FACTURA)
            .tipoFactura(DEFAULT_TIPO_FACTURA)
            .informacionProovedor(DEFAULT_INFORMACION_PROOVEDOR)
            .idProveedor(DEFAULT_ID_PROVEEDOR)
            .valorFactura(DEFAULT_VALOR_FACTURA)
            .valorPagado(DEFAULT_VALOR_PAGADO)
            .valorDeuda(DEFAULT_VALOR_DEUDA)
            .estado(DEFAULT_ESTADO);
        return compra;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Compra createUpdatedEntity(EntityManager em) {
        Compra compra = new Compra()
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .numeroFactura(UPDATED_NUMERO_FACTURA)
            .tipoFactura(UPDATED_TIPO_FACTURA)
            .informacionProovedor(UPDATED_INFORMACION_PROOVEDOR)
            .idProveedor(UPDATED_ID_PROVEEDOR)
            .valorFactura(UPDATED_VALOR_FACTURA)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .valorDeuda(UPDATED_VALOR_DEUDA)
            .estado(UPDATED_ESTADO);
        return compra;
    }

    @BeforeEach
    public void initTest() {
        compra = createEntity(em);
    }

    @Test
    @Transactional
    void createCompra() throws Exception {
        int databaseSizeBeforeCreate = compraRepository.findAll().size();
        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);
        restCompraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compraDTO)))
            .andExpect(status().isCreated());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeCreate + 1);
        Compra testCompra = compraList.get(compraList.size() - 1);
        assertThat(testCompra.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testCompra.getNumeroFactura()).isEqualTo(DEFAULT_NUMERO_FACTURA);
        assertThat(testCompra.getTipoFactura()).isEqualTo(DEFAULT_TIPO_FACTURA);
        assertThat(testCompra.getInformacionProovedor()).isEqualTo(DEFAULT_INFORMACION_PROOVEDOR);
        assertThat(testCompra.getIdProveedor()).isEqualTo(DEFAULT_ID_PROVEEDOR);
        assertThat(testCompra.getValorFactura()).isEqualByComparingTo(DEFAULT_VALOR_FACTURA);
        assertThat(testCompra.getValorPagado()).isEqualByComparingTo(DEFAULT_VALOR_PAGADO);
        assertThat(testCompra.getValorDeuda()).isEqualByComparingTo(DEFAULT_VALOR_DEUDA);
        assertThat(testCompra.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void createCompraWithExistingId() throws Exception {
        // Create the Compra with an existing ID
        compra.setId(1L);
        CompraDTO compraDTO = compraMapper.toDto(compra);

        int databaseSizeBeforeCreate = compraRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compraDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompras() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get all the compraList
        restCompraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compra.getId().intValue())))
            .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION)))
            .andExpect(jsonPath("$.[*].numeroFactura").value(hasItem(DEFAULT_NUMERO_FACTURA)))
            .andExpect(jsonPath("$.[*].tipoFactura").value(hasItem(DEFAULT_TIPO_FACTURA.toString())))
            .andExpect(jsonPath("$.[*].informacionProovedor").value(hasItem(DEFAULT_INFORMACION_PROOVEDOR)))
            .andExpect(jsonPath("$.[*].idProveedor").value(hasItem(DEFAULT_ID_PROVEEDOR.intValue())))
            .andExpect(jsonPath("$.[*].valorFactura").value(hasItem(sameNumber(DEFAULT_VALOR_FACTURA))))
            .andExpect(jsonPath("$.[*].valorPagado").value(hasItem(sameNumber(DEFAULT_VALOR_PAGADO))))
            .andExpect(jsonPath("$.[*].valorDeuda").value(hasItem(sameNumber(DEFAULT_VALOR_DEUDA))))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)));
    }

    @Test
    @Transactional
    void getCompra() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        // Get the compra
        restCompraMockMvc
            .perform(get(ENTITY_API_URL_ID, compra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compra.getId().intValue()))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION))
            .andExpect(jsonPath("$.numeroFactura").value(DEFAULT_NUMERO_FACTURA))
            .andExpect(jsonPath("$.tipoFactura").value(DEFAULT_TIPO_FACTURA.toString()))
            .andExpect(jsonPath("$.informacionProovedor").value(DEFAULT_INFORMACION_PROOVEDOR))
            .andExpect(jsonPath("$.idProveedor").value(DEFAULT_ID_PROVEEDOR.intValue()))
            .andExpect(jsonPath("$.valorFactura").value(sameNumber(DEFAULT_VALOR_FACTURA)))
            .andExpect(jsonPath("$.valorPagado").value(sameNumber(DEFAULT_VALOR_PAGADO)))
            .andExpect(jsonPath("$.valorDeuda").value(sameNumber(DEFAULT_VALOR_DEUDA)))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO));
    }

    @Test
    @Transactional
    void getNonExistingCompra() throws Exception {
        // Get the compra
        restCompraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCompra() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        int databaseSizeBeforeUpdate = compraRepository.findAll().size();

        // Update the compra
        Compra updatedCompra = compraRepository.findById(compra.getId()).get();
        // Disconnect from session so that the updates on updatedCompra are not directly saved in db
        em.detach(updatedCompra);
        updatedCompra
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .numeroFactura(UPDATED_NUMERO_FACTURA)
            .tipoFactura(UPDATED_TIPO_FACTURA)
            .informacionProovedor(UPDATED_INFORMACION_PROOVEDOR)
            .idProveedor(UPDATED_ID_PROVEEDOR)
            .valorFactura(UPDATED_VALOR_FACTURA)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .valorDeuda(UPDATED_VALOR_DEUDA)
            .estado(UPDATED_ESTADO);
        CompraDTO compraDTO = compraMapper.toDto(updatedCompra);

        restCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compraDTO))
            )
            .andExpect(status().isOk());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
        Compra testCompra = compraList.get(compraList.size() - 1);
        assertThat(testCompra.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testCompra.getNumeroFactura()).isEqualTo(UPDATED_NUMERO_FACTURA);
        assertThat(testCompra.getTipoFactura()).isEqualTo(UPDATED_TIPO_FACTURA);
        assertThat(testCompra.getInformacionProovedor()).isEqualTo(UPDATED_INFORMACION_PROOVEDOR);
        assertThat(testCompra.getIdProveedor()).isEqualTo(UPDATED_ID_PROVEEDOR);
        assertThat(testCompra.getValorFactura()).isEqualByComparingTo(UPDATED_VALOR_FACTURA);
        assertThat(testCompra.getValorPagado()).isEqualByComparingTo(UPDATED_VALOR_PAGADO);
        assertThat(testCompra.getValorDeuda()).isEqualByComparingTo(UPDATED_VALOR_DEUDA);
        assertThat(testCompra.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void putNonExistingCompra() throws Exception {
        int databaseSizeBeforeUpdate = compraRepository.findAll().size();
        compra.setId(count.incrementAndGet());

        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompra() throws Exception {
        int databaseSizeBeforeUpdate = compraRepository.findAll().size();
        compra.setId(count.incrementAndGet());

        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(compraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompra() throws Exception {
        int databaseSizeBeforeUpdate = compraRepository.findAll().size();
        compra.setId(count.incrementAndGet());

        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(compraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompraWithPatch() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        int databaseSizeBeforeUpdate = compraRepository.findAll().size();

        // Update the compra using partial update
        Compra partialUpdatedCompra = new Compra();
        partialUpdatedCompra.setId(compra.getId());

        partialUpdatedCompra
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .numeroFactura(UPDATED_NUMERO_FACTURA)
            .informacionProovedor(UPDATED_INFORMACION_PROOVEDOR)
            .idProveedor(UPDATED_ID_PROVEEDOR)
            .valorFactura(UPDATED_VALOR_FACTURA)
            .valorDeuda(UPDATED_VALOR_DEUDA);

        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompra))
            )
            .andExpect(status().isOk());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
        Compra testCompra = compraList.get(compraList.size() - 1);
        assertThat(testCompra.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testCompra.getNumeroFactura()).isEqualTo(UPDATED_NUMERO_FACTURA);
        assertThat(testCompra.getTipoFactura()).isEqualTo(DEFAULT_TIPO_FACTURA);
        assertThat(testCompra.getInformacionProovedor()).isEqualTo(UPDATED_INFORMACION_PROOVEDOR);
        assertThat(testCompra.getIdProveedor()).isEqualTo(UPDATED_ID_PROVEEDOR);
        assertThat(testCompra.getValorFactura()).isEqualByComparingTo(UPDATED_VALOR_FACTURA);
        assertThat(testCompra.getValorPagado()).isEqualByComparingTo(DEFAULT_VALOR_PAGADO);
        assertThat(testCompra.getValorDeuda()).isEqualByComparingTo(UPDATED_VALOR_DEUDA);
        assertThat(testCompra.getEstado()).isEqualTo(DEFAULT_ESTADO);
    }

    @Test
    @Transactional
    void fullUpdateCompraWithPatch() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        int databaseSizeBeforeUpdate = compraRepository.findAll().size();

        // Update the compra using partial update
        Compra partialUpdatedCompra = new Compra();
        partialUpdatedCompra.setId(compra.getId());

        partialUpdatedCompra
            .fechaCreacion(UPDATED_FECHA_CREACION)
            .numeroFactura(UPDATED_NUMERO_FACTURA)
            .tipoFactura(UPDATED_TIPO_FACTURA)
            .informacionProovedor(UPDATED_INFORMACION_PROOVEDOR)
            .idProveedor(UPDATED_ID_PROVEEDOR)
            .valorFactura(UPDATED_VALOR_FACTURA)
            .valorPagado(UPDATED_VALOR_PAGADO)
            .valorDeuda(UPDATED_VALOR_DEUDA)
            .estado(UPDATED_ESTADO);

        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompra.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompra))
            )
            .andExpect(status().isOk());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
        Compra testCompra = compraList.get(compraList.size() - 1);
        assertThat(testCompra.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testCompra.getNumeroFactura()).isEqualTo(UPDATED_NUMERO_FACTURA);
        assertThat(testCompra.getTipoFactura()).isEqualTo(UPDATED_TIPO_FACTURA);
        assertThat(testCompra.getInformacionProovedor()).isEqualTo(UPDATED_INFORMACION_PROOVEDOR);
        assertThat(testCompra.getIdProveedor()).isEqualTo(UPDATED_ID_PROVEEDOR);
        assertThat(testCompra.getValorFactura()).isEqualByComparingTo(UPDATED_VALOR_FACTURA);
        assertThat(testCompra.getValorPagado()).isEqualByComparingTo(UPDATED_VALOR_PAGADO);
        assertThat(testCompra.getValorDeuda()).isEqualByComparingTo(UPDATED_VALOR_DEUDA);
        assertThat(testCompra.getEstado()).isEqualTo(UPDATED_ESTADO);
    }

    @Test
    @Transactional
    void patchNonExistingCompra() throws Exception {
        int databaseSizeBeforeUpdate = compraRepository.findAll().size();
        compra.setId(count.incrementAndGet());

        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, compraDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompra() throws Exception {
        int databaseSizeBeforeUpdate = compraRepository.findAll().size();
        compra.setId(count.incrementAndGet());

        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(compraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompra() throws Exception {
        int databaseSizeBeforeUpdate = compraRepository.findAll().size();
        compra.setId(count.incrementAndGet());

        // Create the Compra
        CompraDTO compraDTO = compraMapper.toDto(compra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompraMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(compraDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Compra in the database
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompra() throws Exception {
        // Initialize the database
        compraRepository.saveAndFlush(compra);

        int databaseSizeBeforeDelete = compraRepository.findAll().size();

        // Delete the compra
        restCompraMockMvc
            .perform(delete(ENTITY_API_URL_ID, compra.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Compra> compraList = compraRepository.findAll();
        assertThat(compraList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
