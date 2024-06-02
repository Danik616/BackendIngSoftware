package com.proyecto.pqrs.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("archivos")
public class Archivos implements Persistable<String> {

  @Transient
  @Id
  @JsonIgnore
  private Long id;

  @JsonIgnore
  private byte[] archivo;

  private String extension;
  private String nombreArchivo;
  private Long pqrsId;

  @Transient
  @JsonIgnore
  private boolean isNew = true;

  public Archivos(
    byte[] archivo,
    String extension,
    String nombreArchivo,
    Long pqrsId
  ) {
    this.archivo = archivo;
    this.extension = extension;
    this.nombreArchivo = nombreArchivo;
    this.pqrsId = pqrsId;
  }

  public Archivos() {}

  @Override
  public boolean isNew() {
    return this.isNew;
  }

  @Override
  public String getId() {
    return String.valueOf(this.id);
  }
}
