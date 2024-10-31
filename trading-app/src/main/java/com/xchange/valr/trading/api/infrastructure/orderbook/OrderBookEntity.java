package com.xchange.valr.trading.api.infrastructure.orderbook;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.TreeMap;

@Builder(toBuilder = true)
public record OrderBookEntity(
  String currencyPair,
  TreeMap<BigDecimal, OrderBookLevelEntity> asks,
  TreeMap<BigDecimal, OrderBookLevelEntity> bids,
  long sequenceNumber,
  Instant lastChange
) {
  @Builder(toBuilder = true)
  public record OrderBookLevelEntity(
    OderBookSide side,
    BigDecimal price,
    BigDecimal quantity,
    String currencyPair,
    int orderCount
  ) {
  }

  @Getter
  @RequiredArgsConstructor
  public enum OderBookSide {
    SELL("sell"),
    BUY("buy");

    private final String side;
  }
}
