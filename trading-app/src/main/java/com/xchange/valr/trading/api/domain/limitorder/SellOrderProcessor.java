package com.xchange.valr.trading.api.domain.limitorder;

import com.xchange.valr.trading.api.domain.orderbook.OrderBook;

import java.math.BigDecimal;
import java.util.TreeMap;

public class SellOrderProcessor extends OrderProcessor {
  @Override
  protected TreeMap<BigDecimal, OrderBook.OrderBookLevel> getMatchingSide(OrderBook orderBook) {
    return orderBook.bids();
  }

  @Override
  protected TreeMap<BigDecimal, OrderBook.OrderBookLevel> getPlacementSide(OrderBook orderBook) {
    return orderBook.asks();
  }

  @Override
  protected boolean canMatch(BigDecimal sellPrice, BigDecimal bidPrice) {
    return sellPrice.compareTo(bidPrice) <= 0;
  }

  @Override
  protected OrderBook.OrderBookSide getOrderSide() {
    return OrderBook.OrderBookSide.SELL;
  }
}