package com.xchange.valr.trading.api.domain.orderbook;

import com.xchange.valr.trading.api.domain.limitorder.LimitOrderCommand;
import com.xchange.valr.trading.api.domain.limitorder.LimitOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderBookIdemPotencyValidator {
  private final LimitOrderRepository orderRepository;

  public Optional<LimitOrderCommand> validate(LimitOrderCommand command) {
    return validateOrderIdempotency(command).flatMap(this::saveOrder);
  }

  private Optional<LimitOrderCommand> validateOrderIdempotency(LimitOrderCommand command) {
    if (orderRepository.findByOrderId(command.customerOrderId()).isPresent()) {
      log.warn("Attempted to place duplicate order with orderId: {}", command.customerOrderId());
      throw LimitOrderAlreadyExistsException.withOrderId(command.customerOrderId());
    }
    return Optional.of(command);
  }

  private Optional<LimitOrderCommand> saveOrder(LimitOrderCommand command) {
    log.debug("Saving new order with orderId: {}", command.customerOrderId());
    return Optional.ofNullable(orderRepository.save(command));
  }
}
