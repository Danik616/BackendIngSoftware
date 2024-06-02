package com.proyecto.pqrs.services;

import com.proyecto.pqrs.dto.PQRSStatusDto;
import reactor.core.publisher.Flux;

public interface IPQRSStatusService {
  Flux<PQRSStatusDto> obtenerStatus();
}
