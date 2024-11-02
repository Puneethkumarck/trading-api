package com.xchange.valr.trading.api.domain.orderbook;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentNavigableMap;

@Slf4j
@Builder(toBuilder = true)
public record OrderBook(
  String currencyPair,
  ConcurrentNavigableMap<BigDecimal, OrderBookLevel> asks,
  ConcurrentNavigableMap<BigDecimal, OrderBookLevel> bids,
  Instant lastChange,
  Long sequenceNumber
) {

  @Builder(toBuilder = true)
  public record OrderBookLevel(
    OrderBookSide side,
    BigDecimal quantity,
    BigDecimal price,
    String currencyPair,
    int orderCount
  ) {
  }

  @RequiredArgsConstructor
  @Getter
  public enum OrderBookSide {
    SELL("SELL"),
    BUY("BUY");

    private final String side;
  }
}
