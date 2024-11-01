package com.xchange.valr.trading.fixtures;

import com.xchange.valr.trading.api.infrastructure.tradehistory.TradeEntity;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

import static java.time.Instant.now;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class TradeEntityFixtures {

  public static TradeEntity createTradeEntity(String currencyPair, String takerSide) {
    return TradeEntity.builder()
      .id(UUID.randomUUID().toString())
      .currencyPair(currencyPair)
      .takerSide(takerSide)
      .price(new BigDecimal("1000000"))
      .quantity(new BigDecimal("1.0"))
      .quoteVolume(new BigDecimal("1000000"))
      .tradedAt(now())
      .build();
  }

  public static TradeEntity createTradeEntity(
    String currencyPair,
    String takerSide,
    BigDecimal quantity,
    BigDecimal price
  ) {
    return TradeEntity.builder()
      .id(UUID.randomUUID().toString())
      .currencyPair(currencyPair)
      .takerSide(takerSide)
      .quantity(quantity)
      .price(price)
      .quoteVolume(price.multiply(quantity))
      .tradedAt(now())
      .build();
  }
}
