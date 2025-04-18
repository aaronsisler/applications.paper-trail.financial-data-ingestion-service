package com.ebsolutions.papertrail.financialdataingestionservice.accounttransaction;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateValidationUtil {
  public static final String DATE_FORMAT = "yyyy-MM-dd";
  public static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern(DATE_FORMAT);

  public static boolean validate(String inputDate) {
    try {
      LocalDate.parse(inputDate, DATE_TIME_FORMATTER);
      return true;
    } catch (Exception exception) {
      return false;
    }
  }
}