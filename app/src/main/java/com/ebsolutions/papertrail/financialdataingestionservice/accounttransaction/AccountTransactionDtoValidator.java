package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction;

import com.ebsolutions.papertrail.financialdataingestionservice.common.ErrorMessageEnvelope;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

@Component
public class AccountTransactionDtoValidator {
  public List<ErrorMessageEnvelope> validate(
      AccountTransactionDto accountTransactionDto) {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      Validator validator = factory.getValidator();
      Set<ConstraintViolation<AccountTransactionDto>> violations =
          validator.validate(accountTransactionDto);

      if (CollectionUtils.isNotEmpty(violations)) {
        return Collections.emptyList();
      }

      return Collections.emptyList();
    }
  }
}
