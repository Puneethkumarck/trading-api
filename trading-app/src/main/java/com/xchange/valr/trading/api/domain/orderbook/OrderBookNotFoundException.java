package com.xchange.valr.trading.api.domain.orderbook;

public class OrderBookNotFoundException extends RuntimeException {
  public OrderBookNotFoundException(String message) {
    super(message);
  }

  public static OrderBookNotFoundException withCurrencyPair(String currencyPair) {
    return new OrderBookNotFoundException(
      "Order book not found for currency pair: %s".formatted(currencyPair)
    );
  }
}
