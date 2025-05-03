package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AccountTransactionFileEnvelope {
  @NonNull
  @Min(value = 1, message = "Account id must be greater than zero")
  private Integer accountId;
  private SupportedInstitution supportedInstitution;
  private MultipartFile file;
}
