package com.xchange.valr.trading.api.infrastructure.orderbook;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.xchange.valr.trading.api.domain.model.CurrencyPair.BTCZAR;
import static com.xchange.valr.trading.fixtures.OrderBookEntityFixtures.createOrderBookEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderBookRepositoryAdaptorTest {
  @InjectMocks
  private OrderBookRepositoryAdaptor adaptor;
  @Mock
  private InMemoryOrderBookRepository inMemoryOrderBookRepository;
  @Spy
  private final OrderBookEntityMapper mapper = new OrderBookEntityMapperImpl();

  @Test
  void shouldGetOrderBookForGivenCurrencyPair() {
    // given
    var orderBookEntity = createOrderBookEntity(BTCZAR.name());
    var orderBook = Optional.ofNullable(mapper.toDomain(orderBookEntity));
    when(inMemoryOrderBookRepository.findByCurrencyPair(BTCZAR.name()))
      .thenReturn(Optional.of(orderBookEntity));

    // when
    var result = adaptor.findByCurrencyPair(BTCZAR.name());

    // then
    assertThat(result)
      .usingRecursiveComparison()
      .isEqualTo(orderBook);
  }

  @Test
  void shouldReturnEmptyWhenOrderBookNotFoundForGivenCurrencyPair() {
    // given
    when(inMemoryOrderBookRepository.findByCurrencyPair(BTCZAR.name()))
        .thenReturn(Optional.empty());

    // when
    var result = adaptor.findByCurrencyPair(BTCZAR.name());

    // then
    assertThat(result).isEmpty();
  }
}
