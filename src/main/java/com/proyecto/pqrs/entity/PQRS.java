package com.proyecto.pqrs.entity;

import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("pqrs")
public class PQRS implements Persistable<String> {

  @Id
  private Long id;

  private String numeroPQRS;
  private LocalDateTime fecha;
  private String tipo;
  private String comentarios;
  private Long clienteId;
  private Long status;

  @Transient
  private boolean isNew = true;

  public PQRS(
    String numeroPQRS,
    LocalDateTime fecha,
    String tipo,
    String comentarios,
    Long clienteId,
    Long status
  ) {
    this.numeroPQRS = numeroPQRS;
    this.fecha = fecha;
    this.tipo = tipo;
    this.comentarios = comentarios;
    this.clienteId = clienteId;
    this.status = status;
  }

  public PQRS() {}

  @Override
  public boolean isNew() {
    return this.isNew;
  }

  @Override
  public String getId() {
    return String.valueOf(this.id);
  }
}
