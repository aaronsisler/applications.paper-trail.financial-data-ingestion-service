package com.ebsolutions.papertrail.financialdataingestionservice.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientCredentials {
  private String providerUrl;

  private String authenticatedUserInfoUrl;

  private String clientId;

  private String clientSecret;
}
