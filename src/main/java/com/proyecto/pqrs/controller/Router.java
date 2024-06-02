package com.proyecto.pqrs.controller;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class Router {

  @Bean
  public RouterFunction<ServerResponse> routerFunction(
    SecurityHandler securityHandler
  ) {
    RouterFunction<ServerResponse> securityRoutes = RouterFunctions
      .route(POST("/api/secure/login"), securityHandler::createToken)
      .andRoute(
        GET("/api/secure/validateSecurity"),
        securityHandler::testEndpoint
      );

    return RouterFunctions.route().add(securityRoutes).build();
  }
}
