package com.proyecto.pqrs.repository;

import com.proyecto.pqrs.entity.PQRSStatus;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface IPQRSStatusRepository
  extends R2dbcRepository<PQRSStatus, Long> {}
