package com.xchange.valr.trading.api.domain.limitorder;

import com.xchange.valr.trading.api.domain.orderbook.OrderBook;

import java.math.BigDecimal;
import java.util.concurrent.ConcurrentNavigableMap;

public class BuyOrderProcessor extends OrderProcessor {
  @Override
  protected ConcurrentNavigableMap<BigDecimal, OrderBook.OrderBookLevel> getMatchingSide(OrderBook orderBook) {
    return orderBook.asks();
  }

  @Override
  protected ConcurrentNavigableMap<BigDecimal, OrderBook.OrderBookLevel> getPlacementSide(OrderBook orderBook) {
    return orderBook.bids();
  }

  @Override
  protected boolean canMatch(BigDecimal buyPrice, BigDecimal askPrice) {
    return buyPrice.compareTo(askPrice) >= 0;
  }

  @Override
  protected OrderBook.OrderBookSide getOrderSide() {
    return OrderBook.OrderBookSide.BUY;
  }
}
