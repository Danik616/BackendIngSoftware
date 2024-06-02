package com.proyecto.pqrs.dto;

import lombok.Data;

@Data
public class PQRRequest {

  private String nombres;

  private String apellidos;

  private String tipoIdentificacion;

  private String numeroIdentificacion;

  private String email;

  private Long status;

  private String telefono;

  private String tipo;

  private String comentarios;
}
