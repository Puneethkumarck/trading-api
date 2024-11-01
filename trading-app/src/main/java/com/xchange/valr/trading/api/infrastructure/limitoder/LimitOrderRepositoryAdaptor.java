package com.xchange.valr.trading.api.infrastructure.limitoder;

import com.xchange.valr.trading.api.domain.limitorder.LimitOrderCommand;
import com.xchange.valr.trading.api.domain.limitorder.LimitOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Slf4j
@RequiredArgsConstructor
public class LimitOrderRepositoryAdaptor implements LimitOrderRepository {

  private final InMemoryOrderRepository repository;

  private final LimitOrderEntityMapper mapper;

  @Override
  public LimitOrderCommand save(LimitOrderCommand order) {
    return repository.save(mapper.toEntity(order)).map(mapper::toDomain).orElse(null);
  }

  @Override
  public Optional<LimitOrderCommand> findByOrderId(String orderId) {
    return repository.findByOrderId(orderId).map(mapper::toDomain);
  }
}
