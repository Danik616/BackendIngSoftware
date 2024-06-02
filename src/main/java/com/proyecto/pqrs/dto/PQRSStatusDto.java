package com.proyecto.pqrs.dto;

import lombok.Data;

@Data
public class PQRSStatusDto {

  private Long id;
  private String estado;

  public PQRSStatusDto(Long id, String estado) {
    this.id = id;
    this.estado = estado;
  }
}
