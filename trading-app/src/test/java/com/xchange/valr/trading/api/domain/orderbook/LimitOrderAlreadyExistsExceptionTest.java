package com.xchange.valr.trading.api.domain.orderbook;

import org.junit.jupiter.api.Test;

import static com.xchange.valr.trading.api.domain.orderbook.LimitOrderAlreadyExistsException.withOrderId;
import static org.assertj.core.api.Assertions.assertThat;

class LimitOrderAlreadyExistsExceptionTest {

  @Test
  void testWithOrderId() {
    // when
    var exception = withOrderId("orderId");

    // then
    assertThat(exception.getMessage())
      .isEqualTo("Limit order with orderId orderId already exists");
  }
}