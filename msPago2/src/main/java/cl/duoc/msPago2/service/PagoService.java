package cl.duoc.msPago2.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.duoc.msPago2.client.ArriendoClient;
import cl.duoc.msPago2.client.ClienteClient;
import cl.duoc.msPago2.dto.ArriendoDTO;
import cl.duoc.msPago2.dto.ClienteDTO;
import cl.duoc.msPago2.dto.PagoDTO;
import cl.duoc.msPago2.model.Pago;
import cl.duoc.msPago2.repository.PagoRepository;

@Service
public class PagoService {

    @Autowired
    private PagoRepository repo;

    @Autowired
    private ArriendoClient arriendoClient;
    
    @Autowired
    private ClienteClient clienteClient;

    public List<Pago> listar(){
        return repo.findAll();
    }

    public Pago guardar(Pago pago){
        
        
        if(pago.getComprobante() != null){
            pago.getComprobante().setPago(pago);
        }

        
        return repo.save(pago);
    }

    public Pago buscarPorId(Integer id){
        return repo.findById(id).orElseThrow(() -> new RuntimeException("pago no encontrado"));
    }

    public void eliminar(Integer id){
        if(repo.existsById(id)){
            repo.deleteById(id);
        }else{
            throw new RuntimeException("pago no encontrado");
        }
    }

    public Pago actualizar(Integer id, Pago pagoActualizar){
        Pago pago = repo.findById(id).orElseThrow(() -> new RuntimeException("pago no encontrado"));
        pago.setMonto(pagoActualizar.getMonto());    
        pago.setEstado(pagoActualizar.getEstado());
        
        if(pagoActualizar.getMetodoPago()!= null){
            
            pago.setMetodoPago(pagoActualizar.getMetodoPago());
        }

        if(pagoActualizar.getComprobante() != null){
            pago.getComprobante().setPago(pago);
            pago.setComprobante(pagoActualizar.getComprobante());
        }

       return repo.save(pago);
    }

    public PagoDTO obtenerDetallesPago(Integer id){

        Pago pago = repo.findById(id).orElseThrow(() -> new RuntimeException("pago no encontrado"));

        ArriendoDTO arriendo = arriendoClient.buscarDTO(pago.getArriendoId());
        
        ClienteDTO cliente = clienteClient.buscarDTO(pago.getClienteId());

        PagoDTO pagoCompleto = new PagoDTO();
        
        pagoCompleto.setEstado(pago.getEstado());
        pagoCompleto.setArriendoDTO(arriendo);
        pagoCompleto.setClienteDTO(cliente);

        return pagoCompleto;
    }

    
}
