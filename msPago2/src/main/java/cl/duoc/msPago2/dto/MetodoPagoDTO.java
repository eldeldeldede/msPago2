package cl.duoc.msPago2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MetodoPagoDTO {

    private Integer id;
    private String nombre; //transeferencia, tarjeta o efectivo
    

}
