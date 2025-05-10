package com.ebsolutions.papertrail.financialdataingestionservice.authentication;

import com.ebsolutions.papertrail.financialdataingestionservice.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
@Slf4j
public class AuthenticationService {
  private final ClientCredentials clientCredentials;

  public String getAccessToken(String code) throws JsonProcessingException {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> params = getParams(code);

    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, httpHeaders);

    RestTemplate restTemplate = new RestTemplate();
    String response =
        restTemplate.postForObject(clientCredentials.getProviderUrl(), requestEntity, String.class);

    JsonNode jsonNode = new ObjectMapper().readTree(response);

    return jsonNode.get("access_token").asText();
  }

  public User getUserDetails(String accessToken) throws JsonProcessingException {
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.setBearerAuth(accessToken);

    HttpEntity<String> requestEntity = new HttpEntity<>(httpHeaders);

    ResponseEntity<String>
        response =
        restTemplate.exchange(clientCredentials.getAuthenticatedUserInfoUrl(),
            HttpMethod.GET,
            requestEntity, String.class);

    try {
      JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
      System.out.println(jsonNode);

      return User.builder()
          .accessToken(accessToken)
          .email(jsonNode.get("email").asText())
          .lastName(jsonNode.get("family_name").asText())
          .firstName(jsonNode.get("given_name").asText())
          .picture(jsonNode.get("picture").asText())
          .build();
    } catch (Exception exception) {
      log.info(exception.getMessage());
      throw exception;
    }
  }

  private MultiValueMap<String, String> getParams(String code) {
    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("code", code);
    params.add("redirect_uri", "http://localhost:8080/auth/grant-code");
    params.add("client_id", clientCredentials.getClientId());
    params.add("client_secret", clientCredentials.getClientSecret());
    params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.profile");
    params.add("scope", "https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email");
    params.add("scope", "openid");
    params.add("grant_type", "authorization_code");
    return params;
  }
}
