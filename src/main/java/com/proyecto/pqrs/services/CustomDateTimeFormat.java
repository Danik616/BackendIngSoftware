package com.proyecto.pqrs.services;

import java.time.format.DateTimeFormatter;

public class CustomDateTimeFormat {

  public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(
    "yyyy-MM-dd'T'HH:mm:ss"
  );
}
