package com.xchange.valr.trading.api.infrastructure.limitoder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

public record LimitOrderEntity(
  String orderId,
  String customerOrderId,
  String currencyPair,
  OrderSide side,
  BigDecimal quantity,
  BigDecimal price,
  OrderStatus status,
  Instant createdAt
) {
  @RequiredArgsConstructor
  @Getter
  public enum OrderSide {
    SELL("SELL"),
    BUY("BUY");

    private final String side;
  }

  @RequiredArgsConstructor
  @Getter
  public enum OrderStatus {
    OPEN("OPEN"),
    FILLED("FILLED"),
    CANCELLED("CANCELLED");

    private final String status;
  }
}
