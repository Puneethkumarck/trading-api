package com.xchange.valr.trading.api.domain.tradehistory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TradeHistoryQueryHandler {

  private final TradeHistoryRepository tradeHistoryRepository;

  public Optional<List<Trade>> getTradeHistory(String currencyPair, int limit) {
    log.info("Retrieving trade history for currency pair: {} with limit: {}", currencyPair, limit);
    return tradeHistoryRepository.findRecentTradesByCurrencyPair(currencyPair, limit);
  }
}
