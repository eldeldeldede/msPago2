package cl.duoc.msPago2.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.msPago2.client.ArriendoClient;
import cl.duoc.msPago2.client.ClienteClient;
import cl.duoc.msPago2.dto.ArriendoDTO;
import cl.duoc.msPago2.dto.ClienteDTO;
import cl.duoc.msPago2.dto.PagoDTO;
import cl.duoc.msPago2.model.Comprobante;
import cl.duoc.msPago2.model.MetodoPago;
import cl.duoc.msPago2.model.Pago;
import cl.duoc.msPago2.repository.PagoRepository;

@ExtendWith(MockitoExtension.class)
public class PagoServiceTest {

    @Mock
    private PagoRepository repo; 

    @Mock
    private ArriendoClient arriendoClient; 

    @Mock
    private ClienteClient clienteClient; 

    @InjectMocks
    private PagoService pagoService;

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
    // listar
    // ─────────────────────────────────────────────

    @Test
    void listar_retornaLista() {
        // ARRANGE
        when(repo.findAll()).thenReturn(List.of(pagoEjemplo));

        // ACT
        List<Pago> resultado = pagoService.listar();

        // ASSERT
        assertEquals(1, resultado.size());
        assertEquals("Completado", resultado.get(0).getEstado());
    }

    // ─────────────────────────────────────────────
    // buscarPorId
    // ─────────────────────────────────────────────

    @Test
    void buscarPorId_Encontrado() {
        // ARRANGE
        when(repo.findById(1)).thenReturn(Optional.of(pagoEjemplo));

        // ACT
        Pago resultado = pagoService.buscarPorId(1);

        // ASSERT
        assertEquals(1, resultado.getId());
        assertEquals("Completado", resultado.getEstado());
    }

    @Test
    void buscarPorId_NoEncontrado() {
        // ARRANGE
        when(repo.findById(99)).thenReturn(Optional.empty());

        // ACT + ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagoService.buscarPorId(99);
        });

        assertEquals("Pago no encontrado", exception.getMessage());
    }

    // ─────────────────────────────────────────────
    // guardar
    // ─────────────────────────────────────────────

    @Test
    void guardar_conComprobante() {
        // ARRANGE: pagoEjemplo ya tiene comprobante seteado en setUp
        when(repo.save(pagoEjemplo)).thenReturn(pagoEjemplo);

        // ACT
        Pago resultado = pagoService.guardar(pagoEjemplo);

        // ASSERT: verifica que el comprobante queda enlazado al pago
        assertEquals(1, resultado.getId());
        assertNotNull(resultado.getComprobante());
        assertEquals(pagoEjemplo, resultado.getComprobante().getPago());
    }

    @Test
    void guardar_sinComprobante() {
        // ARRANGE: pago sin comprobante
        Pago pagoSinComprobante = new Pago();
        pagoSinComprobante.setId(2);
        pagoSinComprobante.setMonto(500);
        pagoSinComprobante.setEstado("Pendiente");
        pagoSinComprobante.setComprobante(null);

        when(repo.save(pagoSinComprobante)).thenReturn(pagoSinComprobante);

        // ACT
        Pago resultado = pagoService.guardar(pagoSinComprobante);

        // ASSERT
        assertEquals(2, resultado.getId());
        assertEquals(null, resultado.getComprobante());
    }

    // ─────────────────────────────────────────────
    // eliminar
    // ─────────────────────────────────────────────

    @Test
    void eliminarExitoso() {
        // ARRANGE
        when(repo.existsById(1)).thenReturn(true);

        // ACT + ASSERT
        assertDoesNotThrow(() -> pagoService.eliminar(1));
        verify(repo, times(1)).deleteById(1);
    }

    @Test
    void eliminarNoExiste() {
        // ARRANGE
        when(repo.existsById(99)).thenReturn(false);

        // ACT + ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagoService.eliminar(99);
        });

        assertEquals("Pago no encontrado", exception.getMessage());
        verify(repo, times(0)).deleteById(99);
    }

    // ─────────────────────────────────────────────
    // actualizar
    // ─────────────────────────────────────────────

    @Test
    void actualizar_exitoso() {
        // ARRANGE
        Pago datosNuevos = new Pago();
        datosNuevos.setMonto(2000);
        datosNuevos.setEstado("Anulado");
        datosNuevos.setMetodoPago(new MetodoPago(2, "Transferencia"));
        datosNuevos.setComprobante(null); // sin comprobante nuevo

        when(repo.findById(1)).thenReturn(Optional.of(pagoEjemplo));
        when(repo.save(pagoEjemplo)).thenReturn(pagoEjemplo);

        // ACT
        Pago resultado = pagoService.actualizar(1, datosNuevos);

        // ASSERT
        assertEquals("Anulado", resultado.getEstado());
        assertEquals(2000, resultado.getMonto());
        verify(repo, times(1)).save(pagoEjemplo);
    }

    @Test
    void actualizar_noEncontrado() {
        // ARRANGE
        when(repo.findById(99)).thenReturn(Optional.empty());

        // ACT + ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagoService.actualizar(99, new Pago());
        });

        assertEquals("Pago no encontrado", exception.getMessage());
        verify(repo, times(0)).save(any(Pago.class));
    }

    // ─────────────────────────────────────────────
    // obtenerDetallesPago
    // ─────────────────────────────────────────────

    @Test
    void obtenerDetallesPago_exitoso() {
        // ARRANGE
        ArriendoDTO arriendoSimulado = new ArriendoDTO();
        ClienteDTO clienteSimulado = new ClienteDTO();

        when(repo.findById(1)).thenReturn(Optional.of(pagoEjemplo));
        when(arriendoClient.buscarDTO(1)).thenReturn(arriendoSimulado);
        when(clienteClient.buscarDTO(1)).thenReturn(clienteSimulado);

        // ACT
        PagoDTO resultado = pagoService.obtenerDetallesPago(1);

        // ASSERT
        assertNotNull(resultado);
        assertEquals("Completado", resultado.getEstado());
        assertNotNull(resultado.getArriendoDTO());
        assertNotNull(resultado.getClienteDTO());
    }

    @Test
    void obtenerDetallesPago_noEncontrado() {
        // ARRANGE
        when(repo.findById(99)).thenReturn(Optional.empty());

        // ACT + ASSERT
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagoService.obtenerDetallesPago(99);
        });

        assertEquals("Pago no encontrado", exception.getMessage());
    }
}