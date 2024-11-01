package com.xchange.valr.trading.api.infrastructure.tradehistory;

import com.xchange.valr.trading.api.domain.tradehistory.Trade;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.xchange.valr.trading.api.domain.model.CurrencyPair.BTCZAR;
import static com.xchange.valr.trading.api.domain.tradehistory.Trade.TakerSide.BUY;
import static com.xchange.valr.trading.fixtures.TradeEntityFixtures.createTradeEntity;
import static com.xchange.valr.trading.fixtures.TradeFixtures.createTrade;
import static org.assertj.core.api.Assertions.assertThat;

class TradeHistoryEntityMapperTest {

  private final TradeHistoryEntityMapper mapper = new TradeHistoryEntityMapperImpl();

  @Test
  void toEntity() {
    // given
    var trade = createTrade(BTCZAR.name(), BUY);

    // when
    var result = mapper.toEntity(trade);

    // then
    var expected =
      TradeEntity.builder()
        .id(trade.id())
        .currencyPair(trade.currencyPair())
        .takerSide(trade.takerSide())
        .price(trade.price())
        .quantity(trade.quantity())
        .quoteVolume(trade.quoteVolume())
        .sequenceId(trade.sequenceId())
        .tradedAt(trade.tradedAt())
        .build();

    assertThat(result)
      .usingRecursiveComparison()
      .isEqualTo(expected);
  }

  @Test
  void toDomain() {
    // given
    var tradeEntity = createTradeEntity(BTCZAR.name(), "BUY");

    // when
    var result = mapper.toDomain(tradeEntity);

    // then
    var expected =
      Trade.builder()
        .id(tradeEntity.id())
        .currencyPair(tradeEntity.currencyPair())
        .takerSide(tradeEntity.takerSide())
        .price(tradeEntity.price())
        .quantity(tradeEntity.quantity())
        .quoteVolume(tradeEntity.quoteVolume())
        .sequenceId(tradeEntity.sequenceId())
        .tradedAt(tradeEntity.tradedAt())
        .build();

    assertThat(result)
      .usingRecursiveComparison()
      .isEqualTo(expected);
  }

  @Test
  void toDomainList() {
    // given
    var tradeEntity1 = createTradeEntity(BTCZAR.name(), "BUY");
    var tradeEntity2 = createTradeEntity(BTCZAR.name(), "BUY");
    var tradeEntityList = List.of(tradeEntity1, tradeEntity2);

    // when
    var result = mapper.toDomainList(tradeEntityList);

    // then
    var trade1 =
      Trade.builder()
        .id(tradeEntity1.id())
        .currencyPair(tradeEntity1.currencyPair())
        .takerSide(tradeEntity1.takerSide())
        .price(tradeEntity1.price())
        .quantity(tradeEntity1.quantity())
        .quoteVolume(tradeEntity1.quoteVolume())
        .sequenceId(tradeEntity1.sequenceId())
        .tradedAt(tradeEntity1.tradedAt())
        .build();

    var trade2 =
      Trade.builder()
        .id(tradeEntity2.id())
        .currencyPair(tradeEntity2.currencyPair())
        .takerSide(tradeEntity2.takerSide())
        .price(tradeEntity2.price())
        .quantity(tradeEntity2.quantity())
        .quoteVolume(tradeEntity2.quoteVolume())
        .sequenceId(tradeEntity2.sequenceId())
        .tradedAt(tradeEntity2.tradedAt())
        .build();

    var expected = List.of(trade1, trade2);

    assertThat(result)
      .usingRecursiveComparison()
      .isEqualTo(expected);
  }
}
