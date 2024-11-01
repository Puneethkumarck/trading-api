package com.xchange.valr.trading.api.infrastructure.tradehistory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
@Slf4j
@RequiredArgsConstructor
public class InMemoryTradeRepository {
  private final Map<String, List<TradeEntity>> trades = new ConcurrentHashMap<>();
  private final AtomicLong sequenceGenerator = new AtomicLong(1300890000000000000L);

  public Optional<TradeEntity> save(TradeEntity trade) {
    log.info("Saving trade: {}", trade);

    var updatedTrade =
      trade.toBuilder()
        .sequenceId(sequenceGenerator.incrementAndGet())
        .tradedAt(Instant.now())
        .build();

    trades
      .computeIfAbsent(trade.currencyPair().toUpperCase(), key -> new ArrayList<>())
      .add(updatedTrade);

    log.debug("Saved trade: {} for currency pair: {}", trade.id(), trade.currencyPair());
    return Optional.of(updatedTrade);
  }

  public Optional<List<TradeEntity>> findRecentTradeByCurrencyPair(String currencyPair, int limit) {
    return Optional.of(
      trades.getOrDefault(currencyPair.toUpperCase(), new ArrayList<>())
        .stream()
        .sorted((a, b) -> b.tradedAt().compareTo(a.tradedAt()))
        .limit(limit)
        .toList()
    );
  }

  public Optional<TradeEntity> findTradesByOrderId(String orderId) {
    return trades.values()
      .stream()
      .flatMap(List::stream)
      .filter(trade -> trade.id().equals(orderId))
      .findFirst();
  }
}
