package com.proyecto.pqrs.controller;

import com.proyecto.pqrs.dto.PQRRequest;
import com.proyecto.pqrs.entity.PQRS;
import com.proyecto.pqrs.services.ArchivoService;
import com.proyecto.pqrs.services.ClienteService;
import com.proyecto.pqrs.services.PQRSStatusService;
import com.proyecto.pqrs.services.PQRService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class PqrsHandler {

  @Autowired
  private PQRSStatusService pqrsStatusService;

  @Autowired
  private ClienteService clienteService;

  @Autowired
  private PQRService pqrsService;

  @Autowired
  private ArchivoService archivosService;

  public Mono<ServerResponse> guardarPQR(ServerRequest serverRequest) {
    return serverRequest
      .principal()
      .cast(Authentication.class)
      .flatMap(authentication -> {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = userDetails.getUsername();

        return clienteService
          .findUserByEmail(email)
          .flatMap(user -> {
            String numeroIdentificacion = user.getNumeroIdentificacion();
            LocalDate fechaActual = LocalDate.now();
            String numeroPQRS =
              numeroIdentificacion +
              "_" +
              fechaActual.getYear() +
              "_" +
              String.format("%02d", fechaActual.getMonthValue()) +
              String.format("%02d", fechaActual.getDayOfMonth());
            LocalDateTime fecha = LocalDateTime.now();

            return serverRequest
              .bodyToMono(PQRRequest.class)
              .flatMap(pqrRequest -> {
                if (pqrRequest.getStatus() == null) {
                  return ServerResponse
                    .badRequest()
                    .bodyValue("El campo 'status' es obligatorio.");
                }
                if (pqrRequest.getComentarios() == null) {
                  return ServerResponse
                    .badRequest()
                    .bodyValue("El campo 'comentarios' es obligatorio.");
                }
                if (pqrRequest.getTipo() == null) {
                  return ServerResponse
                    .badRequest()
                    .bodyValue("El campo 'tipo' es obligatorio.");
                }

                return pqrsStatusService
                  .obtenerStatus()
                  .collectList()
                  .flatMap(statusList -> {
                    if (
                      statusList
                        .stream()
                        .anyMatch(status ->
                          status.getId().equals(pqrRequest.getStatus())
                        )
                    ) {
                      PQRS pqrs = new PQRS(
                        numeroPQRS,
                        fecha,
                        pqrRequest.getTipo(),
                        pqrRequest.getComentarios(),
                        Long.parseLong(user.getId()),
                        pqrRequest.getStatus()
                      );

                      return pqrsService
                        .save(pqrs)
                        .flatMap(savedPqrs ->
                          ServerResponse.ok().bodyValue(savedPqrs)
                        );
                    } else {
                      return ServerResponse
                        .badRequest()
                        .bodyValue("El estado proporcionado no es válido.");
                    }
                  });
              });
          });
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
