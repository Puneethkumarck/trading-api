package com.xchange.valr.trading.fixtures;

import com.xchange.valr.trading.api.domain.orderbook.OrderBook;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

import static com.xchange.valr.trading.api.domain.orderbook.OrderBook.OrderBookSide.BUY;
import static com.xchange.valr.trading.api.domain.orderbook.OrderBook.OrderBookSide.SELL;
import static java.time.Instant.now;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class OrderBookFixtures {
  public static OrderBook orderBook(String currencyPair) {
    return OrderBook.builder()
      .currencyPair(currencyPair)
      .asks(
        new TreeMap<>(
          Map.of(
            new BigDecimal("1000000"),
            OrderBook.OrderBookLevel.builder()
              .side(SELL)
              .price(new BigDecimal("1000000"))
              .quantity(new BigDecimal("0.1"))
              .currencyPair(currencyPair)
              .orderCount(1)
              .build()
          )
        )
      )
      .bids(
        new TreeMap<>(
          Map.of(
            new BigDecimal("900000"),
            OrderBook.OrderBookLevel.builder()
              .side(BUY)
              .price(new BigDecimal("900000"))
              .quantity(new BigDecimal("0.1"))
              .currencyPair(currencyPair)
              .orderCount(1)
              .build()
          )
        )
      )
      .lastChange(now())
      .sequenceNumber(1L)
      .build();
  }
}
