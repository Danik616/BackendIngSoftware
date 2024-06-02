package com.proyecto.pqrs.repository;

import com.proyecto.pqrs.entity.Archivos;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface IArchivosRepository extends R2dbcRepository<Archivos, Long> {}
