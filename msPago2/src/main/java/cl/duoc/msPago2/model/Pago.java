package cl.duoc.msPago2.model;

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
public class Pago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private Integer monto;
    @Column(nullable = false)
    private String estado;

    @Column(name = "arriendo_id", nullable = false)
    private Integer arriendoId;
    
    @Column(name = "cliente_id", nullable = false)
    private Integer clienteId; 

    @ManyToOne
    @JoinColumn(name = "metodo_id",nullable = false)
    private MetodoPago metodoPago;

    @OneToOne(mappedBy = "pago", cascade = CascadeType.ALL)
    private Comprobante comprobante;
}
