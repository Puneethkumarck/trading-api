package com.xchange.valr.trading.api.infrastructure.limitoder;

import com.xchange.valr.trading.api.domain.limitorder.LimitOrderCommand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@Mapper
public interface LimitOrderEntityMapper {
  @Mapping(target = "orderId", source = "command", qualifiedByName = "mapOrderId")
  @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())")
  @Mapping(target = "side", source = "command", qualifiedByName = "mapSide")
  @Mapping(target = "quantity", source = "command", qualifiedByName = "mapQuantity")
  @Mapping(target = "price", source = "command", qualifiedByName = "mapPrice")
  @Mapping(target = "status", source = "command", qualifiedByName = "mapStatus")
  @Mapping(target = "currencyPair", source = "command", qualifiedByName = "mapCurrencyPair")
  LimitOrderEntity toEntity(LimitOrderCommand command);

  @Mapping(target = "limitOrder", source = "entity", qualifiedByName = "createLimitOrder")
  @Mapping(target = "customerOrderId", source = "orderId")
  LimitOrderCommand toDomain(LimitOrderEntity entity);

  @Named("createLimitOrder")
  default LimitOrderCommand.LimitOrder createLimitOrder(LimitOrderEntity entity) {
    return LimitOrderCommand.LimitOrder.builder()
      .side(mapToDomainSide(entity.side()))
      .quantity(entity.quantity())
      .price(entity.price())
      .currencyPair(entity.currencyPair())
      .status(mapToDomainStatus(entity.status()))
      .build();
  }

  @Named("mapOrderId")
  default String mapOrderId(LimitOrderCommand command) {
    return command.customerOrderId() != null ? command.customerOrderId() : randomAlphanumeric(50);
  }

  default LimitOrderCommand.LimitOrder.OrderBookSide mapToDomainSide(
    LimitOrderEntity.OrderSide side
  ) {
    return LimitOrderCommand.LimitOrder.OrderBookSide.valueOf(side.name());
  }

  default LimitOrderCommand.LimitOrder.OrderStatus mapToDomainStatus(
    LimitOrderEntity.OrderStatus status
  ) {
    return LimitOrderCommand.LimitOrder.OrderStatus.valueOf(status.name());
  }

  @Named("mapSide")
  default LimitOrderEntity.OrderSide mapSide(LimitOrderCommand command) {
    return LimitOrderEntity.OrderSide.valueOf(command.limitOrder().side().name());
  }

  @Named("mapQuantity")
  default java.math.BigDecimal mapQuantity(LimitOrderCommand command) {
    return command.limitOrder().quantity();
  }

  @Named("mapPrice")
  default java.math.BigDecimal mapPrice(LimitOrderCommand command) {
    return command.limitOrder().price();
  }

  @Named("mapStatus")
  default LimitOrderEntity.OrderStatus mapStatus(LimitOrderCommand command) {
    return LimitOrderEntity.OrderStatus.valueOf(
      LimitOrderCommand.LimitOrder.OrderStatus.OPEN.name()
    );
  }

  @Named("mapCurrencyPair")
  default String mapCurrencyPair(LimitOrderCommand command) {
    return command.limitOrder().currencyPair();
  }
}
