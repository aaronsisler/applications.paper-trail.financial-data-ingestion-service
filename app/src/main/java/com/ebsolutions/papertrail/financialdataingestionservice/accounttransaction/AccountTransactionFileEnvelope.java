package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
public class AccountTransactionFileEnvelope {
  private SupportedInstitution supportedInstitution;
  private MultipartFile file;
}
