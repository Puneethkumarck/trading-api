package com.xchange.valr.trading.api.domain.limitorder;

import java.math.BigDecimal;

public interface OrderExecutor {
  void executeTrade(LimitOrderCommand command, BigDecimal matchPrice, BigDecimal matchedQuantity);
}
