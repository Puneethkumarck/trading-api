package com.xchange.valr.trading.api.domain.tradehistory;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static com.xchange.valr.trading.api.domain.tradehistory.Trade.TakerSide.BUY;
import static com.xchange.valr.trading.api.domain.tradehistory.Trade.TakerSide.SELL;
import static com.xchange.valr.trading.fixtures.TradeFixtures.createTrade;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TradeHistoryQueryHandlerTest {
  private static final String CURRENCY_PAIR = "BTCZAR";
  @InjectMocks
  private TradeHistoryQueryHandler tradeHistoryQueryHandler;
  @Mock
  private TradeHistoryRepository tradeHistoryRepository;

  @Test
  void getTradeHistory() {
    // Given
    List<Trade> trades =
      List.of(createTrade(CURRENCY_PAIR, SELL), createTrade(CURRENCY_PAIR, BUY));
    when(tradeHistoryRepository.findRecentTradesByCurrencyPair(CURRENCY_PAIR, 10))
      .thenReturn(Optional.of(trades));

    // When
    var result = tradeHistoryQueryHandler.getTradeHistory(CURRENCY_PAIR, 10);

    // Then
    assertThat(result)
      .usingRecursiveAssertion()
      .isEqualTo(Optional.of(trades));
  }

  @Test
    void shouldReturnEmptyListWhenNoTradesFound() {
        // Given
        when(tradeHistoryRepository.findRecentTradesByCurrencyPair(CURRENCY_PAIR, 10))
                .thenReturn(Optional.empty());

        // When
        var result = tradeHistoryQueryHandler.getTradeHistory(CURRENCY_PAIR, 10);

        // Then
        assertThat(result).isEmpty();
    }
}
