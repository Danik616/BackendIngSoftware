package com.proyecto.pqrs.controller;

import com.proyecto.pqrs.services.PQRSStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class PqrsHandler {

  @Autowired
  private PQRSStatusService pqrsStatusService;

  public Mono<ServerResponse> guardarPQR(ServerRequest serverRequest) {
    return serverRequest
      .principal()
      .cast(Authentication.class)
      .flatMap(autentication -> {
        return ServerResponse.ok().bodyValue("#");
      });
  }

  public Mono<ServerResponse> obtenerEstados(ServerRequest serverRequest) {
    return serverRequest
      .principal()
      .cast(Authentication.class)
      .flatMap(authentication ->
        pqrsStatusService
          .obtenerStatus()
          .collectList()
          .flatMap(estados -> ServerResponse.ok().bodyValue(estados))
      );
  }
}
