package com.xchange.valr.trading.api.infrastructure.limitoder;

import com.xchange.valr.trading.api.domain.limitorder.LimitOrderCommand;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static com.xchange.valr.trading.api.domain.model.CurrencyPair.BTCZAR;
import static java.time.Instant.now;
import static org.assertj.core.api.Assertions.assertThat;

class LimitOrderEntityMapperTest {
  private static final LimitOrderEntityMapper MAPPER = new LimitOrderEntityMapperImpl();

  @Test
  void toEntity_BUY() {
    // given
    var limitOrderCommand =
      LimitOrderCommand.builder()
        .limitOrder(
          new LimitOrderCommand.LimitOrder(
            LimitOrderCommand.LimitOrder.OrderBookSide.BUY,
            BigDecimal.valueOf(0.1),
            BigDecimal.valueOf(1000),
            BTCZAR.name(),
            LimitOrderCommand.LimitOrder.OrderStatus.OPEN
          )
        )
        .customerOrderId("123")
        .build();

    // when
    var result = MAPPER.toEntity(limitOrderCommand);

    // then
    var expected =
      new LimitOrderEntity(
        "123",
        "123",
        BTCZAR.name(),
        LimitOrderEntity.OrderSide.BUY,
        BigDecimal.valueOf(0.1),
        BigDecimal.valueOf(1000),
        LimitOrderEntity.OrderStatus.OPEN,
        result.createdAt()
      );

    assertThat(result)
            .isEqualTo(expected);
  }

  @Test
  void toEntity_SELL() {
    // given
    var limitOrderCommand =
      LimitOrderCommand.builder()
        .limitOrder(
          new LimitOrderCommand.LimitOrder(
            LimitOrderCommand.LimitOrder.OrderBookSide.SELL,
            BigDecimal.valueOf(0.1),
            BigDecimal.valueOf(1000),
            BTCZAR.name(),
            LimitOrderCommand.LimitOrder.OrderStatus.OPEN
          )
        )
        .customerOrderId("123")
        .build();

    // when
    var result = MAPPER.toEntity(limitOrderCommand);

    // then
    var expected =
      new LimitOrderEntity(
        "123",
        "123",
        BTCZAR.name(),
        LimitOrderEntity.OrderSide.SELL,
        BigDecimal.valueOf(0.1),
        BigDecimal.valueOf(1000),
        LimitOrderEntity.OrderStatus.OPEN,
        result.createdAt()
      );

    assertThat(result)
            .isEqualTo(expected);
  }

  @Test
  void toDomain_BUY() {
    // given
    var limitOrderEntity =
      new LimitOrderEntity(
        "456",
        null,
        BTCZAR.name(),
        LimitOrderEntity.OrderSide.BUY,
        BigDecimal.valueOf(0.1),
        BigDecimal.valueOf(1000),
        LimitOrderEntity.OrderStatus.OPEN,
        now()
      );

    // when
    var result = MAPPER.toDomain(limitOrderEntity);

    // then
    var expected =
      LimitOrderCommand.builder()
        .limitOrder(
          new LimitOrderCommand.LimitOrder(
            LimitOrderCommand.LimitOrder.OrderBookSide.BUY,
            BigDecimal.valueOf(0.1),
            BigDecimal.valueOf(1000),
            BTCZAR.name(),
            LimitOrderCommand.LimitOrder.OrderStatus.OPEN
          )
        )
        .customerOrderId("456")
        .build();

    assertThat(result)
            .isEqualTo(expected);
  }

  @Test
  void toDomain_SELL() {
    // given
    var limitOrderEntity =
      new LimitOrderEntity(
        "456",
        null,
        BTCZAR.name(),
        LimitOrderEntity.OrderSide.SELL,
        BigDecimal.valueOf(0.1),
        BigDecimal.valueOf(1000),
        LimitOrderEntity.OrderStatus.OPEN,
        now()
      );

    // when
    var result = MAPPER.toDomain(limitOrderEntity);

    var expected =
      LimitOrderCommand.builder()
        .limitOrder(
          new LimitOrderCommand.LimitOrder(
            LimitOrderCommand.LimitOrder.OrderBookSide.SELL,
            BigDecimal.valueOf(0.1),
            BigDecimal.valueOf(1000),
            BTCZAR.name(),
            LimitOrderCommand.LimitOrder.OrderStatus.OPEN
          )
        )
        .customerOrderId("456")
        .build();

    // then
    assertThat(result)
            .isEqualTo(expected);
  }
}