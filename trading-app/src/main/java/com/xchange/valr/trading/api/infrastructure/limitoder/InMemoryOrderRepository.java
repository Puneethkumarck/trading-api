package com.xchange.valr.trading.api.infrastructure.limitoder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Slf4j
@RequiredArgsConstructor
public class InMemoryOrderRepository {
  private final Map<String, LimitOrderEntity> limitOrders = new ConcurrentHashMap<>();

  public Optional<LimitOrderEntity> save(LimitOrderEntity order) {
    limitOrders.put(order.orderId(), order);
    return Optional.of(order);
  }

  public Optional<LimitOrderEntity> findByOrderId(String orderId) {
    return Optional.ofNullable(limitOrders.get(orderId));
  }
}
