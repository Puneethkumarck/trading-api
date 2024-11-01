package com.xchange.valr.trading.fixtures;

import com.xchange.valr.trading.api.domain.limitorder.LimitOrderCommand;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static com.xchange.valr.trading.api.domain.model.CurrencyPair.BTCZAR;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@NoArgsConstructor(access = PRIVATE)
public final class LimitOrderCommandFixtures {
  public static LimitOrderCommand createLimitOrder() {
    return LimitOrderCommand.builder()
      .limitOrder(
        LimitOrderCommand.LimitOrder.builder()
          .side(LimitOrderCommand.LimitOrder.OrderBookSide.BUY)
          .quantity(BigDecimal.valueOf(1))
          .price(BigDecimal.valueOf(900000))
          .currencyPair(BTCZAR.name())
          .status(LimitOrderCommand.LimitOrder.OrderStatus.OPEN)
          .build()
      )
      .customerOrderId(randomAlphanumeric(50))
      .build();
  }

  public static LimitOrderCommand createLimitOrder(
    BigDecimal price,
    BigDecimal quantity,
    String currencyPair,
    LimitOrderCommand.LimitOrder.OrderBookSide side
  ) {
    return LimitOrderCommand.builder()
      .limitOrder(
        LimitOrderCommand.LimitOrder.builder()
          .side(side)
          .quantity(quantity)
          .price(price)
          .currencyPair(currencyPair)
          .status(LimitOrderCommand.LimitOrder.OrderStatus.OPEN)
          .build()
      )
      .customerOrderId(randomAlphanumeric(50))
      .build();
  }
}
