package com.proyecto.pqrs;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.proyecto.pqrs.entity.Archivos;
import com.proyecto.pqrs.entity.Cliente;
import com.proyecto.pqrs.entity.PQRS;
import com.proyecto.pqrs.entity.PQRSStatus;
import com.proyecto.pqrs.repository.IArchivosRepository;
import com.proyecto.pqrs.repository.IClienteRepository;
import com.proyecto.pqrs.repository.IPQRSRepository;
import com.proyecto.pqrs.repository.IPQRSStatusRepository;
import com.proyecto.pqrs.services.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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

  @Autowired
  private JwtUtil jwtUtil;

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

  @Test
  public void testGenerateAndValidateToken() {
    // Act
    String token = jwtUtil.generateToken("juan@example.com").block();
    Mono<Boolean> validationResultMono = jwtUtil.validateToken(token);

    // Assert
    assertNotNull(token);
    assertTrue(token.length() > 0);
    assertTrue(validationResultMono.block()); // Validate generated token
  }
}
