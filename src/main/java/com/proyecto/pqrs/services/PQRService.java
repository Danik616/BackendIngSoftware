package com.proyecto.pqrs.services;

import com.proyecto.pqrs.dto.PQRSResponse;
import com.proyecto.pqrs.entity.PQRS;
import com.proyecto.pqrs.repository.IPQRSRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PQRService implements IPQRSService {

  @Autowired
  private IPQRSRepository pqrsRepository;

  public Mono<PQRSResponse> save(PQRS pqrs) {
    return pqrsRepository
      .save(pqrs)
      .map(savedPqrs -> {
        // Mapear el objeto PQRS a un nuevo objeto sin los campos id y new
        PQRSResponse response = new PQRSResponse();
        response.setNumeroPQRS(savedPqrs.getNumeroPQRS());
        response.setFecha(savedPqrs.getFecha());
        response.setTipo(savedPqrs.getTipo());
        response.setComentarios(savedPqrs.getComentarios());
        response.setStatus(savedPqrs.getStatus());
        return response;
      });
  }

  public Mono<PQRS> findByNumeroPQRS(String numeroPQRS) {
    return pqrsRepository.findByNumeroPQRS(numeroPQRS);
  }
}
