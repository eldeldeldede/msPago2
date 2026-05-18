package cl.duoc.msPago2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.duoc.msPago2.model.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Integer> {

}
