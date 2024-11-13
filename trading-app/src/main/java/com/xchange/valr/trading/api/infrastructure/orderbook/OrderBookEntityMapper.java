package com.xchange.valr.trading.api.infrastructure.orderbook;

import org.mapstruct.Mapper;
import com.xchange.valr.trading.api.domain.orderbook.OrderBook;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;

@Mapper
public interface OrderBookEntityMapper {
  @Mapping(target = "bids", expression = "java(mapBidsToDescending(orderBookEntity.bids()))")
  @Mapping(target = "asks", expression = "java(mapAsksToAscending(orderBookEntity.asks()))")
  OrderBook toDomain(OrderBookEntity orderBookEntity);

  OrderBookEntity toEntity(OrderBook orderBook);

  default ConcurrentNavigableMap<BigDecimal, OrderBook.OrderBookLevel> mapBidsToDescending(
    TreeMap<BigDecimal, OrderBookEntity.OrderBookLevelEntity> bids
  ) {
    if (bids == null) {
      return new ConcurrentSkipListMap<>(Comparator.reverseOrder());
    }
    ConcurrentSkipListMap<BigDecimal, OrderBook.OrderBookLevel> result =
      new ConcurrentSkipListMap<>(Comparator.reverseOrder());
    result.putAll(
      bids.entrySet()
        .stream()
        .collect(
          Collectors.toMap(
            Map.Entry::getKey,
            e -> toOrderBookLevel(e.getValue())
          )
        )
    );
    return result;
  }

  default ConcurrentNavigableMap<BigDecimal, OrderBook.OrderBookLevel> mapAsksToAscending(
    TreeMap<BigDecimal, OrderBookEntity.OrderBookLevelEntity> asks
  ) {
    if (asks == null) {
      return new ConcurrentSkipListMap<>();
    }
    return new ConcurrentSkipListMap<>(
      asks.entrySet()
        .stream()
        .collect(
          Collectors.toMap(
            Map.Entry::getKey,
            e -> toOrderBookLevel(e.getValue())
          )
        )
    );
  }

  OrderBook.OrderBookLevel toOrderBookLevel(OrderBookEntity.OrderBookLevelEntity levelEntity);
}