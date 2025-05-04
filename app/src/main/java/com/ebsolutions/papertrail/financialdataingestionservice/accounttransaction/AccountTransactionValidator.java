package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction;

import com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction.dto.AccountTransactionDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AccountTransactionValidator {
  public List<ErrorMessageEnvelope> validate(AccountTransactionDto accountTransactionDto) {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      List<ErrorMessageEnvelope> errorMessageEnvelopes = new ArrayList<>();

      Validator validator = factory.getValidator();
      Set<ConstraintViolation<AccountTransactionDto>> violations =
          validator.validate(accountTransactionDto);

      if (CollectionUtils.isNotEmpty(violations)) {
        errorMessageEnvelopes.addAll(violations.stream()
            .map(violation ->
                ErrorMessageEnvelope.builder()
                    .rowId(accountTransactionDto.getRowId())
                    .errorMessage(violation.getMessage())
                    .build())
            .toList());
      }

      // Check if amount is a valid integer
      if (!validateAmount(accountTransactionDto.getAmount())) {
        errorMessageEnvelopes.add(ErrorMessageEnvelope.builder()
            .rowId(accountTransactionDto.getRowId())
            .errorMessage("Amount is not a valid integer")
            .build());
      }

      // Check if date is the correct format
      if (!DateValidationUtil.validate(accountTransactionDto.getDateFormat(),
          accountTransactionDto.getTransactionDate())) {
        errorMessageEnvelopes.add(ErrorMessageEnvelope.builder()
            .rowId(accountTransactionDto.getRowId())
            .errorMessage("Transaction Date is not a valid format YYYY-MM-DD")
            .build());
      }

      // If any errors are present, return the errors
      if (!errorMessageEnvelopes.isEmpty()) {
        return errorMessageEnvelopes;
      }

      return Collections.emptyList();
    }
  }

  private boolean validateAmount(String inputAmount) {
    if (StringUtils.isBlank(inputAmount)) {
      return false;
    }

    try {
      Double.parseDouble(inputAmount);
      return true;
    } catch (NumberFormatException numberFormatException) {
      return false;
    }
  }
}