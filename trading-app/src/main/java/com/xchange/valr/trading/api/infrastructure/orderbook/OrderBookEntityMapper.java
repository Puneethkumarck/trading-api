package com.xchange.valr.trading.api.infrastructure.orderbook;

import org.mapstruct.Mapper;
import com.xchange.valr.trading.api.domain.orderbook.OrderBook;

@Mapper
public interface OrderBookEntityMapper {
  OrderBook toDomain(OrderBookEntity orderBookEntity);

  OrderBookEntity toEntity(OrderBook orderBook);
}
