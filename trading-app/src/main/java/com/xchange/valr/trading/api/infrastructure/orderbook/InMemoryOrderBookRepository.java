package com.xchange.valr.trading.api.infrastructure.orderbook;

import static java.time.Instant.now;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class InMemoryOrderBookRepository {
  private final Map<String, OrderBookEntity> orderBooks = new ConcurrentHashMap<>();
  private final AtomicLong sequenceNumber = new AtomicLong(0);

  public Optional<OrderBookEntity> findByCurrencyPair(String currencyPair) {
    return Optional.ofNullable(orderBooks.get(currencyPair));
  }

  public Optional<OrderBookEntity> save(OrderBookEntity orderBookEntity) {
    OrderBookEntity updatedEntity = orderBookEntity
      .toBuilder()
      .sequenceNumber(sequenceNumber.incrementAndGet())
      .lastChange(now())
      .build();
    orderBooks.put(orderBookEntity.currencyPair(), updatedEntity);
    return Optional.ofNullable(updatedEntity);
  }
}
