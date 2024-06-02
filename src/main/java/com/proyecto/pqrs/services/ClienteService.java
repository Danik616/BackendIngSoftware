package com.proyecto.pqrs.services;

import com.proyecto.pqrs.entity.Cliente;
import com.proyecto.pqrs.repository.IClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ClienteService implements IClienteService {

  @Autowired
  private IClienteRepository clienteRepository;

  @Override
  public Mono<UserDetails> findByUsername(String email) {
    String user = email.trim();
    return clienteRepository
      .findByEmail(user)
      .map(cliente -> {
        return User
          .builder()
          .username(cliente.getEmail())
          .password(cliente.getPassword())
          .authorities("ROLE_USER")
          .build();
      })
      .switchIfEmpty(
        Mono.error(
          new UsernameNotFoundException("User not found with user: " + user)
        )
      )
      .onErrorResume(
        NullPointerException.class,
        e -> {
          System.out.println("Error");
          return Mono.error(
            new UsernameNotFoundException("Usernt found with user: " + user)
          );
        }
      );
  }

  public Mono<Boolean> validateCliente(String usuario, String password) {
    return findByUsername(usuario)
      .flatMap(userDetails -> {
        boolean passwordMatches = password.equals(userDetails.getPassword());
        boolean usernameMatches = usuario.equals(userDetails.getUsername());
        return Mono.just(passwordMatches && usernameMatches);
      })
      .onErrorResume(UsernameNotFoundException.class, e -> Mono.just(false));
  }

  public Mono<Cliente> findUserByEmail(String email) {
    return clienteRepository.findByEmail(email);
  }
}
