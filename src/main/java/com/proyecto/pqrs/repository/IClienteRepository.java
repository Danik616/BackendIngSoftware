package com.proyecto.pqrs.repository;

import com.proyecto.pqrs.entity.Cliente;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Mono;

public interface IClienteRepository extends R2dbcRepository<Cliente, Long> {
  @Query("SELECT * FROM CLIENTE WHERE EMAIL = :email")
  Mono<Cliente> findByEmail(@Param(":email") String email);
}
