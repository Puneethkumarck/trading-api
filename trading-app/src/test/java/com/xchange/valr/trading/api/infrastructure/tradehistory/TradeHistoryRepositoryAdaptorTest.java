package com.xchange.valr.trading.api.infrastructure.tradehistory;

import com.xchange.valr.trading.api.domain.tradehistory.TradeHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.xchange.valr.trading.api.domain.model.CurrencyPair.BTCZAR;
import static com.xchange.valr.trading.fixtures.TradeFixtures.createTrade;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TradeHistoryRepositoryAdaptorTest {
  @Mock
  private InMemoryTradeRepository repository;
  @Spy
  private final TradeHistoryEntityMapper mapper = new TradeHistoryEntityMapperImpl();
  @InjectMocks
  private TradeHistoryRepositoryAdaptor adaptor;

  @Test
  void shouldSaveTrade() {
    // given
    var trade = createTrade(BTCZAR.name(), "BUY");
    var entity =
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

    when(repository.save(any(TradeEntity.class))).thenReturn(Optional.of(entity));

    // when
    var result = adaptor.save(trade);

    // then
    assertThat(result)
      .usingRecursiveComparison()
      .isEqualTo(Optional.of(trade));
  }

  @Test
  void shouldFindRecentTrade() {
    // given
    var trade = createTrade(BTCZAR.name(), "BUY");
    var entity =
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
    when(repository.findRecentTradeByCurrencyPair(BTCZAR.name(), 1))
      .thenReturn(Optional.of(List.of(entity)));

    // when
    var result = adaptor.findRecentTradesByCurrencyPair(BTCZAR.name(), 1);

    // then
    assertThat(result)
      .isPresent()
      .hasValueSatisfying(
        trades -> {
          assertThat(trades).hasSize(1);
          assertThat(trades.get(0)).usingRecursiveComparison().isEqualTo(trade);
        }
      );
  }

  @Test
  void shouldFindTradeByOrderId() {
    // given
    var trade = createTrade(BTCZAR.name(), "BUY");
    var entity =
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

    when(repository.findTradesByOrderId(trade.id())).thenReturn(Optional.of(entity));

    // when
    var result = adaptor.findTradesByOrderId(trade.id());

    // then
    assertThat(result)
      .isPresent()
      .hasValue(trade);
  }
}
