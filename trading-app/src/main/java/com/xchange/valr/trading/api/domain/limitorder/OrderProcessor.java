package com.xchange.valr.trading.api.domain.limitorder;

import com.xchange.valr.trading.api.domain.orderbook.OrderBook;

import java.math.BigDecimal;
import java.util.TreeMap;

abstract class OrderProcessor {
  protected abstract TreeMap<BigDecimal, OrderBook.OrderBookLevel> getMatchingSide(OrderBook orderBook);

  protected abstract TreeMap<BigDecimal, OrderBook.OrderBookLevel> getPlacementSide(OrderBook orderBook);

  protected abstract boolean canMatch(BigDecimal orderPrice, BigDecimal matchPrice);

  protected abstract OrderBook.OrderBookSide getOrderSide();

  protected void processOrder(LimitOrderCommand command, OrderBook orderBook, OrderExecutor executor) {
    var remainingQuantity = command.limitOrder().quantity();
    var price = command.limitOrder().price();

    // Match orders
    remainingQuantity = matchOrders(command, orderBook, remainingQuantity, executor);

    // Place remaining quantity if any
    if (remainingQuantity.compareTo(BigDecimal.ZERO) > 0) {
      placeOrder(command.currencyPair(), price, remainingQuantity, getPlacementSide(orderBook));
    }
  }

  private BigDecimal matchOrders(
    LimitOrderCommand command,
    OrderBook orderBook,
    BigDecimal remainingQuantity,
    OrderExecutor executor
  ) {
    var matchingSide = getMatchingSide(orderBook);
    var currentQuantity = remainingQuantity;

    for (var entry : matchingSide.entrySet()) {
      var matchPrice = entry.getKey();
      var matchLevel = entry.getValue();

      if (!canMatch(command.limitOrder().price(), matchPrice)) {
        break;
      }

      if (currentQuantity.compareTo(BigDecimal.ZERO) > 0) {
        var matchedQuantity = currentQuantity.min(matchLevel.quantity());
        currentQuantity = currentQuantity.subtract(matchedQuantity);

        // Execute trade
        executor.executeTrade(command, matchPrice, matchedQuantity);

        // Update match level
        updateMatchLevel(matchingSide, matchPrice, matchLevel, matchedQuantity);
      }
    }

    return currentQuantity;
  }

  private void updateMatchLevel(
    TreeMap<BigDecimal, OrderBook.OrderBookLevel> side,
    BigDecimal price,
    OrderBook.OrderBookLevel level,
    BigDecimal matchedQuantity
  ) {
    var updatedQuantity = level.quantity().subtract(matchedQuantity);
    if (updatedQuantity.compareTo(BigDecimal.ZERO) > 0) {
      side.put(
        price,
        level.toBuilder()
          .quantity(updatedQuantity)
          .build()
      );
    } else {
      side.remove(price);
    }
  }

  private void placeOrder(
    String currencyPair,
    BigDecimal price,
    BigDecimal quantity,
    TreeMap<BigDecimal, OrderBook.OrderBookLevel> side
  ) {
    var existingLevel = side.get(price);
    if (existingLevel != null) {
      side.put(
        price,
        existingLevel.toBuilder()
          .quantity(existingLevel.quantity().add(quantity))
          .orderCount(existingLevel.orderCount() + 1)
          .build()
      );
    } else {
      side.put(
        price,
        OrderBook.OrderBookLevel.builder()
          .side(getOrderSide())
          .price(price)
          .quantity(quantity)
          .currencyPair(currencyPair)
          .orderCount(1)
          .build()
      );
    }
  }
}
