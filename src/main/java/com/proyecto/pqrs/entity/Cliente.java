package com.proyecto.pqrs.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("cliente")
public class Cliente {

  @Id
  private Long id;

  private String tipoIdentificacion;
  private String numeroIdentificacion;
  private String nombre;
  private String email;
  private String telefono;
  private String password;

  public Cliente(
    Long id,
    String tipoIdentificacion,
    String numeroIdentificacion,
    String nombre,
    String email,
    String telefono,
    String password
  ) {
    this.id = id;
    this.tipoIdentificacion = tipoIdentificacion;
    this.numeroIdentificacion = numeroIdentificacion;
    this.nombre = nombre;
    this.email = email;
    this.telefono = telefono;
    this.password = password;
  }

  public Cliente() {}
}
