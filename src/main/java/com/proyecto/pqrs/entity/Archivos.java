package com.proyecto.pqrs.entity;

import lombok.Data;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("archivos")
public class Archivos {

  @Id
  private Long id;

  private ByteArrayResource archivo;
  private String extension;
  private String nombreArchivo;
  private Long pqrsId;

  public Archivos(
    Long id,
    ByteArrayResource archivo,
    String extension,
    String nombreArchivo,
    Long pqrsId
  ) {
    this.id = id;
    this.archivo = archivo;
    this.extension = extension;
    this.nombreArchivo = nombreArchivo;
    this.pqrsId = pqrsId;
  }

  public Archivos() {}
}
