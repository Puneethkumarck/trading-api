package com.xchange.valr.trading.api.domain.limitorder;

import com.xchange.valr.trading.api.domain.orderbook.OrderBook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class BuyOrderProcessorTest extends OrderProcessorBaseTest {

  private final BuyOrderProcessor processor = new BuyOrderProcessor();

  @Override
  protected OrderProcessor getProcessor() {
    return processor;
  }

  @Override
  protected OrderBook.OrderBookSide getExpectedSide() {
    return OrderBook.OrderBookSide.BUY;
  }

  @Test
  void shouldReturnAsksAsMatchingSide() {
    //given
    var orderBook = createOrderBook();

    //when then
    assertThat(processor.getMatchingSide(orderBook))
      .isEqualTo(orderBook.asks());
  }

  @Test
  void shouldReturnBidsAsPlacementSide() {
    //given
    var orderBook = createOrderBook();

    //when then
    assertThat(processor.getPlacementSide(orderBook))
      .isEqualTo(orderBook.bids());
  }

  @ParameterizedTest
  @CsvSource({
    "100, 100, true,  'matches when buy price equals ask price'",
    "110, 100, true,  'matches when buy price higher than ask price'",
    "90,  100, false, 'does not match when buy price lower than ask price'"
  })
  void shouldCorrectlyDetermineMatching(BigDecimal buyPrice, BigDecimal askPrice, boolean expectedResult) {
    assertThat(processor.canMatch(buyPrice, askPrice)).isEqualTo(expectedResult);
  }
}
