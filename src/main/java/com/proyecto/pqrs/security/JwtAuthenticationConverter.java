package com.proyecto.pqrs.security;

import com.proyecto.pqrs.services.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class JwtAuthenticationConverter
  implements ServerAuthenticationConverter {

  private final JwtUtil jwtUtil;

  public JwtAuthenticationConverter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  public Mono<Authentication> convert(ServerWebExchange exchange) {
    return Mono
      .justOrEmpty(
        exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION)
      )
      .filter(authHeader -> authHeader.startsWith("Bearer "))
      .map(authHeader -> authHeader.substring(7))
      .flatMap(jwtUtil::getAuthentication);
  }
}
