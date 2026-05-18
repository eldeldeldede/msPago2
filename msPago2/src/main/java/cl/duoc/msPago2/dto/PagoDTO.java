package cl.duoc.msPago2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoDTO {
    private Integer id;
    private Integer monto;
    private String estado;
    

    private ArriendoDTO arriendoDTO;
    private ClienteDTO clienteDTO;
}
