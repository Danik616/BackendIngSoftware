package com.proyecto.pqrs.repository;

import com.proyecto.pqrs.entity.PQRS;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;

public interface IPQRSRepository extends R2dbcRepository<PQRS, Long> {
  @Query("select * from pqrs where numero_pqrs = :numeroPQRS")
  Mono<PQRS> findByNumeroPQRS(@Param("numeroPQRS") String numeroPQRS);
}
