package com.xchange.valr.trading.api.domain.orderbook;

import com.xchange.valr.trading.api.domain.limitorder.LimitOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.xchange.valr.trading.fixtures.LimitOrderCommandFixtures.createLimitOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderBookIdemPotencyValidatorTest {
  @Mock
  private LimitOrderRepository orderRepository;

  @InjectMocks
  private OrderBookIdemPotencyValidator validator;

  @Test
  void shouldSaveNewOrder() {
    // given
    var command = createLimitOrder();
    when(orderRepository.findByOrderId(command.customerOrderId())).thenReturn(Optional.empty());
    when(orderRepository.save(command)).thenReturn(command);

    // when
    var result = validator.validate(command);

    // then
    assertThat(result)
      .usingRecursiveComparison()
      .isEqualTo(Optional.of(command));
  }

  @Test
  void shouldThrowLimitOrderAlreadyExistsExceptionWhenDuplicateOrderFound() {
    // given
    var command = createLimitOrder();
    when(orderRepository.findByOrderId(command.customerOrderId())).thenReturn(Optional.of(command));

    // when
    assertThatThrownBy(() -> validator.validate(command))
      .isInstanceOf(LimitOrderAlreadyExistsException.class)
      .hasMessageContaining(
        "Limit order with orderId %s already exists".formatted(command.customerOrderId())
      );
  }

  @ParameterizedTest(name = "should return limit order command when orderId is blank or null")
  @ValueSource(strings = {"", " ", "null"})
  void shouldReturnLimitOrderCommandWhenOrderIdIsBlank(String orderId) {
    // given
    var command = createLimitOrder().toBuilder().customerOrderId(orderId).build();
    when(orderRepository.save(command)).thenReturn(command);

    // when
    var result = validator.validate(command);

    // then
    assertThat(result)
      .usingRecursiveComparison()
      .isEqualTo(Optional.of(command));
  }
}