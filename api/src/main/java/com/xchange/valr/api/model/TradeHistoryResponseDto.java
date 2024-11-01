package com.xchange.valr.api.model;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.Instant;

@Builder
@Jacksonized
public record TradeHistoryResponseDto(
  BigDecimal price,
  BigDecimal quantity,
  String currencyPair,
  Instant tradedAt,
  String takerSide,
  Long sequenceId,
  String id,
  BigDecimal quoteVolume
) {
}
