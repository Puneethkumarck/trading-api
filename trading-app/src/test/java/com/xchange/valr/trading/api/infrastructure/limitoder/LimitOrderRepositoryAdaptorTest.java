package com.xchange.valr.trading.api.infrastructure.limitoder;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.xchange.valr.trading.fixtures.LimitOrderCommandFixtures.createLimitOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LimitOrderRepositoryAdaptorTest {
  @InjectMocks
  private LimitOrderRepositoryAdaptor adaptor;
  @Mock
  private InMemoryOrderRepository repository;
  @Spy
  private final LimitOrderEntityMapper mapper = new LimitOrderEntityMapperImpl();

  @Test
  void shouldSaveLimitOrder() {
    // given
    var command = createLimitOrder();
    var entity = mapper.toEntity(command);
    when(repository.save(any(LimitOrderEntity.class))).thenReturn(Optional.of(entity));

    // when
    var result = adaptor.save(command);

    // then
    assertThat(result).usingRecursiveComparison().isEqualTo(command);
  }

  @Test
  void shouldFindLimitOrder() {
    // given
    var command = createLimitOrder();
    var entity = mapper.toEntity(command);
    when(repository.findByOrderId(command.customerOrderId())).thenReturn(Optional.of(entity));

    // when
    var result = adaptor.findByOrderId(command.customerOrderId());

    // then
    assertThat(result).usingRecursiveComparison().isEqualTo(Optional.of(command));
  }

  @Test
  void shouldReturnEmptyWhenLimitOrderNotFound() {
    // given
    var command = createLimitOrder();
    when(repository.findByOrderId(command.customerOrderId())).thenReturn(Optional.empty());

    // when
    var result = adaptor.findByOrderId(command.customerOrderId());

    // then
    assertThat(result).isEmpty();
  }
}
