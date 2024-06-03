package com.proyecto.pqrs.services;

import com.proyecto.pqrs.dto.PQRSResponse;
import com.proyecto.pqrs.entity.PQRS;
import reactor.core.publisher.Mono;

public interface IPQRSService {
  public Mono<PQRSResponse> save(PQRS pqrs);

  public Mono<PQRS> findByNumeroPQRS(String numeroPQRS);
}
