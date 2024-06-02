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
    SecurityHandler securityHandler,
    PqrsHandler pqrsHanlder
  ) {
    RouterFunction<ServerResponse> securityRoutes = RouterFunctions
      .route(POST("/api/secure/login"), securityHandler::createToken)
      .andRoute(
        GET("/api/secure/validateSecurity"),
        securityHandler::testEndpoint
      );

    RouterFunction<ServerResponse> pqrHandler = RouterFunctions.route(
      POST("/api/savePQRS"),
      pqrsHanlder::guardarPQR
    );

    return RouterFunctions.route().add(securityRoutes).add(pqrHandler).build();
  }
}
