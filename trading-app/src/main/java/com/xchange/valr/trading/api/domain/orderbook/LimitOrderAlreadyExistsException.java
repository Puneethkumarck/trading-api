package com.xchange.valr.trading.api.domain.orderbook;

public class LimitOrderAlreadyExistsException extends RuntimeException {
  public LimitOrderAlreadyExistsException(String message) {
    super(message);
  }

  public static LimitOrderAlreadyExistsException withOrderId(String orderId) {
    return new LimitOrderAlreadyExistsException(
      "Limit order with orderId %s already exists".formatted(orderId)
    );
  }
}
