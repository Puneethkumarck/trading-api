package com.xchange.valr.trading.api.application.orderbook;

import com.xchange.valr.api.model.OrderBookResponseDto;
import com.xchange.valr.trading.api.domain.orderbook.OrderBook;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.math.BigDecimal;
import java.util.List;
import java.util.TreeMap;

@Mapper
public interface OrderBookDtoMapper {

  @Mapping(target = "asks", source = "asks", qualifiedByName = "mapOrderBookLevels")
  @Mapping(target = "bids", source = "bids", qualifiedByName = "mapOrderBookLevels")
  OrderBookResponseDto toDto(OrderBook orderBook);

  @Named("mapOrderBookLevels")
  default List<OrderBookResponseDto.OrderBookEntryDto> mapOrderBookLevels(
    TreeMap<BigDecimal, OrderBook.OrderBookLevel> levels
  ) {

    if (levels == null || levels.isEmpty()) {
      return List.of();
    }

    return levels.values()
      .stream()
      .map(
        level -> new OrderBookResponseDto.OrderBookEntryDto(
          level.side().name(),
          level.quantity().toPlainString(),
          level.price().toPlainString(),
          level.currencyPair(),
          level.orderCount()
        )
      )
      .toList();
  }
}
