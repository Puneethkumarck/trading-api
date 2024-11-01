package com.xchange.valr.trading.api.infrastructure.tradehistory;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.Instant;

@Builder(toBuilder = true)
public record TradeEntity(
  String id,
  String currencyPair,
  String takerSide,
  BigDecimal quantity,
  BigDecimal price,
  BigDecimal quoteVolume,
  Instant tradedAt,
  Long sequenceId
) {
}
