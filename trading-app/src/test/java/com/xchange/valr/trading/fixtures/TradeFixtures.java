package com.xchange.valr.trading.fixtures;

import com.xchange.valr.trading.api.domain.tradehistory.Trade;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

import static java.time.Instant.now;
import static java.util.UUID.randomUUID;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public final class TradeFixtures {

  public static Trade createTrade(String currencyPair, Trade.TakerSide takerSide) {
    return Trade.builder()
      .id(randomUUID().toString())
      .currencyPair(currencyPair)
      .takerSide(takerSide.name())
      .quantity(BigDecimal.ONE)
      .price(new BigDecimal("1000000"))
      .quoteVolume(new BigDecimal("1000000"))
      .tradedAt(now())
      .sequenceId(1300890000000000001L)
      .build();
  }
}
