package com.proyecto.pqrs.controller;

import com.proyecto.pqrs.dto.PQRRequest;
import com.proyecto.pqrs.entity.Archivos;
import com.proyecto.pqrs.entity.PQRS;
import com.proyecto.pqrs.services.ArchivoService;
import com.proyecto.pqrs.services.ClienteService;
import com.proyecto.pqrs.services.PQRSStatusService;
import com.proyecto.pqrs.services.PQRService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.Part;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
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

  public Mono<ServerResponse> guardarArchivo(ServerRequest serverRequest) {
    return serverRequest
      .principal()
      .cast(Authentication.class)
      .flatMap(authentication -> {
        String pqrsId = serverRequest.pathVariable("pqrsId");

        return serverRequest
          .body(BodyExtractors.toMultipartData())
          .flatMap(multipartData -> {
            List<Part> fileParts = multipartData.get("archivos");

            if (fileParts != null && !fileParts.isEmpty()) {
              Flux<Archivos> archivosFlux = Flux
                .fromIterable(fileParts)
                .flatMap(part -> {
                  String filename = part
                    .headers()
                    .getContentDisposition()
                    .getFilename();
                  if (filename == null) {
                    return Mono.error(
                      new RuntimeException(
                        "No se pudo obtener el nombre del archivo"
                      )
                    );
                  }

                  return DataBufferUtils
                    .join(part.content())
                    .flatMap(dataBuffer -> {
                      byte[] bytes = new byte[dataBuffer.readableByteCount()];
                      dataBuffer.read(bytes);

                      Archivos archivo = new Archivos(
                        bytes,
                        obtenerExtension(filename),
                        filename,
                        Long.parseLong(pqrsId)
                      );

                      return archivosService
                        .save(archivo)
                        .map(response ->
                          new Archivos(
                            null,
                            response.getExtension(),
                            response.getNombreArchivo(),
                            response.getPqrsId()
                          )
                        );
                    });
                });

              return ServerResponse.ok().body(archivosFlux, Archivos.class);
            } else {
              return ServerResponse
                .badRequest()
                .bodyValue("No se encontraron archivos en la solicitud");
            }
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

  public String obtenerExtension(String filename) {
    int lastIndex = filename.lastIndexOf('.');
    if (lastIndex != -1 && lastIndex < filename.length() - 1) {
      return filename.substring(lastIndex + 1);
    }
    return "";
  }
}
