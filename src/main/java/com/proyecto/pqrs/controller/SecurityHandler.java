package com.proyecto.pqrs.controller;

import com.proyecto.pqrs.dto.LoginDto;
import com.proyecto.pqrs.dto.TokenResponse;
import com.proyecto.pqrs.services.ClienteService;
import com.proyecto.pqrs.services.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class SecurityHandler {

  @Autowired
  private ClienteService clienteService;

  @Autowired
  private JwtUtil jwtUtil;

  public SecurityHandler() {}

  public Mono<ServerResponse> testEndpoint(ServerRequest serverRequest) {
    TokenResponse tokenResponse = new TokenResponse("pruebas");
    return ServerResponse
      .ok()
      .contentType(MediaType.APPLICATION_JSON)
      .bodyValue(tokenResponse);
  }

  public Mono<ServerResponse> createToken(ServerRequest serverRequest) {
    Mono<LoginDto> tokenRequest = serverRequest.bodyToMono(LoginDto.class);
    return tokenRequest
      .flatMap(form ->
        clienteService
          .findByUsername(form.getEmail())
          .flatMap(userDetails ->
            clienteService
              .validateCliente(
                userDetails.getUsername(),
                userDetails.getPassword()
              )
              .flatMap(valid -> {
                if (valid) {
                  return jwtUtil
                    .generateToken(userDetails.getUsername())
                    .flatMap(token -> {
                      TokenResponse tokenResponse = new TokenResponse(token);
                      return ServerResponse.ok().bodyValue(tokenResponse);
                    });
                } else {
                  return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
                }
              })
          )
          .switchIfEmpty(ServerResponse.status(HttpStatus.UNAUTHORIZED).build())
      )
      .onErrorResume(
        UsernameNotFoundException.class,
        e -> ServerResponse.status(HttpStatus.UNAUTHORIZED).build()
      )
      .onErrorResume(
        Exception.class,
        e ->
          ServerResponse
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .bodyValue("Hay un error en el servidor: " + e.getMessage())
      );
  }
}
