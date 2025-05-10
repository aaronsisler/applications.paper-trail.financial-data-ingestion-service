package com.ebsolutions.papertrail.financialdataingestionservice.authentication;

import com.ebsolutions.papertrail.financialdataingestionservice.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@AllArgsConstructor
@RequestMapping("auth")
@Slf4j
public class AuthenticationController {
  private final ClientCredentials clientCredentials;
  private final AuthenticationService authenticationService;

  @GetMapping(value = "/grant-code")
  public ResponseEntity<User> grantCode(@RequestParam("code") String code)
      throws JsonProcessingException {

    String accessToken = authenticationService.getAccessToken(code);
    User user = authenticationService.getUserDetails(accessToken);

    return ResponseEntity.ok(user);
  }
}
