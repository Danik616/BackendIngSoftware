package com.proyecto.pqrs.repository;

import com.proyecto.pqrs.entity.Cliente;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface IClienteRepository extends R2dbcRepository<Cliente, Long> {}
