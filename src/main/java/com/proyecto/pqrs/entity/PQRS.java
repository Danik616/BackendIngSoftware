package com.proyecto.pqrs.entity;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("pqrs")
public class PQRS {

  @Id
  private Long id;

  private String numeroPQRS;
  private LocalDateTime fecha;
  private String tipo;
  private String comentarios;
  private Long clienteId;
  private Long status;

  public PQRS(
    Long id,
    String numeroPQRS,
    LocalDateTime fecha,
    String tipo,
    String comentarios,
    Long clienteId,
    Long status
  ) {
    this.id = id;
    this.numeroPQRS = numeroPQRS;
    this.fecha = fecha;
    this.tipo = tipo;
    this.comentarios = comentarios;
    this.clienteId = clienteId;
    this.status = status;
  }

  public PQRS() {}
}
