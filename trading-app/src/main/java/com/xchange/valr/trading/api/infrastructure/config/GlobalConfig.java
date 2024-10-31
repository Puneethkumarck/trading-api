package com.xchange.valr.trading.api.infrastructure.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@Configuration
public class GlobalConfig {
  @Bean
  @Order(HIGHEST_PRECEDENCE)
  public Jackson2ObjectMapperBuilderCustomizer objectMapperBuilderCustomizer() {
    return builder -> builder.modules(new JavaTimeModule()).featuresToDisable(FAIL_ON_UNKNOWN_PROPERTIES).build();
  }
}
