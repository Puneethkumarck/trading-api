package com.xchange.valr.trading.api.domain.orderbook;

import java.util.Optional;

public interface OrderBookRepository {
  Optional<OrderBook> findByCurrencyPair(String currencyPair);

  Optional<OrderBook> save(OrderBook orderBook);
}
