package cl.duoc.msPago2.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comprobante")
@Schema(description = "Entidad que representa un comprobante de pago generado en el sistema del Rent a Car.")
public class Comprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del comprobante.")
    private Integer id;

    @Column(nullable = false)
    @Schema(description = "Información del comprobante.")
    private String informacion;

    @Column(nullable = false)
    @Schema(description = "Fecha de emisión del comprobante.")
    private LocalDate fecha_emision;

    @Column(nullable = false)
    @Schema(description = "Total del comprobante.")
    private Integer total;

    @Column(nullable = false, unique = true)
    @Schema(description = "Número de la boleta.")
    private Integer numeroBoleta;

    @OneToOne
    @Schema(description = "Pago asociado al comprobante.")
    @JoinColumn(name = "pago_id")
    @JsonBackReference
    private Pago pago;
}
