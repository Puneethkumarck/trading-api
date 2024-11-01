package com.xchange.valr.trading.api.domain.tradehistory;

import java.util.List;
import java.util.Optional;

public interface TradeHistoryRepository {
  Optional<Trade> save(Trade trade);

  Optional<List<Trade>> findRecentTradesByCurrencyPair(String currencyPair, int limit);

  Optional<Trade> findTradesByOrderId(String orderId);
}
