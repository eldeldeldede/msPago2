package cl.duoc.msPago2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArriendoDTO {

    private Integer id;
    private String fechaInicio;
    private String fechaFin;
}
