package com.proyecto.pqrs.services;

import com.proyecto.pqrs.dto.PQRSStatusDto;
import com.proyecto.pqrs.repository.IPQRSStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class PQRSStatusService implements IPQRSStatusService {

  @Autowired
  private IPQRSStatusRepository pqrsStatusRepository;

  @Override
  public Flux<PQRSStatusDto> obtenerStatus() {
    return pqrsStatusRepository
      .findAll()
      .map(status ->
        new PQRSStatusDto(Long.parseLong(status.getId()), status.getEstado())
      );
  }
}
