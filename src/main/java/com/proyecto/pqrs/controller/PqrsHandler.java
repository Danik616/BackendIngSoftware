package com.proyecto.pqrs.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class PqrsHandler {

  public Mono<ServerResponse> guardarPQR(ServerRequest serverRequest) {
    return serverRequest
      .principal()
      .cast(Authentication.class)
      .flatMap(autentication -> {
        return ServerResponse.ok().bodyValue("#");
      });
  }
}
