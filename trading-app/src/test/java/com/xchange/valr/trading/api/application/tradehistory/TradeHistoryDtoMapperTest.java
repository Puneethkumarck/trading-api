package com.xchange.valr.trading.api.application.tradehistory;

import com.xchange.valr.api.model.TradeHistoryResponseDto;

import static com.xchange.valr.trading.api.domain.model.CurrencyPair.BTCZAR;
import static com.xchange.valr.trading.api.domain.tradehistory.Trade.TakerSide.BUY;
import static com.xchange.valr.trading.fixtures.TradeFixtures.createTrade;
import static org.assertj.core.api.Assertions.assertThat;

class TradeHistoryDtoMapperTest {

  private static final TradeHistoryDtoMapper mapper = new TradeHistoryDtoMapperImpl();

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

}