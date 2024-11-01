package com.xchange.valr.trading.api.application.tradehistory;

import com.xchange.valr.api.model.TradeHistoryResponseDto;
import com.xchange.valr.trading.api.domain.tradehistory.Trade;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface TradeHistoryDtoMapper {
  TradeHistoryResponseDto toDto(Trade trade);

  default List<TradeHistoryResponseDto> toDtoList(List<Trade> trades) {
    if (trades == null) {
      return List.of();
    }
    return trades.stream().map(this::toDto).collect(Collectors.toList());
  }
}
