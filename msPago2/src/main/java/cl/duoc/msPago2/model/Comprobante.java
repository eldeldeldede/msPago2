package cl.duoc.msPago2.model;

import java.time.LocalDate;


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
public class Comprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String informacion;

    @Column(nullable = false)
    private LocalDate fecha_emision;

    @Column(nullable = false)
    private Integer total;

    @Column(nullable = false, unique = true)
    private Integer numeroBoleta;

    @OneToOne
    @JoinColumn(name = "pago_id")
    private Pago pago;
}
