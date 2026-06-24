package cl.duoc.msPago2.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import cl.duoc.msPago2.dto.PagoDTO;
import cl.duoc.msPago2.model.Comprobante;
import cl.duoc.msPago2.model.MetodoPago;
import cl.duoc.msPago2.model.Pago;
import cl.duoc.msPago2.service.PagoService;

@WebMvcTest(PagoController.class)
public class PagoControllerTest {

    @Autowired
    private MockMvc mock;

    @MockitoBean
    private PagoService service;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    private Pago pagoEjemplo;

    @BeforeEach
    void setUp() {
        pagoEjemplo = new Pago();
        pagoEjemplo.setId(1);
        pagoEjemplo.setMonto(1000);
        pagoEjemplo.setEstado("Completado");
        pagoEjemplo.setClienteId(1);
        pagoEjemplo.setArriendoId(1);
        pagoEjemplo.setMetodoPago(new MetodoPago(1, "Webpay"));
        pagoEjemplo.setComprobante(new Comprobante(1, "Hola", LocalDate.of(2026, 1, 1), 1000, 1234, pagoEjemplo));
    }

    // ─────────────────────────────────────────────
    // GET /api/v1/pagos  →  listar()
    // ─────────────────────────────────────────────

    @Test
    void listar_retorna200ConLista() throws Exception {
        // ARRANGE
        when(service.listar()).thenReturn(List.of(pagoEjemplo));

        // ACT + ASSERT
        mock.perform(get("/api/v1/pagos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].estado").value("Completado"));
    }

    @Test
    void listar_retorna204CuandoListaVacia() throws Exception {
        // ARRANGE: lista vacía → controller devuelve 204 noContent
        when(service.listar()).thenReturn(Collections.emptyList());

        // ACT + ASSERT
        mock.perform(get("/api/v1/pagos"))
                .andExpect(status().isNoContent());
    }

    // ─────────────────────────────────────────────
    // GET /api/v1/pagos/id/{id}  →  buscarPorId()
    // ─────────────────────────────────────────────

    @Test
    void buscarPago_retorna200() throws Exception {
        // ARRANGE
        when(service.buscarPorId(1)).thenReturn(pagoEjemplo);

        // ACT + ASSERT
        mock.perform(get("/api/v1/pagos/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("Completado"));
    }

    @Test
    void buscarPago_retorna404() throws Exception {
        // ARRANGE
        when(service.buscarPorId(99)).thenThrow(new RuntimeException("Pago no encontrado"));

        // ACT + ASSERT
        mock.perform(get("/api/v1/pagos/id/99"))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────
    // POST /api/v1/pagos  →  guardar()
    // ─────────────────────────────────────────────

    @Test
    void guardar_retorna200() throws Exception {
        // ARRANGE: el controller no tiene try/catch, siempre devuelve 200
        when(service.guardar(any(Pago.class))).thenReturn(pagoEjemplo);

        // ACT + ASSERT
        mock.perform(post("/api/v1/pagos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pagoEjemplo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.estado").value("Completado"));
    }

    // ─────────────────────────────────────────────
    // DELETE /api/v1/pagos/id/{id}  →  eliminar()
    // ─────────────────────────────────────────────

    @Test
    void eliminar_retorna204() throws Exception {
        // ARRANGE
        doNothing().when(service).eliminar(1);

        // ACT + ASSERT
        mock.perform(delete("/api/v1/pagos/id/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void eliminar_retorna404() throws Exception {
        // ARRANGE
        doThrow(new RuntimeException("Pago no encontrado")).when(service).eliminar(99);

        // ACT + ASSERT
        mock.perform(delete("/api/v1/pagos/id/99"))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────
    // PUT /api/v1/pagos/{id}  →  actualizar()
    // ─────────────────────────────────────────────

    @Test
    void actualizar_retorna200() throws Exception {
        // ARRANGE
        when(service.actualizar(eq(1), any(Pago.class))).thenReturn(pagoEjemplo);

        // ACT + ASSERT
        mock.perform(put("/api/v1/pagos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pagoEjemplo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("Completado"));
    }

    @Test
    void actualizar_retorna404() throws Exception {
        // ARRANGE
        when(service.actualizar(eq(99), any(Pago.class)))
                .thenThrow(new RuntimeException("Pago no encontrado"));

        // ACT + ASSERT
        mock.perform(put("/api/v1/pagos/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pagoEjemplo)))
                .andExpect(status().isNotFound());
    }

    // ─────────────────────────────────────────────
    // GET /api/v1/pagos/detalle/{id}  →  detallePagoDTO()
    // ─────────────────────────────────────────────

    @Test
    void detallePagoDTO_retorna200() throws Exception {
        // ARRANGE
        PagoDTO dto = new PagoDTO();
        dto.setEstado("Completado");
        when(service.obtenerDetallesPago(1)).thenReturn(dto);

        // ACT + ASSERT
        mock.perform(get("/api/v1/pagos/detalle/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("Completado"));
    }

    @Test
    void detallePagoDTO_retorna404() throws Exception {
        // ARRANGE
        when(service.obtenerDetallesPago(99)).thenThrow(new RuntimeException("Pago no encontrado"));

        // ACT + ASSERT
        mock.perform(get("/api/v1/pagos/detalle/99"))
                .andExpect(status().isNotFound());
    }
}