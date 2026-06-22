package cl.duoc.msPago2.controller;

import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import cl.duoc.msPago2.model.Comprobante;
import cl.duoc.msPago2.model.MetodoPago;
import cl.duoc.msPago2.model.Pago;
import cl.duoc.msPago2.service.PagoService;

@WebMvcTest(PagoController.class)//Levanta solo la capa web, no la base de datos
public class PagoControllerTest {

    @Autowired
    private MockMvc mock;
    
    @MockitoBean
    private PagoService service;

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
    void buscarPago_retorna200() throws Exception{
        //ARRANGE: el service debe retornar el usuario
        when(service.buscarPorId(1)).thenReturn(pagoEjemplo);

        //ACT + ASSERT
        mock.perform(get("/api/v1/pagos/id/1"))
            .andExpect(status().isOk());
    }

    @Test
    void buscarPago_retonar404() throws Exception{
        //ARRANGE: el service debe retornar el usuario
        when(service.buscarPorId(99)).thenThrow(new RuntimeException("Pago no encontrado"));

        //ACT + ASSERT
        mock.perform(get("/api/v1/pagos/id/99"))
            .andExpect(status().isNotFound());
    }
}
