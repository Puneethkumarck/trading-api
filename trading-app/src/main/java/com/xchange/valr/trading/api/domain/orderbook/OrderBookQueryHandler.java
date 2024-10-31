package com.xchange.valr.trading.api.domain.orderbook;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderBookQueryHandler {

  private final OrderBookRepository orderBookRepository;

  public OrderBook getOrderBook(String currencyPair) {
    log.info("Getting order book for currency pair: {}", currencyPair);
    return orderBookRepository
      .findByCurrencyPair(currencyPair)
      .orElseThrow(() -> OrderBookNotFoundException.withCurrencyPair(currencyPair));
  }
}
