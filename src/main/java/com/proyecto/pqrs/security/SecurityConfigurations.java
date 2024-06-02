package com.proyecto.pqrs.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.proyecto.pqrs.services.CustomDateTimeFormat;
import com.proyecto.pqrs.services.JwtUtil;
import java.time.LocalDateTime;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableWebFlux
public class SecurityConfigurations {

  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());

    // Configurar el formato personalizado para las fechas
    SimpleModule module = new SimpleModule();
    module.addSerializer(
      LocalDateTime.class,
      new LocalDateTimeSerializer(CustomDateTimeFormat.FORMATTER)
    );
    mapper.registerModule(module);

    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    return mapper;
  }

  @Bean
  public WebProperties.Resources resources() {
    return new WebProperties.Resources();
  }

  @Bean
  public ReactiveAuthenticationManager jwtAuthenticationManager(
    JwtUtil jwtUtil
  ) {
    return authentication ->
      Mono
        .justOrEmpty(authentication)
        .filter(auth -> auth.getCredentials() != null)
        .flatMap(auth ->
          jwtUtil
            .validateToken(auth.getCredentials().toString())
            .filter(Boolean::booleanValue)
            .flatMap(valid ->
              jwtUtil.getAuthentication(auth.getCredentials().toString())
            )
        )
        .switchIfEmpty(
          Mono.defer(() ->
            Mono.error(new BadCredentialsException("Invalid JWT Token"))
          )
        );
  }

  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter(
    JwtUtil jwtUtil
  ) {
    return new JwtAuthenticationConverter(jwtUtil);
  }

  @Bean
  public ServerSecurityContextRepository serverSecurityContextRepository(
    ReactiveAuthenticationManager jwtAuthenticationManager
  ) {
    return new ServerSecurityContextRepository() {
      @Override
      public Mono<Void> save(
        ServerWebExchange exchange,
        SecurityContext context
      ) {
        // Implementación para guardar el contexto de seguridad, si es necesario
        return Mono.empty();
      }

      @Override
      public Mono<SecurityContext> load(ServerWebExchange exchange) {
        // Obtén el token JWT de la solicitud
        String token = exchange
          .getRequest()
          .getHeaders()
          .getFirst(HttpHeaders.AUTHORIZATION);
        if (token != null && token.startsWith("Bearer ")) {
          token = token.substring(7);
        }

        // Crea un objeto de autenticación con el token JWT
        Authentication auth = new UsernamePasswordAuthenticationToken(
          token,
          token
        );

        // Utiliza el ReactiveAuthenticationManager para autenticar y cargar el contexto
        // de seguridad
        return jwtAuthenticationManager
          .authenticate(auth)
          .map(authentication ->
            (SecurityContext) new SecurityContextImpl(authentication)
          )
          .onErrorResume(e -> Mono.empty());
      }
    };
  }

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(
    ServerHttpSecurity http,
    ReactiveAuthenticationManager jwtAuthenticationManager,
    ServerSecurityContextRepository serverSecurityContextRepository,
    JwtAuthenticationConverter jwtAuthenticationConverter
  ) {
    http
      .authorizeExchange(exchange ->
        exchange
          .pathMatchers("/api/secure/login")
          .permitAll()
          .anyExchange()
          .authenticated()
      )
      .authenticationManager(jwtAuthenticationManager)
      .securityContextRepository(serverSecurityContextRepository)
      .exceptionHandling(handling ->
        handling
          .authenticationEntryPoint((exchange, e) ->
            Mono.fromRunnable(() ->
              exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED)
            )
          )
          .accessDeniedHandler((exchange, e) ->
            Mono.fromRunnable(() ->
              exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN)
            )
          )
      )
      .httpBasic(Customizer.withDefaults())
      .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
      .csrf(ServerHttpSecurity.CsrfSpec::disable)
      .cors(ServerHttpSecurity.CorsSpec::disable);

    return http.build();
  }
}
