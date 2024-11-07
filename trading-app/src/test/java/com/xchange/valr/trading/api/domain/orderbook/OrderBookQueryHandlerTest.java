package com.xchange.valr.trading.api.domain.orderbook;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.xchange.valr.trading.api.domain.model.CurrencyPair.BTCZAR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static com.xchange.valr.trading.fixtures.OrderBookFixtures.orderBook;

@ExtendWith(MockitoExtension.class)
class OrderBookQueryHandlerTest {
  @InjectMocks
  private OrderBookQueryHandler orderBookQueryHandler;
  @Mock
  private OrderBookRepository orderBookRepository;

  @Test
  void getOrderBook() {
    // given
    var orderBook = orderBook(BTCZAR.name());
    given(orderBookRepository.findByCurrencyPair(BTCZAR.name())).willReturn(Optional.of(orderBook));

    // when
    var result = orderBookQueryHandler.getOrderBook(BTCZAR.name());

    // then
    assertThat(result).isEqualTo(orderBook);
  }

  @Test
  void shouldThrowExceptionWhenOrderBookNotFound() {
    // given
    given(orderBookRepository.findByCurrencyPair(BTCZAR.name())).willReturn(Optional.empty());

    // when then
    assertThatThrownBy(() -> orderBookQueryHandler.getOrderBook(BTCZAR.name()))
      .isInstanceOf(OrderBookNotFoundException.class)
      .hasMessage("Order book not found for currency pair: BTCZAR");
  }
}