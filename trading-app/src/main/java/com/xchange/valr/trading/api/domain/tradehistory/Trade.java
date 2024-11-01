package com.xchange.valr.trading.api.domain.tradehistory;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Builder(toBuilder = true)
public record Trade(
  String id,
  String currencyPair,
  String takerSide,
  BigDecimal quantity,
  BigDecimal price,
  BigDecimal quoteVolume,
  Instant tradedAt,
  Long sequenceId
) {

  @Getter
  @RequiredArgsConstructor
  public enum TakerSide {
    BUY("buy"),
    SELL("sell");

    private final String side;
  }
}
