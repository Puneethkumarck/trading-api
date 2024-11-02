package com.xchange.valr.trading.api.domain.limitorder;

import com.xchange.valr.trading.api.domain.orderbook.LimitOrderAlreadyExistsException;
import com.xchange.valr.trading.api.domain.orderbook.OrderBook;
import com.xchange.valr.trading.api.domain.orderbook.OrderBookIdemPotencyValidator;
import com.xchange.valr.trading.api.domain.orderbook.OrderBookRepository;
import com.xchange.valr.trading.api.domain.tradehistory.Trade;
import com.xchange.valr.trading.api.domain.tradehistory.TradeHistoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static com.xchange.valr.trading.api.domain.model.CurrencyPair.BTCZAR;
import static com.xchange.valr.trading.api.domain.orderbook.LimitOrderAlreadyExistsException.withOrderId;
import static com.xchange.valr.trading.fixtures.LimitOrderCommandFixtures.createLimitOrder;
import static com.xchange.valr.trading.fixtures.OrderBookFixtures.createOrderBook;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LimitOrderCommandHandlerTest {
  @Mock
  private OrderBookRepository orderBookRepository;
  @Mock
  private TradeHistoryRepository tradeHistoryRepository;
  @Mock
  private OrderBookIdemPotencyValidator validator;
  @InjectMocks
  private LimitOrderCommandHandler handler;
  @Captor
  private ArgumentCaptor<OrderBook> orderBookCaptor;
  @Captor
  private ArgumentCaptor<Trade> tradeCaptor;

  @Test
  void shouldRejectDuplicateOrder() {
    // given
    var command = createLimitOrder();
    when(validator.validate(command)).thenThrow(withOrderId(command.customerOrderId()));

    // when
    assertThatThrownBy(() -> handler.handle(command))
      .isInstanceOf(LimitOrderAlreadyExistsException.class)
      .hasMessageContaining(command.customerOrderId());

    verifyNoInteractions(orderBookRepository, tradeHistoryRepository);
  }

  @Test
  void shouldCreateNewOrderBookWithCorrectCurrencyPair() {
    // given
    var command = createLimitOrder();
    given(validator.validate(command)).willReturn(Optional.of(command));
    given(orderBookRepository.findByCurrencyPair(BTCZAR.name())).willReturn(Optional.empty());
    given(orderBookRepository.save(argThat(orderBook -> orderBook.currencyPair().equals(BTCZAR.name()))))
      .willAnswer(i -> Optional.of(i.getArgument(0)));

    // when
    handler.handle(command);

    // then
    verify(orderBookRepository).save(orderBookCaptor.capture());
    assertThat(orderBookCaptor.getValue().currencyPair()).isEqualTo(BTCZAR.name());
  }

  @Test
  void shouldProcessFullMatchBuyOrder() {
    var sellPrice = BigDecimal.valueOf(900000);
    var buyPrice = BigDecimal.valueOf(900000);
    var quantity = BigDecimal.ONE;

    var existingOrderBook = createOrderBook(sellPrice, quantity, BTCZAR.name(), OrderBook.OrderBookSide.SELL);
    var buyCommand = createLimitOrder(
      buyPrice,
      quantity,
      BTCZAR.name(),
      LimitOrderCommand.LimitOrder.OrderBookSide.BUY
    );

    setupMocks(buyCommand, existingOrderBook);

    handler.handle(buyCommand);

    verify(orderBookRepository).save(orderBookCaptor.capture());
    assertThat(orderBookCaptor.getValue().asks()).isEmpty();
  }

  @Test
  void shouldProcessPartialMatchBuyOrder() {
    var sellPrice = BigDecimal.valueOf(900000);
    var buyPrice = BigDecimal.valueOf(900000);
    var sellQuantity = BigDecimal.ONE;
    var buyQuantity = BigDecimal.valueOf(2);
    var expectedRemainingQuantity = buyQuantity.subtract(sellQuantity);

    var existingOrderBook = createOrderBook(sellPrice, sellQuantity, BTCZAR.name(), OrderBook.OrderBookSide.SELL);
    var buyCommand = createLimitOrder(
      buyPrice,
      buyQuantity,
      BTCZAR.name(),
      LimitOrderCommand.LimitOrder.OrderBookSide.BUY
    );

    setupMocks(buyCommand, existingOrderBook);

    handler.handle(buyCommand);

    verify(orderBookRepository).save(orderBookCaptor.capture());
    assertThat(orderBookCaptor.getValue().bids().get(buyPrice).quantity())
      .isEqualByComparingTo(expectedRemainingQuantity);
  }

  @Test
  void shouldNotMatchBuyOrderWhenPriceTooLow() {
    var sellPrice = BigDecimal.valueOf(900000);
    var buyPrice = BigDecimal.valueOf(800000);
    var quantity = BigDecimal.ONE;

    var existingOrderBook = createOrderBook(sellPrice, quantity, BTCZAR.name(), OrderBook.OrderBookSide.SELL);
    var buyCommand = createLimitOrder(
      buyPrice,
      quantity,
      BTCZAR.name(),
      LimitOrderCommand.LimitOrder.OrderBookSide.BUY
    );
    given(validator.validate(buyCommand)).willReturn(Optional.of(buyCommand));
    given(orderBookRepository.findByCurrencyPair(BTCZAR.name()))
      .willReturn(Optional.of(existingOrderBook));
    given(orderBookRepository.save(argThat(ob -> ob.currencyPair().equals(BTCZAR.name()))))
      .willAnswer(i -> Optional.of(i.getArgument(0)));

    handler.handle(buyCommand);

    verifyNoInteractions(tradeHistoryRepository);
  }

  @Test
  void shouldProcessFullMatchSellOrder() {
    var bidPrice = BigDecimal.valueOf(900000);
    var sellPrice = BigDecimal.valueOf(900000);
    var quantity = BigDecimal.ONE;

    var existingOrderBook = createOrderBook(bidPrice, quantity, BTCZAR.name(), OrderBook.OrderBookSide.BUY);
    var sellCommand = createLimitOrder(
      sellPrice,
      quantity,
      BTCZAR.name(),
      LimitOrderCommand.LimitOrder.OrderBookSide.SELL
    );

    setupMocks(sellCommand, existingOrderBook);

    handler.handle(sellCommand);

    verify(orderBookRepository).save(orderBookCaptor.capture());
    assertThat(orderBookCaptor.getValue().bids()).isEmpty();
  }

  @Test
  void shouldProcessPartialMatchSellOrder() {
    var bidPrice = BigDecimal.valueOf(900000);
    var sellPrice = BigDecimal.valueOf(900000);
    var bidQuantity = BigDecimal.ONE;
    var sellQuantity = BigDecimal.valueOf(2);
    var expectedRemainingQuantity = sellQuantity.subtract(bidQuantity);

    var existingOrderBook = createOrderBook(bidPrice, bidQuantity, BTCZAR.name(), OrderBook.OrderBookSide.BUY);
    var sellCommand = createLimitOrder(
      sellPrice,
      sellQuantity,
      BTCZAR.name(),
      LimitOrderCommand.LimitOrder.OrderBookSide.SELL
    );

    setupMocks(sellCommand, existingOrderBook);

    handler.handle(sellCommand);

    verify(orderBookRepository).save(orderBookCaptor.capture());
    assertThat(orderBookCaptor.getValue().asks().get(sellPrice).quantity())
      .isEqualByComparingTo(expectedRemainingQuantity);
  }

  @Test
  void shouldAggregateBuyOrdersAtSamePrice() {
    var price = BigDecimal.valueOf(900000);
    var quantity1 = BigDecimal.ONE;
    var quantity2 = BigDecimal.TWO;
    var expectedTotalQuantity = quantity1.add(quantity2);

    var existingOrderBook = createOrderBook(price, quantity1, BTCZAR.name(), OrderBook.OrderBookSide.BUY);
    var buyCommand = createLimitOrder(
      price,
      quantity2,
      BTCZAR.name(),
      LimitOrderCommand.LimitOrder.OrderBookSide.BUY
    );
    given(validator.validate(buyCommand)).willReturn(Optional.of(buyCommand));
    given(orderBookRepository.findByCurrencyPair(BTCZAR.name()))
      .willReturn(Optional.of(existingOrderBook));
    given(orderBookRepository.save(argThat(ob -> ob.currencyPair().equals(BTCZAR.name()))))
      .willAnswer(i -> Optional.of(i.getArgument(0)));

    handler.handle(buyCommand);

    verify(orderBookRepository).save(orderBookCaptor.capture());
    var savedLevel = orderBookCaptor.getValue().bids().get(price);

    assertThat(savedLevel.quantity())
      .isEqualByComparingTo(expectedTotalQuantity);
  }

  @Test
  void shouldAggregateSellOrdersAtSamePrice() {
    var price = BigDecimal.valueOf(900000);
    var quantity1 = BigDecimal.ONE;
    var quantity2 = BigDecimal.ONE;
    var expectedTotalQuantity = quantity1.add(quantity2);
    var expectedOrderCount = 2;

    var existingOrderBook = createOrderBook(price, quantity1, BTCZAR.name(), OrderBook.OrderBookSide.SELL);
    var sellCommand = createLimitOrder(
      price,
      quantity1,
      BTCZAR.name(),
      LimitOrderCommand.LimitOrder.OrderBookSide.SELL
    );

    given(validator.validate(sellCommand)).willReturn(Optional.of(sellCommand));
    given(orderBookRepository.findByCurrencyPair(BTCZAR.name()))
      .willReturn(Optional.of(existingOrderBook));
    given(orderBookRepository.save(argThat(ob -> ob.currencyPair().equals(BTCZAR.name()))))
      .willAnswer(i -> Optional.of(i.getArgument(0)));

    handler.handle(sellCommand);

    verify(orderBookRepository).save(orderBookCaptor.capture());
    var savedLevel = orderBookCaptor.getValue().asks().get(price);
    assertThat(savedLevel.quantity()).isEqualByComparingTo(expectedTotalQuantity);
    assertThat(savedLevel.orderCount()).isEqualTo(expectedOrderCount);
  }

  private void setupMocks(LimitOrderCommand command, OrderBook orderBook) {
    given(validator.validate(command)).willReturn(Optional.of(command));
    given(orderBookRepository.findByCurrencyPair(BTCZAR.name()))
      .willReturn(Optional.of(orderBook));
    given(orderBookRepository.save(argThat(ob -> ob.currencyPair().equals(BTCZAR.name()))))
      .willAnswer(i -> Optional.of(i.getArgument(0)));
    given(tradeHistoryRepository.save(argThat(trade -> trade.currencyPair().equals(BTCZAR.name()))))
      .willAnswer(i -> Optional.of(i.getArgument(0)));
  }
}