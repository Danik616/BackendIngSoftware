package com.proyecto.pqrs.services;

import com.proyecto.pqrs.entity.Archivos;
import com.proyecto.pqrs.repository.IArchivosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ArchivoService implements IArchivosService {

  @Autowired
  private IArchivosRepository archivosRepository;

  public Mono<Archivos> save(Archivos archivos) {
    return archivosRepository.save(archivos);
  }
}
