package com.xchange.valr.trading.api.domain.limitorder;

import com.xchange.valr.trading.api.domain.orderbook.OrderBook;
import com.xchange.valr.trading.api.domain.orderbook.OrderBookRepository;
import com.xchange.valr.trading.api.domain.orderbook.OrderBookIdemPotencyValidator;
import com.xchange.valr.trading.api.domain.tradehistory.Trade;
import com.xchange.valr.trading.api.domain.tradehistory.TradeHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import static com.xchange.valr.trading.api.domain.limitorder.LimitOrderCommand.LimitOrder.OrderBookSide.BUY;
import static com.xchange.valr.trading.api.domain.limitorder.LimitOrderCommand.LimitOrder.OrderBookSide.SELL;
import static java.util.UUID.randomUUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class LimitOrderCommandHandler {
  private final OrderBookRepository orderBookRepository;
  private final TradeHistoryRepository tradeHistoryRepository;
  private final OrderBookIdemPotencyValidator idemPotencyValidator;

  private final Map<LimitOrderCommand.LimitOrder.OrderBookSide, OrderProcessor> processors = Map.of(
    BUY,
    new BuyOrderProcessor(),
    SELL,
    new SellOrderProcessor()
  );

  public Optional<String> handle(LimitOrderCommand command) {
    log.info("Processing limit order: {}", command);

    return idemPotencyValidator.validate(command)
      .flatMap(this::processOrder)
      .map(LimitOrderCommand::customerOrderId);
  }

  private Optional<LimitOrderCommand> processOrder(LimitOrderCommand command) {
    var orderBook = getOrCreateOrderBook(command.currencyPair());
    var processor = processors.get(command.limitOrder().side());

    processor.processOrder(command, orderBook, this::executeTrade);

    orderBookRepository.save(orderBook);
    return Optional.of(command);
  }

  private OrderBook getOrCreateOrderBook(String currencyPair) {
    return orderBookRepository.findByCurrencyPair(currencyPair)
      .orElseGet(
        () -> OrderBook.builder()
          .currencyPair(currencyPair)
          .asks(new TreeMap<>())
          .bids(new TreeMap<>())
          .build()
      );
  }

  private void executeTrade(LimitOrderCommand command, BigDecimal price, BigDecimal quantity) {
    var trade = Trade.builder()
      .id(randomUUID().toString())
      .currencyPair(command.currencyPair())
      .price(price)
      .quantity(quantity)
      .quoteVolume(price.multiply(quantity))
      .takerSide(command.limitOrder().side().name())
      .tradedAt(Instant.now())
      .build();

    tradeHistoryRepository.save(trade);
  }
}