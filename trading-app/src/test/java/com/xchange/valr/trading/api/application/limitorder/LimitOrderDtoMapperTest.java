package com.xchange.valr.trading.api.application.limitorder;

import com.xchange.valr.api.model.LimitOrderRequestDto;
import com.xchange.valr.trading.api.domain.limitorder.LimitOrderCommand;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.xchange.valr.trading.api.domain.model.CurrencyPair.BTCZAR;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.assertj.core.api.Assertions.assertThat;

class LimitOrderDtoMapperTest {
  private static final LimitOrderDtoMapper mapper = new LimitOrderDtoMapperImpl();

  @Test
  void toCommand_When_Buy_Order() {
    // given
    var customerOrderId = randomAlphanumeric(50);
    var request =
      LimitOrderRequestDto.builder()
        .pair(BTCZAR.name())
        .side("BUY")
        .quantity(BigDecimal.valueOf(1))
        .price(BigDecimal.valueOf(100000))
        .customerOrderId(customerOrderId)
        .build();

    // when
    var result = mapper.toCommand(request);

    // then
    var expectedLimitOrder =
      LimitOrderCommand.LimitOrder.builder()
        .side(LimitOrderCommand.LimitOrder.OrderBookSide.BUY)
        .quantity(BigDecimal.valueOf(1))
        .price(BigDecimal.valueOf(100000))
        .currencyPair(BTCZAR.name())
        .build();

    var expected =
      LimitOrderCommand.builder()
        .limitOrder(expectedLimitOrder)
        .customerOrderId(customerOrderId)
        .build();

    assertThat(result)
      .usingRecursiveComparison()
      .ignoringFields("lastChange")
      .isEqualTo(expected);
  }

  @Test
  void toCommand_When_Sell_Order() {
    // given
    var customerOrderId = randomAlphanumeric(50);
    var request =
      LimitOrderRequestDto.builder()
        .pair(BTCZAR.name())
        .side("SELL")
        .quantity(BigDecimal.valueOf(1))
        .price(BigDecimal.valueOf(100000))
        .customerOrderId(customerOrderId)
        .build();

    // when
    var result = mapper.toCommand(request);

    // then
    var expectedLimitOrder =
      LimitOrderCommand.LimitOrder.builder()
        .side(LimitOrderCommand.LimitOrder.OrderBookSide.SELL)
        .quantity(BigDecimal.valueOf(1))
        .price(BigDecimal.valueOf(100000))
        .currencyPair(BTCZAR.name())
        .build();

    var expected =
      LimitOrderCommand.builder()
        .limitOrder(expectedLimitOrder)
        .customerOrderId(customerOrderId)
        .build();

    assertThat(result)
      .usingRecursiveComparison()
      .ignoringFields("lastChange")
      .isEqualTo(expected);
  }
}