package com.ebsolutions.papertrail.financialdataingestionservice.config;

import com.ebsolutions.papertrail.financialdataingestionservice.authentication.ClientCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class AuthenticationConfig {
  @Value("${authentication.authenticated-user-info-url:`Authentication Authenticated User Info Url not found in environment`}")
  protected String authenticatedUserInfoUrl;
  @Value("${authentication.provider-url:`Authentication Provider Url not found in environment`}")
  protected String providerUrl;
  @Value("${authentication.client-id:`Authentication Client Id not found in environment`}")
  protected String clientId;
  @Value("${authentication.client-secret:`Authentication Client Secret not found in environment`}")
  protected String clientSecret;

  @Bean
  @Profile({"local", "dev", "default"})
  public ClientCredentials clientCredentials() {
    return ClientCredentials.builder()
        .providerUrl(providerUrl)
        .authenticatedUserInfoUrl(authenticatedUserInfoUrl)
        .clientId(clientId)
        .clientSecret(clientSecret)
        .build();
  }
}
