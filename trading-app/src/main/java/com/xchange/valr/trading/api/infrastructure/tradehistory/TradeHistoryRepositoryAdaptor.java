package com.xchange.valr.trading.api.infrastructure.tradehistory;

import com.xchange.valr.trading.api.domain.tradehistory.Trade;
import com.xchange.valr.trading.api.domain.tradehistory.TradeHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TradeHistoryRepositoryAdaptor implements TradeHistoryRepository {

  private final InMemoryTradeRepository repository;

  private final TradeHistoryEntityMapper mapper;

  @Override
  public Optional<Trade> save(Trade trade) {
    return repository.save(mapper.toEntity(trade)).map(mapper::toDomain);
  }

  @Override
  public Optional<List<Trade>> findRecentTradesByCurrencyPair(String currencyPair, int limit) {
    return repository.findRecentTradeByCurrencyPair(currencyPair, limit).map(mapper::toDomainList);
  }

  @Override
  public Optional<Trade> findTradesByOrderId(String orderId) {
    return repository.findTradesByOrderId(orderId).map(mapper::toDomain);
  }
}
