package cl.duoc.msPago2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "metodo_pago")
@Schema(description = "Entidad que representa un método de pago disponible en el sistema del Rent a Car.")
public class MetodoPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del método de pago.")
    private Integer id;

    @Column(nullable = false)
    @Schema(description = "Nombre del método de pago.")
    private String nombre;

}
