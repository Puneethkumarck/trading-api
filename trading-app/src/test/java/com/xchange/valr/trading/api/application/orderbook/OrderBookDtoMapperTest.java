package com.xchange.valr.trading.api.application.orderbook;

import static com.xchange.valr.trading.api.domain.model.CurrencyPair.BTCZAR;
import static com.xchange.valr.trading.fixtures.OrderBookDtoFixtures.orderBookDto;
import static com.xchange.valr.trading.fixtures.OrderBookFixtures.orderBook;
import static org.assertj.core.api.Assertions.assertThat;

import com.xchange.valr.trading.api.application.orderbook.OrderBookDtoMapper;
import com.xchange.valr.trading.api.application.orderbook.OrderBookDtoMapperImpl;
import org.junit.jupiter.api.Test;

class OrderBookDtoMapperTest {
  private static final OrderBookDtoMapper mapper = new OrderBookDtoMapperImpl();

  @Test
  void toDto() {
    // given
    var domain = orderBook(BTCZAR.name());

    // when
    var result = mapper.toDto(domain);

    // then
    var expected = orderBookDto(BTCZAR.name());

    assertThat(result)
            .usingRecursiveComparison()
            .ignoringFields("lastChange")
            .isEqualTo(expected);
  }
}
