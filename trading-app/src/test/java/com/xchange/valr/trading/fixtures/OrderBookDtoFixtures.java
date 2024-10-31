package com.xchange.valr.trading.fixtures;

import static java.time.Instant.now;
import static lombok.AccessLevel.PRIVATE;

import com.xchange.valr.api.model.OrderBookResponseDto;
import java.util.List;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = PRIVATE)
public final class OrderBookDtoFixtures {
  public static OrderBookResponseDto orderBookDto(String currencyPair) {
    return OrderBookResponseDto.builder()
      .asks(
        List.of(
          OrderBookResponseDto.OrderBookEntryDto.builder()
            .side("SELL")
            .price("1000000")
            .quantity("0.1")
            .currencyPair(currencyPair)
            .orderCount(1)
            .build()
        )
      )
      .bids(
        List.of(
          OrderBookResponseDto.OrderBookEntryDto.builder()
            .side("BUY")
            .price("900000")
            .quantity("0.1")
            .currencyPair(currencyPair)
            .orderCount(1)
            .build()
        )
      )
      .lastChange(now())
      .sequenceNumber(1L)
      .build();
  }
}
