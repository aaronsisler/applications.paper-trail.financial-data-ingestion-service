package com.ebsolutions.papertrail.financialdataingestionservice.config;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Component
public class CorsConfig implements CorsConfigurationSource {
  private static final List<String> ALLOWED_ORIGINS =
      List.of(
          "http://localhost:3000",
          "https://paper-trail.otterandcow.com",
          // House
          "64.98.122.48",
          // Gym
          "96.10.245.210",
          // Tavern
          "107.12.84.23"
      );

  @Override
  public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedOrigins(ALLOWED_ORIGINS);
    return configuration;
  }
}