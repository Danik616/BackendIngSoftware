package com.proyecto.pqrs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("pqrsstatus")
public class PQRSStatus implements Persistable<String> {

  @Id
  private Long id;

  private String estado;

  @Transient
  private boolean isNew = true;

  public PQRSStatus(Long id, String estado) {
    this.id = id;
    this.estado = estado;
  }

  public PQRSStatus() {}

  @Override
  public boolean isNew() {
    return this.isNew;
  }

  @Override
  public String getId() {
    return String.valueOf(this.id);
  }
}
