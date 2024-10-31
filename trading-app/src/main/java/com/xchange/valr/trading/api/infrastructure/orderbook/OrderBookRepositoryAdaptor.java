package com.xchange.valr.trading.api.infrastructure.orderbook;

import com.xchange.valr.trading.api.domain.orderbook.OrderBook;
import com.xchange.valr.trading.api.domain.orderbook.OrderBookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderBookRepositoryAdaptor implements OrderBookRepository {

  private final InMemoryOrderBookRepository inMemoryOrderBookRepository;
  private final OrderBookEntityMapper mapper;

  @Override
  public Optional<OrderBook> findByCurrencyPair(String currencyPair) {
    return inMemoryOrderBookRepository.findByCurrencyPair(currencyPair).map(mapper::toDomain);
  }

  @Override
  public Optional<OrderBook> save(OrderBook orderBook) {
    return inMemoryOrderBookRepository.save(mapper.toEntity(orderBook)).map(mapper::toDomain);
  }
}
