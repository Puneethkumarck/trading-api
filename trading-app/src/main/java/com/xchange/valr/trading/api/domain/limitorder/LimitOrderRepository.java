package com.xchange.valr.trading.api.domain.limitorder;

import java.util.Optional;

public interface LimitOrderRepository {
  LimitOrderCommand save(LimitOrderCommand order);

  Optional<LimitOrderCommand> findByOrderId(String orderId);
}
