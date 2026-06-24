package cl.duoc.msPago2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.duoc.msPago2.dto.PagoDTO;
import cl.duoc.msPago2.model.Pago;
import cl.duoc.msPago2.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/v1/pagos")
@Tag(name = "Pago Controller", description = "Controlador para gestionar los pagos en el sistema del Rent a Car.")
public class PagoController {

    @Autowired
    private PagoService service;

    @GetMapping
    @Operation(
        summary = "Obtener la lista de pagos registrados",
        description = "Retorna la lista de pagos registrados en el sistema del Rent a Car."
    )
    public ResponseEntity<List<Pago>> listar(){
        List<Pago> pago = service.listar();
        if(pago.isEmpty()){
            return ResponseEntity.noContent().build();
        }else{
            return ResponseEntity.ok(pago);
        }
    }

    @GetMapping("/id/{id}")
    @Operation(
        summary = "Buscar pago por ID",
        description = "Retorna los detalles de un pago específico por su ID."
    )
    public ResponseEntity<Pago> buscarPorId(@PathVariable Integer id){
        try {
            Pago pago = service.buscarPorId(id);
            return ResponseEntity.ok(pago);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @Operation(
        summary = "Registrar un nuevo pago",
        description = "Permite registrar un nuevo pago en el sistema del Rent a Car."
    )
    public ResponseEntity<Pago> guardar(@RequestBody Pago pago){

            Pago pagoNuevo = service.guardar(pago);
            return ResponseEntity.ok(pagoNuevo);
    }

    @DeleteMapping("/id/{id}")
    @Operation(
        summary = "Eliminar pago por ID",
        description = "Permite eliminar un pago específico por su ID."
    )
    public ResponseEntity<?> eliminar(@PathVariable Integer id){
        try {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(
        summary = "Actualizar pago",
        description = "Permite actualizar los detalles de un pago específico por su ID."
    )
    public ResponseEntity<Pago> actualizar(@PathVariable Integer id, @RequestBody Pago pagoActualizar){
        try {
            Pago pago = service.actualizar(id, pagoActualizar);
            return ResponseEntity.ok(pago);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/detalle/{id}")
    @Operation(
        summary = "Obtener detalles de un pago",
        description = "Retorna los detalles de un pago específico por su ID."
    )
    public ResponseEntity<PagoDTO> detallePagoDTO(@PathVariable Integer id){
        try {
            PagoDTO pagoDTO = service.obtenerDetallesPago(id);
            return ResponseEntity.ok(pagoDTO);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

}
