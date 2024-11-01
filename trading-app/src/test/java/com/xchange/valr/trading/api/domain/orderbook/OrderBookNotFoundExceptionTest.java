package com.xchange.valr.trading.api.domain.orderbook;

import org.junit.jupiter.api.Test;

import static com.xchange.valr.trading.api.domain.model.CurrencyPair.BTCZAR;
import static org.assertj.core.api.Assertions.assertThat;

class OrderBookNotFoundExceptionTest {

  @Test
  void shouldThrowExpectedMessageWhenOrderBookNotFoundForGivenCurrencyPair() {
    // when
    var exception = OrderBookNotFoundException.withCurrencyPair(BTCZAR.name());

    // then
    assertThat(exception.getMessage())
      .isEqualTo("Order book not found for currency pair: BTCZAR");
  }
}