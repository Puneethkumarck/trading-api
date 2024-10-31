package com.xchange.valr.trading.fixtures;

import com.xchange.valr.trading.api.infrastructure.orderbook.OrderBookEntity;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

import static com.xchange.valr.trading.api.infrastructure.orderbook.OrderBookEntity.OderBookSide.BUY;
import static com.xchange.valr.trading.api.infrastructure.orderbook.OrderBookEntity.OderBookSide.SELL;
import static java.time.Instant.now;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class OrderBookEntityFixtures {
  public static OrderBookEntity createOrderBookEntity(String currencyPair) {
    return OrderBookEntity.builder()
      .currencyPair(currencyPair)
      .asks(
        new TreeMap<>(
          Map.of(
            new BigDecimal("1000000"),
            OrderBookEntity.OrderBookLevelEntity.builder()
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
            OrderBookEntity.OrderBookLevelEntity.builder()
              .side(BUY)
              .price(new BigDecimal("900000"))
              .quantity(new BigDecimal("0.1"))
              .currencyPair(currencyPair)
              .orderCount(1)
              .build()
          )
        )
      )
      .sequenceNumber(1L)
      .lastChange(now())
      .build();
  }
}
