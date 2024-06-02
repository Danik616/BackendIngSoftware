package com.proyecto.pqrs.dto;

import lombok.Data;

@Data
public class ArchivoResponse {

  private String extension;
  private String nombreArchivo;
  private Long pqrsId;

  public ArchivoResponse(String extension, String nombreArchivo, Long pqrsId) {
    this.extension = extension;
    this.nombreArchivo = nombreArchivo;
    this.pqrsId = pqrsId;
  }
}
