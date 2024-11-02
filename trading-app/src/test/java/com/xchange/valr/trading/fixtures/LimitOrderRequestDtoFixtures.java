package com.xchange.valr.trading.fixtures;

import com.xchange.valr.api.model.LimitOrderRequestDto;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static com.xchange.valr.trading.api.domain.model.CurrencyPair.BTCZAR;
import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class LimitOrderRequestDtoFixtures {

  public static LimitOrderRequestDto limitOrderRequestDto() {
    return LimitOrderRequestDto.builder()
      .pair(BTCZAR.name())
      .side("SELL")
      .quantity(BigDecimal.valueOf(0.1))
      .price(BigDecimal.valueOf(1000000))
      .customerOrderId("123")
      .build();
  }
}
