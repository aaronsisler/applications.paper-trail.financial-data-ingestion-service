package com.ebsolutions.papertrail.financialdataingestionservice.common;

import java.util.List;

public interface EventPublisher {

  void publish(List<?> event);

  String process(Object event);
}
