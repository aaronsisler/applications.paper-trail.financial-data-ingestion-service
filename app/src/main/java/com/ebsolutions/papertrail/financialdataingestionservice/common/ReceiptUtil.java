package com.ebsolutions.papertrail.financialdataingestionservice.common;

import java.util.UUID;

public class ReceiptUtil {
  public static String createReceipt() {
    return UUID.randomUUID().toString();
  }
}
