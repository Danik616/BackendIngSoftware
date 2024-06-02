package com.proyecto.pqrs.repository;

import com.proyecto.pqrs.entity.PQRS;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface IPQRSRepository extends R2dbcRepository<PQRS, Long> {}
