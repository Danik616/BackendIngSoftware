package com.proyecto.pqrs.dto;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class PQRSResponse {

  private String numeroPQRS;
  private LocalDateTime fecha;
  private String tipo;
  private String comentarios;
  private Long status;
}
