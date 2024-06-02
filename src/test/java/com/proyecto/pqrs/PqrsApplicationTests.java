package com.proyecto.pqrs;

import com.proyecto.pqrs.entity.Archivos;
import com.proyecto.pqrs.entity.Cliente;
import com.proyecto.pqrs.entity.PQRS;
import com.proyecto.pqrs.entity.PQRSStatus;
import com.proyecto.pqrs.repository.IArchivosRepository;
import com.proyecto.pqrs.repository.IClienteRepository;
import com.proyecto.pqrs.repository.IPQRSRepository;
import com.proyecto.pqrs.repository.IPQRSStatusRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
class PqrsApplicationTests {

  @Autowired
  private IArchivosRepository iArchivosRepository;

  @Autowired
  private IClienteRepository iClienteRepository;

  @Autowired
  private IPQRSRepository ipqrsRepository;

  @Autowired
  private IPQRSStatusRepository ipqrsStatusRepository;

  @Test
  void contextLoads() {}

  @Test
  public void testingArchivos() {
    Flux<Archivos> getArchivos = iArchivosRepository.findAll();

    StepVerifier.create(getArchivos).expectNextCount(1).verifyComplete();
  }

  @Test
  public void testingCliente() {
    Flux<Cliente> getCliente = iClienteRepository.findAll();

    StepVerifier.create(getCliente).expectNextCount(1).verifyComplete();
  }

  @Test
  public void testingPqrs() {
    Flux<PQRS> getPqrs = ipqrsRepository.findAll();

    StepVerifier.create(getPqrs).expectNextCount(1).verifyComplete();
  }

  @Test
  public void testingPqrsStatus() {
    Flux<PQRSStatus> getPqrsStatus = ipqrsStatusRepository.findAll();

    StepVerifier.create(getPqrsStatus).expectNextCount(1).verifyComplete();
  }
}
