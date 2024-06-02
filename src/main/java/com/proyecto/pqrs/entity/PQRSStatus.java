package com.proyecto.pqrs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("pqrsstatus")
public class PQRSStatus {

  @Id
  private Long id;

  private String estado;

  public PQRSStatus(Long id, String estado) {
    this.id = id;
    this.estado = estado;
  }

  public PQRSStatus() {}
}
