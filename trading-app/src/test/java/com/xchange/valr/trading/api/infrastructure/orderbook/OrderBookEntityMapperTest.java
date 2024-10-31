package com.xchange.valr.trading.api.infrastructure.orderbook;

import com.xchange.valr.trading.fixtures.OrderBookEntityFixtures;
import com.xchange.valr.trading.fixtures.OrderBookFixtures;
import org.junit.jupiter.api.Test;

import static com.xchange.valr.trading.api.domain.model.CurrencyPair.BTCZAR;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class OrderBookEntityMapperTest {
  private final OrderBookEntityMapper mapper = new OrderBookEntityMapperImpl();

  @Test
  void toDomain() {
    // given
    var orderBookEntity = OrderBookEntityFixtures.createOrderBookEntity(BTCZAR.name());

    // when
    var result = mapper.toDomain(orderBookEntity);
    var expected = OrderBookFixtures.orderBook(BTCZAR.name());

    // then
    assertThat(result)
      .usingRecursiveComparison()
      .ignoringFields("lastChange")
      .isEqualTo(expected);
  }

  @Test
  void toEntity() {
    // given
    var orderBook = OrderBookFixtures.orderBook(BTCZAR.name());

    // when
    var result = mapper.toEntity(orderBook);
    var expected = OrderBookEntityFixtures.createOrderBookEntity(BTCZAR.name());

    // then
    assertThat(result)
      .usingRecursiveComparison()
      .ignoringFields("lastChange")
      .isEqualTo(expected);
  }
}
