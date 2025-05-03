package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateValidationUtil {

  public static boolean validate(String dateFormat, String inputDate) {
    try {
      LocalDate.parse(inputDate, DateTimeFormatter.ofPattern(dateFormat));
      return true;
    } catch (Exception exception) {
      return false;
    }
  }
}