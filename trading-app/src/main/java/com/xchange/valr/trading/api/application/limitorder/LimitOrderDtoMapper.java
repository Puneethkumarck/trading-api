package com.xchange.valr.trading.api.application.limitorder;

import com.xchange.valr.api.model.LimitOrderRequestDto;
import com.xchange.valr.trading.api.domain.limitorder.LimitOrderCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface LimitOrderDtoMapper {

  @Mapping(target = "limitOrder.side", source = "side", qualifiedByName = "mapOrderSide")
  @Mapping(target = "limitOrder.quantity", source = "quantity")
  @Mapping(target = "limitOrder.price", source = "price")
  @Mapping(target = "limitOrder.currencyPair", source = "pair")
  LimitOrderCommand toCommand(LimitOrderRequestDto request);

  @Named("mapOrderSide")
  default LimitOrderCommand.LimitOrder.OrderBookSide mapOrderSide(String side) {
    return LimitOrderCommand.LimitOrder.OrderBookSide.valueOf(side);
  }
}
