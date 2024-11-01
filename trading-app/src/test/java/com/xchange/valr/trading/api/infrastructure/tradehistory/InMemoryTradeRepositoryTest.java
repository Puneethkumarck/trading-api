package com.xchange.valr.trading.api.infrastructure.tradehistory;

import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.UUID;

import static com.xchange.valr.trading.api.domain.model.CurrencyPair.BTCZAR;
import static com.xchange.valr.trading.fixtures.TradeEntityFixtures.createTradeEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

class InMemoryTradeRepositoryTest {
  private static final InMemoryTradeRepository repository = new InMemoryTradeRepository();

  @Test
  void shouldSaveAndRetrieveTrade() {
    // given
    var trade = createTradeEntity(BTCZAR.name(), "buy");

    // when
    var savedTrade = repository.save(trade);
    var retrievedTrade = repository.findRecentTradeByCurrencyPair(BTCZAR.name(), 1);

    // then
    assertThat(savedTrade).isPresent();
    assertThat(retrievedTrade)
      .isPresent()
      .hasValueSatisfying(
        trades -> {
          assertThat(trades).hasSize(1);
          assertThat(trades.get(0)).usingRecursiveComparison().isEqualTo(savedTrade.get());
        }
      );
  }

  @Test
  void shouldGenerateSequentialSequenceIds() {
    // given
    var trade1 = createTradeEntity(BTCZAR.name(), "buy");
    var trade2 = createTradeEntity(BTCZAR.name(), "sell");

    // when
    repository.save(trade1);
    repository.save(trade2);

    // then
    var retrievedTrades = repository.findRecentTradeByCurrencyPair(BTCZAR.name(), 2);
    assertThat(retrievedTrades)
      .isPresent()
      .hasValueSatisfying(
        trades -> {
          assertThat(trades)
            .hasSize(2)
            .satisfies(
              tradeList -> {
                var firstSequenceId = tradeList.get(0).sequenceId();
                var secondSequenceId = tradeList.get(1).sequenceId();
                assertThat(firstSequenceId).isGreaterThan(secondSequenceId);
              }
            );
        }
      );
  }

  @Test
  void shouldReturnEmptyOptionalWhenTradeNotFound() {
    // given
    var trade = createTradeEntity(BTCZAR.name(), "buy");

    // when
    var savedTrade = repository.save(trade);
    var retrievedTrade = repository.findRecentTradeByCurrencyPair("ETHZAR", 1);

    assertThat(retrievedTrade)
      .isPresent()
      .hasValueSatisfying(
        trades -> {
          assertThat(trades).isEmpty();
        }
      );
  }

  @Test
  void shouldReturnEmptyOptionalWhenNoTrades() {
    // when
    var retrievedTrade = repository.findRecentTradeByCurrencyPair("AAAAAAA", 1);

    assertThat(retrievedTrade)
      .isPresent()
      .hasValueSatisfying(
        trades -> {
          assertThat(trades).isEmpty();
        }
      );
  }

  @Test
  void shouldFindTradeByOrderId() {
    // given
    var tradeId = UUID.randomUUID().toString();
    var trade = createTradeEntity(BTCZAR.name(), "buy").toBuilder().id(tradeId).build();

    // when
    var result =
      repository.save(trade).flatMap(savedTrade -> repository.findTradesByOrderId(tradeId));

    // then
    assertThat(result)
      .isPresent()
      .hasValueSatisfying(
        trades -> {
          assertThat(trades)
            .usingRecursiveComparison()
            .ignoringFields("sequenceId", "tradedAt")
            .isEqualTo(trade);
        }
      );
  }

  @Test
  void shouldReturnTradesInDescendingOrderByTradedAt() {
    // given
    var oldestTrade = createTradeEntity(BTCZAR.name(), "buy");
    var middleTrade = createTradeEntity(BTCZAR.name(), "sell");
    var newestTrade = createTradeEntity(BTCZAR.name(), "buy");

    // when
    var savedOldest = repository.save(oldestTrade);

    await()
      .pollInterval(Duration.ofMillis(50))
      .atMost(Duration.ofSeconds(1))
      .until(() -> true);

    var savedMiddle = repository.save(middleTrade);

    await()
      .pollInterval(Duration.ofMillis(50))
      .atMost(Duration.ofSeconds(1))
      .until(() -> true);

    var savedNewest = repository.save(newestTrade);

    // then
    await()
      .atMost(Duration.ofSeconds(2))
      .pollInterval(Duration.ofMillis(100))
      .untilAsserted(() -> {
        var retrievedTrades = repository.findRecentTradeByCurrencyPair(BTCZAR.name(), 3);

        assertThat(retrievedTrades)
          .isPresent()
          .hasValueSatisfying(trades -> {
            assertThat(trades)
              .hasSize(3)
              .satisfies(tradeList -> {
                assertThat(tradeList.get(0).tradedAt())
                  .isAfterOrEqualTo(tradeList.get(1).tradedAt());
                assertThat(tradeList.get(1).tradedAt())
                  .isAfterOrEqualTo(tradeList.get(2).tradedAt());

                // Verify the order matches our save order
                assertThat(tradeList.get(0).id())
                  .isEqualTo(savedNewest.get().id());
                assertThat(tradeList.get(1).id())
                  .isEqualTo(savedMiddle.get().id());
                assertThat(tradeList.get(2).id())
                  .isEqualTo(savedOldest.get().id());
              });
          });
      });
  }
}