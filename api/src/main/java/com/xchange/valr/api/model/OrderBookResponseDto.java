package com.xchange.valr.api.model;

import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder(toBuilder = true)
@Jacksonized
public record OrderBookResponseDto(
  List<OrderBookEntryDto> asks,
  List<OrderBookEntryDto> bids,
  Instant lastChange,
  Long sequenceNumber
) {
  @Builder(toBuilder = true)
  @Jacksonized
  public record OrderBookEntryDto(
    String side,
    String quantity,
    String price,
    String currencyPair,
    int orderCount
  ) {
  }
}
