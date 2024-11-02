package com.xchange.valr.trading.api.domain.limitorder;

import com.xchange.valr.trading.api.domain.orderbook.OrderBook;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static com.xchange.valr.trading.api.domain.model.CurrencyPair.BTCZAR;
import static org.assertj.core.api.Assertions.assertThat;

abstract class OrderProcessorBaseTest {
  protected abstract OrderProcessor getProcessor();

  protected abstract OrderBook.OrderBookSide getExpectedSide();

  @Test
  void shouldReturnCorrectOrderSide() {
    assertThat(getProcessor().getOrderSide())
      .isEqualTo(getExpectedSide());
  }

  protected OrderBook createOrderBook() {
    return OrderBook.builder()
      .currencyPair(BTCZAR.name())
      .asks(new ConcurrentSkipListMap<>())
      .bids(new ConcurrentSkipListMap<>())
      .build();
  }

  protected OrderBook.OrderBookLevel createOrderBookLevel(BigDecimal price, BigDecimal quantity, int orderCount) {
    return OrderBook.OrderBookLevel.builder()
      .side(getExpectedSide())
      .price(price)
      .quantity(quantity)
      .currencyPair(BTCZAR.name())
      .orderCount(orderCount)
      .build();
  }
}
