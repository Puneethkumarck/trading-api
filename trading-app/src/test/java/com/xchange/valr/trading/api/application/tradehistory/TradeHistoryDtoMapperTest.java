package com.xchange.valr.trading.api.application.tradehistory;

import com.xchange.valr.api.model.TradeHistoryResponseDto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.xchange.valr.trading.api.domain.model.CurrencyPair.BTCZAR;
import static com.xchange.valr.trading.api.domain.tradehistory.Trade.TakerSide.BUY;
import static com.xchange.valr.trading.fixtures.TradeFixtures.createTrade;
import static org.assertj.core.api.Assertions.assertThat;

class TradeHistoryDtoMapperTest {

  private static final TradeHistoryDtoMapper mapper = new TradeHistoryDtoMapperImpl();

  @Test
  void toDto() {
    // given
    var trade = createTrade(BTCZAR.name(), BUY);

    // when
    var tradeDto = mapper.toDto(trade);

    // then
    var expected = TradeHistoryResponseDto.builder()
      .currencyPair(trade.currencyPair())
      .price(trade.price())
      .quantity(trade.quantity())
      .takerSide(trade.takerSide())
      .tradedAt(trade.tradedAt())
      .sequenceId(trade.sequenceId())
      .id(trade.id())
      .quoteVolume(trade.quoteVolume())
      .build();

    assertThat(tradeDto)
      .usingRecursiveComparison()
      .isEqualTo(expected);
  }

  @Test
  void toDtoList() {
    // given
    var trade1 = createTrade(BTCZAR.name(), BUY);
    var trade2 = createTrade(BTCZAR.name(), BUY);
    var trades = List.of(trade1, trade2);

    // when
    var result = mapper.toDtoList(trades);

    // then
    var expected1 = TradeHistoryResponseDto.builder()
      .currencyPair(trade1.currencyPair())
      .price(trade1.price())
      .quantity(trade1.quantity())
      .takerSide(trade1.takerSide())
      .tradedAt(trade1.tradedAt())
      .sequenceId(trade1.sequenceId())
      .id(trade1.id())
      .quoteVolume(trade1.quoteVolume())
      .build();

    var expected2 = TradeHistoryResponseDto.builder()
      .currencyPair(trade2.currencyPair())
      .price(trade2.price())
      .quantity(trade2.quantity())
      .takerSide(trade2.takerSide())
      .tradedAt(trade2.tradedAt())
      .sequenceId(trade2.sequenceId())
      .id(trade2.id())
      .quoteVolume(trade2.quoteVolume())
      .build();

    var expected = List.of(expected1, expected2);

    assertThat(result)
      .usingRecursiveComparison()
      .isEqualTo(expected);
  }

  @Test
  void toDtoListNull() {
    // when
    var result = mapper.toDtoList(null);

    // then
    assertThat(result).isEmpty();
  }

}