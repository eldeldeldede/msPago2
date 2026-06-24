package cl.duoc.msPago2.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pago")
@Schema(description = "Entidad que representa un pago realizado por un cliente en el sistema del Rent a Car.")
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del pago.")
    private Integer id;
    @Column(nullable = false)
    @Schema(description = "Monto del pago.")
    private Integer monto;
    @Column(nullable = false)
    @Schema(description = "Estado del pago.")
    private String estado;

    @Column(name = "arriendo_id", nullable = false)
    @Schema(description = "Identificador del arriendo al que corresponde el pago.")
    private Integer arriendoId;
    
    @Column(name = "cliente_id", nullable = false)
    @Schema(description = "Identificador del cliente que realiza el pago.")
    private Integer clienteId; 

    @ManyToOne
    @JoinColumn(name = "metodo_id",nullable = false)
    @Schema(description = "Método de pago utilizado.")
    private MetodoPago metodoPago;

    @OneToOne(mappedBy = "pago", cascade = CascadeType.ALL)
    @Schema(description = "Comprobante del pago.")
    private Comprobante comprobante;
}
