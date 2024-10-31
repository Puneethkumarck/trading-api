package com.xchange.valr.trading.api.infrastructure.orderbook;

import org.junit.jupiter.api.Test;

import static com.xchange.valr.trading.api.domain.model.CurrencyPair.BTCZAR;
import static com.xchange.valr.trading.fixtures.OrderBookEntityFixtures.createOrderBookEntity;
import static org.assertj.core.api.Assertions.assertThat;

class InMemoryOrderBookRepositoryTest {
  private static final InMemoryOrderBookRepository repository = new InMemoryOrderBookRepository();

  @Test
  void shouldSaveAndRetrieveOrderBook() {
    // given
    var entity = createOrderBookEntity(BTCZAR.name());

    // when
    var savedEntity = repository.save(entity);
    var retrievedEntity = repository.findByCurrencyPair(BTCZAR.name());

    // then
    assertThat(retrievedEntity)
      .usingRecursiveComparison()
      .isEqualTo(savedEntity);
  }

  @Test
  void shouldReturnEmptyWhenOrderBookNotFound() {
    // when
    var retrievedEntity = repository.findByCurrencyPair(BTCZAR.name());

    // then
    assertThat(retrievedEntity).isEmpty();
  }
}