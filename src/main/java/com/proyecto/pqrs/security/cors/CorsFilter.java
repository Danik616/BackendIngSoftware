package com.proyecto.pqrs.security.cors;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.reactive.CorsUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class CorsFilter implements WebFilter {

  private static final String ALLOWED_HEADERS =
    "Access-Control-Allow-Headers, " +
    "Origin, Accept, X-Requested-With, Content-Type, Authorization, " +
    "Access-Control-Request-Method, " +
    "Access-Control-Request-Headers, xsrf-token, configUrl";
  private static final String ALLOWED_METHODS = "GET, POST, OPTIONS";
  private static final String ALLOWED_ORIGIN = "*";
  private static final String MAX_AGE = "3600";

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    if (CorsUtils.isCorsRequest(exchange.getRequest())) {
      exchange
        .getResponse()
        .getHeaders()
        .add("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
      exchange
        .getResponse()
        .getHeaders()
        .add("Access-Control-Allow-Methods", ALLOWED_METHODS);
      exchange
        .getResponse()
        .getHeaders()
        .add("Access-Control-Max-Age", MAX_AGE);
      exchange
        .getResponse()
        .getHeaders()
        .add("Access-Control-Allow-Headers", ALLOWED_HEADERS);

      if (exchange.getRequest().getMethod() == HttpMethod.OPTIONS) {
        exchange.getResponse().setStatusCode(HttpStatus.OK);
        return Mono.empty();
      }
    }

    return chain.filter(exchange);
  }
}
