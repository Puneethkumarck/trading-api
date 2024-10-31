package com.xchange.valr.trading.api.infrastructure.limitoder;

import static com.xchange.valr.trading.api.domain.model.CurrencyPair.BTCZAR;
import static java.time.Instant.now;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import org.junit.jupiter.api.Test;

class InMemoryOrderRepositoryTest {

  private static final InMemoryOrderRepository repository = new InMemoryOrderRepository();

  @Test
  void shouldSaveAndRetrieveOrder() {
    // given
    var entity =
      new LimitOrderEntity(
        "123",
        "123",
        BTCZAR.name(),
        LimitOrderEntity.OrderSide.BUY,
        BigDecimal.valueOf(0.1),
        BigDecimal.valueOf(1000),
        LimitOrderEntity.OrderStatus.OPEN,
        now()
      );

    // when
    var savedEntity = repository.save(entity);
    var retrievedEntity = repository.findByOrderId("123");

    // then
    assertThat(retrievedEntity)
            .usingRecursiveComparison()
            .isEqualTo(savedEntity);
  }

  @Test
  void shouldReturnEmptyWhenOrderNotFound() {
    // given
    var orderId = randomAlphanumeric(50);

    // when
    var retrievedEntity = repository.findByOrderId(orderId);

    // then
    assertThat(retrievedEntity).isEmpty();
  }
}
