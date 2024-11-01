package com.xchange.valr.trading.api.infrastructure.tradehistory;

import com.xchange.valr.trading.api.domain.tradehistory.Trade;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface TradeHistoryEntityMapper {
  TradeEntity toEntity(Trade trade);

  Trade toDomain(TradeEntity tradeEntity);

  List<Trade> toDomainList(List<TradeEntity> tradeEntity);
}
