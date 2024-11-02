package com.xchange.valr.trading.api.domain.limitorder;

import com.xchange.valr.trading.api.domain.orderbook.OrderBook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class SellOrderProcessorTest extends OrderProcessorBaseTest {
  private final SellOrderProcessor processor = new SellOrderProcessor();

  @Override
  protected OrderProcessor getProcessor() {
    return processor;
  }

  @Override
  protected OrderBook.OrderBookSide getExpectedSide() {
    return OrderBook.OrderBookSide.SELL;
  }

  @Test
  void shouldReturnBidsAsMatchingSide() {
    //given
    var orderBook = createOrderBook();

    //when then
    assertThat(processor.getMatchingSide(orderBook))
      .isSameAs(orderBook.bids());
  }

  @Test
  void shouldReturnAsksAsPlacementSide() {
    //given
    var orderBook = createOrderBook();

    //when then
    assertThat(processor.getPlacementSide(orderBook))
      .isSameAs(orderBook.asks());
  }

  @ParameterizedTest
  @CsvSource({
    "100, 100, true,  'matches when sell price equals bid price'",
    "90,  100, true,  'matches when sell price lower than bid price'",
    "110, 100, false, 'does not match when sell price higher than bid price'"
  })
  void shouldCorrectlyDetermineMatching(BigDecimal sellPrice, BigDecimal bidPrice, boolean expectedResult) {
    assertThat(processor.canMatch(sellPrice, bidPrice)).isEqualTo(expectedResult);
  }
}
