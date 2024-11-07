package com.xchange.valr.trading.api.infrastructure.tradehistory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
@Slf4j
@RequiredArgsConstructor
public class InMemoryTradeRepository {
  private final Map<String, CopyOnWriteArrayList<TradeEntity>> trades = new ConcurrentHashMap<>();
  private final AtomicLong sequenceGenerator = new AtomicLong(1300890000000000000L);

  public Optional<TradeEntity> save(TradeEntity trade) {
    log.info("Saving trade: {}", trade);

    var updatedTrade =
      trade.toBuilder()
        .sequenceId(sequenceGenerator.incrementAndGet())
        .tradedAt(Instant.now())
        .build();

    trades
      .computeIfAbsent(trade.currencyPair().toUpperCase(), key -> new CopyOnWriteArrayList<>())
      .add(updatedTrade);

    log.debug("Saved trade: {} for currency pair: {}", trade.id(), trade.currencyPair());
    return Optional.of(updatedTrade);
  }

  public Optional<List<TradeEntity>> findRecentTradeByCurrencyPair(String currencyPair, int limit) {
    return Optional.of(
      trades.getOrDefault(currencyPair.toUpperCase(), new CopyOnWriteArrayList<>())
        .stream()
        .sorted(Comparator.comparing(TradeEntity::tradedAt).reversed())
        .limit(limit)
        .collect(Collectors.toList())
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
