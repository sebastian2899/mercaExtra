package com.mercaextra.app.web.rest;

import static com.mercaextra.app.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mercaextra.app.IntegrationTest;
import com.mercaextra.app.domain.ItemFacturaVenta;
import com.mercaextra.app.repository.ItemFacturaVentaRepository;
import com.mercaextra.app.service.dto.ItemFacturaVentaDTO;
import com.mercaextra.app.service.mapper.ItemFacturaVentaMapper;
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
 * Integration tests for the {@link ItemFacturaVentaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemFacturaVentaResourceIT {

    private static final Long DEFAULT_ID_FACTURA = 1L;
    private static final Long UPDATED_ID_FACTURA = 2L;

    private static final Long DEFAULT_ID_PRODUCTO = 1L;
    private static final Long UPDATED_ID_PRODUCTO = 2L;

    private static final Long DEFAULT_CANTIDAD = 1L;
    private static final Long UPDATED_CANTIDAD = 2L;

    private static final BigDecimal DEFAULT_PRECIO = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRECIO = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/item-factura-ventas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ItemFacturaVentaRepository itemFacturaVentaRepository;

    @Autowired
    private ItemFacturaVentaMapper itemFacturaVentaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemFacturaVentaMockMvc;

    private ItemFacturaVenta itemFacturaVenta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemFacturaVenta createEntity(EntityManager em) {
        ItemFacturaVenta itemFacturaVenta = new ItemFacturaVenta()
            .idFactura(DEFAULT_ID_FACTURA)
            .idProducto(DEFAULT_ID_PRODUCTO)
            .cantidad(DEFAULT_CANTIDAD)
            .precio(DEFAULT_PRECIO);
        return itemFacturaVenta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemFacturaVenta createUpdatedEntity(EntityManager em) {
        ItemFacturaVenta itemFacturaVenta = new ItemFacturaVenta()
            .idFactura(UPDATED_ID_FACTURA)
            .idProducto(UPDATED_ID_PRODUCTO)
            .cantidad(UPDATED_CANTIDAD)
            .precio(UPDATED_PRECIO);
        return itemFacturaVenta;
    }

    @BeforeEach
    public void initTest() {
        itemFacturaVenta = createEntity(em);
    }

    @Test
    @Transactional
    void createItemFacturaVenta() throws Exception {
        int databaseSizeBeforeCreate = itemFacturaVentaRepository.findAll().size();
        // Create the ItemFacturaVenta
        ItemFacturaVentaDTO itemFacturaVentaDTO = itemFacturaVentaMapper.toDto(itemFacturaVenta);
        restItemFacturaVentaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemFacturaVentaDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ItemFacturaVenta in the database
        List<ItemFacturaVenta> itemFacturaVentaList = itemFacturaVentaRepository.findAll();
        assertThat(itemFacturaVentaList).hasSize(databaseSizeBeforeCreate + 1);
        ItemFacturaVenta testItemFacturaVenta = itemFacturaVentaList.get(itemFacturaVentaList.size() - 1);
        assertThat(testItemFacturaVenta.getIdFactura()).isEqualTo(DEFAULT_ID_FACTURA);
        assertThat(testItemFacturaVenta.getIdProducto()).isEqualTo(DEFAULT_ID_PRODUCTO);
        assertThat(testItemFacturaVenta.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testItemFacturaVenta.getPrecio()).isEqualByComparingTo(DEFAULT_PRECIO);
    }

    @Test
    @Transactional
    void createItemFacturaVentaWithExistingId() throws Exception {
        // Create the ItemFacturaVenta with an existing ID
        itemFacturaVenta.setId(1L);
        ItemFacturaVentaDTO itemFacturaVentaDTO = itemFacturaVentaMapper.toDto(itemFacturaVenta);

        int databaseSizeBeforeCreate = itemFacturaVentaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemFacturaVentaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemFacturaVentaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemFacturaVenta in the database
        List<ItemFacturaVenta> itemFacturaVentaList = itemFacturaVentaRepository.findAll();
        assertThat(itemFacturaVentaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllItemFacturaVentas() throws Exception {
        // Initialize the database
        itemFacturaVentaRepository.saveAndFlush(itemFacturaVenta);

        // Get all the itemFacturaVentaList
        restItemFacturaVentaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemFacturaVenta.getId().intValue())))
            .andExpect(jsonPath("$.[*].idFactura").value(hasItem(DEFAULT_ID_FACTURA.intValue())))
            .andExpect(jsonPath("$.[*].idProducto").value(hasItem(DEFAULT_ID_PRODUCTO.intValue())))
            .andExpect(jsonPath("$.[*].cantidad").value(hasItem(DEFAULT_CANTIDAD.intValue())))
            .andExpect(jsonPath("$.[*].precio").value(hasItem(sameNumber(DEFAULT_PRECIO))));
    }

    @Test
    @Transactional
    void getItemFacturaVenta() throws Exception {
        // Initialize the database
        itemFacturaVentaRepository.saveAndFlush(itemFacturaVenta);

        // Get the itemFacturaVenta
        restItemFacturaVentaMockMvc
            .perform(get(ENTITY_API_URL_ID, itemFacturaVenta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemFacturaVenta.getId().intValue()))
            .andExpect(jsonPath("$.idFactura").value(DEFAULT_ID_FACTURA.intValue()))
            .andExpect(jsonPath("$.idProducto").value(DEFAULT_ID_PRODUCTO.intValue()))
            .andExpect(jsonPath("$.cantidad").value(DEFAULT_CANTIDAD.intValue()))
            .andExpect(jsonPath("$.precio").value(sameNumber(DEFAULT_PRECIO)));
    }

    @Test
    @Transactional
    void getNonExistingItemFacturaVenta() throws Exception {
        // Get the itemFacturaVenta
        restItemFacturaVentaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewItemFacturaVenta() throws Exception {
        // Initialize the database
        itemFacturaVentaRepository.saveAndFlush(itemFacturaVenta);

        int databaseSizeBeforeUpdate = itemFacturaVentaRepository.findAll().size();

        // Update the itemFacturaVenta
        ItemFacturaVenta updatedItemFacturaVenta = itemFacturaVentaRepository.findById(itemFacturaVenta.getId()).get();
        // Disconnect from session so that the updates on updatedItemFacturaVenta are not directly saved in db
        em.detach(updatedItemFacturaVenta);
        updatedItemFacturaVenta
            .idFactura(UPDATED_ID_FACTURA)
            .idProducto(UPDATED_ID_PRODUCTO)
            .cantidad(UPDATED_CANTIDAD)
            .precio(UPDATED_PRECIO);
        ItemFacturaVentaDTO itemFacturaVentaDTO = itemFacturaVentaMapper.toDto(updatedItemFacturaVenta);

        restItemFacturaVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemFacturaVentaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemFacturaVentaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ItemFacturaVenta in the database
        List<ItemFacturaVenta> itemFacturaVentaList = itemFacturaVentaRepository.findAll();
        assertThat(itemFacturaVentaList).hasSize(databaseSizeBeforeUpdate);
        ItemFacturaVenta testItemFacturaVenta = itemFacturaVentaList.get(itemFacturaVentaList.size() - 1);
        assertThat(testItemFacturaVenta.getIdFactura()).isEqualTo(UPDATED_ID_FACTURA);
        assertThat(testItemFacturaVenta.getIdProducto()).isEqualTo(UPDATED_ID_PRODUCTO);
        assertThat(testItemFacturaVenta.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testItemFacturaVenta.getPrecio()).isEqualByComparingTo(UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void putNonExistingItemFacturaVenta() throws Exception {
        int databaseSizeBeforeUpdate = itemFacturaVentaRepository.findAll().size();
        itemFacturaVenta.setId(count.incrementAndGet());

        // Create the ItemFacturaVenta
        ItemFacturaVentaDTO itemFacturaVentaDTO = itemFacturaVentaMapper.toDto(itemFacturaVenta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemFacturaVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemFacturaVentaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemFacturaVentaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemFacturaVenta in the database
        List<ItemFacturaVenta> itemFacturaVentaList = itemFacturaVentaRepository.findAll();
        assertThat(itemFacturaVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItemFacturaVenta() throws Exception {
        int databaseSizeBeforeUpdate = itemFacturaVentaRepository.findAll().size();
        itemFacturaVenta.setId(count.incrementAndGet());

        // Create the ItemFacturaVenta
        ItemFacturaVentaDTO itemFacturaVentaDTO = itemFacturaVentaMapper.toDto(itemFacturaVenta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemFacturaVentaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemFacturaVentaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemFacturaVenta in the database
        List<ItemFacturaVenta> itemFacturaVentaList = itemFacturaVentaRepository.findAll();
        assertThat(itemFacturaVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItemFacturaVenta() throws Exception {
        int databaseSizeBeforeUpdate = itemFacturaVentaRepository.findAll().size();
        itemFacturaVenta.setId(count.incrementAndGet());

        // Create the ItemFacturaVenta
        ItemFacturaVentaDTO itemFacturaVentaDTO = itemFacturaVentaMapper.toDto(itemFacturaVenta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemFacturaVentaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemFacturaVentaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemFacturaVenta in the database
        List<ItemFacturaVenta> itemFacturaVentaList = itemFacturaVentaRepository.findAll();
        assertThat(itemFacturaVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemFacturaVentaWithPatch() throws Exception {
        // Initialize the database
        itemFacturaVentaRepository.saveAndFlush(itemFacturaVenta);

        int databaseSizeBeforeUpdate = itemFacturaVentaRepository.findAll().size();

        // Update the itemFacturaVenta using partial update
        ItemFacturaVenta partialUpdatedItemFacturaVenta = new ItemFacturaVenta();
        partialUpdatedItemFacturaVenta.setId(itemFacturaVenta.getId());

        partialUpdatedItemFacturaVenta.idFactura(UPDATED_ID_FACTURA);

        restItemFacturaVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemFacturaVenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemFacturaVenta))
            )
            .andExpect(status().isOk());

        // Validate the ItemFacturaVenta in the database
        List<ItemFacturaVenta> itemFacturaVentaList = itemFacturaVentaRepository.findAll();
        assertThat(itemFacturaVentaList).hasSize(databaseSizeBeforeUpdate);
        ItemFacturaVenta testItemFacturaVenta = itemFacturaVentaList.get(itemFacturaVentaList.size() - 1);
        assertThat(testItemFacturaVenta.getIdFactura()).isEqualTo(UPDATED_ID_FACTURA);
        assertThat(testItemFacturaVenta.getIdProducto()).isEqualTo(DEFAULT_ID_PRODUCTO);
        assertThat(testItemFacturaVenta.getCantidad()).isEqualTo(DEFAULT_CANTIDAD);
        assertThat(testItemFacturaVenta.getPrecio()).isEqualByComparingTo(DEFAULT_PRECIO);
    }

    @Test
    @Transactional
    void fullUpdateItemFacturaVentaWithPatch() throws Exception {
        // Initialize the database
        itemFacturaVentaRepository.saveAndFlush(itemFacturaVenta);

        int databaseSizeBeforeUpdate = itemFacturaVentaRepository.findAll().size();

        // Update the itemFacturaVenta using partial update
        ItemFacturaVenta partialUpdatedItemFacturaVenta = new ItemFacturaVenta();
        partialUpdatedItemFacturaVenta.setId(itemFacturaVenta.getId());

        partialUpdatedItemFacturaVenta
            .idFactura(UPDATED_ID_FACTURA)
            .idProducto(UPDATED_ID_PRODUCTO)
            .cantidad(UPDATED_CANTIDAD)
            .precio(UPDATED_PRECIO);

        restItemFacturaVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemFacturaVenta.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemFacturaVenta))
            )
            .andExpect(status().isOk());

        // Validate the ItemFacturaVenta in the database
        List<ItemFacturaVenta> itemFacturaVentaList = itemFacturaVentaRepository.findAll();
        assertThat(itemFacturaVentaList).hasSize(databaseSizeBeforeUpdate);
        ItemFacturaVenta testItemFacturaVenta = itemFacturaVentaList.get(itemFacturaVentaList.size() - 1);
        assertThat(testItemFacturaVenta.getIdFactura()).isEqualTo(UPDATED_ID_FACTURA);
        assertThat(testItemFacturaVenta.getIdProducto()).isEqualTo(UPDATED_ID_PRODUCTO);
        assertThat(testItemFacturaVenta.getCantidad()).isEqualTo(UPDATED_CANTIDAD);
        assertThat(testItemFacturaVenta.getPrecio()).isEqualByComparingTo(UPDATED_PRECIO);
    }

    @Test
    @Transactional
    void patchNonExistingItemFacturaVenta() throws Exception {
        int databaseSizeBeforeUpdate = itemFacturaVentaRepository.findAll().size();
        itemFacturaVenta.setId(count.incrementAndGet());

        // Create the ItemFacturaVenta
        ItemFacturaVentaDTO itemFacturaVentaDTO = itemFacturaVentaMapper.toDto(itemFacturaVenta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemFacturaVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemFacturaVentaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemFacturaVentaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemFacturaVenta in the database
        List<ItemFacturaVenta> itemFacturaVentaList = itemFacturaVentaRepository.findAll();
        assertThat(itemFacturaVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItemFacturaVenta() throws Exception {
        int databaseSizeBeforeUpdate = itemFacturaVentaRepository.findAll().size();
        itemFacturaVenta.setId(count.incrementAndGet());

        // Create the ItemFacturaVenta
        ItemFacturaVentaDTO itemFacturaVentaDTO = itemFacturaVentaMapper.toDto(itemFacturaVenta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemFacturaVentaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemFacturaVentaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemFacturaVenta in the database
        List<ItemFacturaVenta> itemFacturaVentaList = itemFacturaVentaRepository.findAll();
        assertThat(itemFacturaVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItemFacturaVenta() throws Exception {
        int databaseSizeBeforeUpdate = itemFacturaVentaRepository.findAll().size();
        itemFacturaVenta.setId(count.incrementAndGet());

        // Create the ItemFacturaVenta
        ItemFacturaVentaDTO itemFacturaVentaDTO = itemFacturaVentaMapper.toDto(itemFacturaVenta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemFacturaVentaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemFacturaVentaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemFacturaVenta in the database
        List<ItemFacturaVenta> itemFacturaVentaList = itemFacturaVentaRepository.findAll();
        assertThat(itemFacturaVentaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItemFacturaVenta() throws Exception {
        // Initialize the database
        itemFacturaVentaRepository.saveAndFlush(itemFacturaVenta);

        int databaseSizeBeforeDelete = itemFacturaVentaRepository.findAll().size();

        // Delete the itemFacturaVenta
        restItemFacturaVentaMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemFacturaVenta.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemFacturaVenta> itemFacturaVentaList = itemFacturaVentaRepository.findAll();
        assertThat(itemFacturaVentaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
