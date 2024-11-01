package com.xchange.valr.trading.api.domain.limitorder;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Builder(toBuilder = true)
public record LimitOrderCommand(
  LimitOrder limitOrder,
  String customerOrderId
) {
  @Builder(toBuilder = true)
  public record LimitOrder(
    OrderBookSide side,
    BigDecimal quantity,
    BigDecimal price,
    String currencyPair,
    OrderStatus status
  ) {

    @RequiredArgsConstructor
    @Getter
    public enum OrderBookSide {
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

  public String currencyPair() {
    return limitOrder.currencyPair();
  }
}
