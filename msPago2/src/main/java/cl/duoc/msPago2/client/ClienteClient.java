package cl.duoc.msPago2.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import cl.duoc.msPago2.dto.ClienteDTO;

// @FeignClient :
// Esta anotacion le dice a spring que esta interfaz
// Representa un cliente HTTP hacia otro microservicio

// name -> el nombre logico del microservicio
// url -> direccion donde esta el microservicio

@FeignClient(name = "msCliente")

public interface ClienteClient {

    @GetMapping("api/v1/clientes/dto/{id}")
    ClienteDTO buscarDTO(@PathVariable("id") Integer id);

    

}
