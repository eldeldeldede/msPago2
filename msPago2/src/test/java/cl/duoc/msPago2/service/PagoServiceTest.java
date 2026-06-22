package cl.duoc.msPago2.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import cl.duoc.msPago2.model.Comprobante;
import cl.duoc.msPago2.model.MetodoPago;
import cl.duoc.msPago2.model.Pago;
import cl.duoc.msPago2.repository.PagoRepository;

@ExtendWith(MockitoExtension.class)
public class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;
    
    @InjectMocks
    private PagoService pagoService;

    private Pago pagoEjemplo;

    @BeforeEach
    void setUp(){
        pagoEjemplo = new Pago();
        pagoEjemplo.setId(1);
        pagoEjemplo.setMonto(1000);
        pagoEjemplo.setEstado("Completado");
        pagoEjemplo.setClienteId(1);
        pagoEjemplo.setArriendoId(1);
        pagoEjemplo.setMetodoPago(new MetodoPago(1, "Webpay"));
        pagoEjemplo.setComprobante(new Comprobante(1, "Hola", LocalDate.of(2026, 1, 1), 1000, 1234, pagoEjemplo));
    }

    @Test
    void buscarPorId_Encontrado(){
         //ARRANGE: preparamos la prueba, le decimos que hacer
        Optional<Pago> optionalPago = Optional.of(pagoEjemplo);
        when(pagoRepository.findById(1)).thenReturn(optionalPago);

        //ACT: llamamos el metodo real
        Pago resultado = pagoService.buscarPorId(1);

        //ASSERT: verificamos si el usuario que retornó es el correcto
        //        (valor que deberia tener, origen)
        assertEquals(1, resultado.getId());
        assertEquals("Completado", resultado.getEstado());
    }

    @Test
    void buscarPorId_NoEncontrado(){
        //ARRANGE: preparamos la prueba pero para que retorne un doctor vacio
        Optional<Pago> pagoVacio = Optional.empty();
        when(pagoRepository.findById(99)).thenReturn(pagoVacio);

        //ACT + ASSERT: verificamos si lanza la excepcion correcta
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagoService.buscarPorId(99);
        });

        assertEquals("Pago no encontrado", exception.getMessage());
    }

    @Test
    void guardar(){
        //ARRANGE: configuramos que el repository retorne el usuario guardado
        when(pagoRepository.save(pagoEjemplo)).thenReturn(pagoEjemplo);

        //ACT: 
        Pago resultado = pagoService.guardar(pagoEjemplo);

        //ASSERT:
        assertEquals(1, resultado.getId());
    }
 
    @Test
    void eliminarExitoso(){
        //ARRANGE: el usuario existe
        when(pagoRepository.existsById(1)).thenReturn(true);

        //ASSERT: no debe lanzar error/exception
        assertDoesNotThrow(() -> pagoService.eliminar(1));

        //verificamos que el deleteByID fue exitoso solo una vez
        verify(pagoRepository, times(1)).deleteById(1);
    }

    @Test
    void eliminarNoExiste(){
        // ARRANGE: configuramos el mock para que simule que el usuario NO existe
        when(pagoRepository.existsById(99)).thenReturn(false);

        // ACT & ASSERT: verificamos que lance la excepción esperada
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pagoService.eliminar(99);
        });

        // Verificamos que el mensaje de error sea el correcto (ajústalo si tu servicio usa otro mensaje)
        assertEquals("Pago no encontrado", exception.getMessage());
        
        // Opcional: aseguramos que NUNCA se intente borrar si no existe
        verify(pagoRepository, times(0)).deleteById(99);
    }

    


}
