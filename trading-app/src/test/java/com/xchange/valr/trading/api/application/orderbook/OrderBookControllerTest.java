package com.xchange.valr.trading.api.application.orderbook;

import static com.xchange.valr.trading.api.domain.model.CurrencyPair.BTCZAR;
import static com.xchange.valr.trading.fixtures.OrderBookFixtures.orderBook;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xchange.valr.api.model.ApiError;
import com.xchange.valr.api.model.OrderBookResponseDto;
import com.xchange.valr.trading.api.domain.orderbook.OrderBookNotFoundException;
import com.xchange.valr.trading.api.domain.orderbook.OrderBookQueryHandler;
import com.xchange.valr.trading.api.infrastructure.config.GlobalConfig;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OrderBookController.class)
@Import({OrderBookDtoMapperImpl.class, GlobalConfig.class})
class OrderBookControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private OrderBookQueryHandler orderBookQueryHandler;
  @Spy
  private final OrderBookDtoMapper mapper = new OrderBookDtoMapperImpl();
  @Autowired
  private ObjectMapper objectMapper;
  private static final String URI = "/api/v1/orders/{currencyPair}";

  @Test
  @SneakyThrows
  void shouldReturnOrderBookForValidPair() {
    // given
    given(orderBookQueryHandler.getOrderBook(BTCZAR.name())).willReturn(orderBook(BTCZAR.name()));

    // when
    var response =
      mockMvc
        .perform(get(URI, BTCZAR.name()))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse();

    // then
    var responseDto =
      objectMapper.readValue(response.getContentAsString(), OrderBookResponseDto.class);

    var expected = mapper.toDto(orderBook(BTCZAR.name()));

    assertThat(responseDto)
      .usingRecursiveComparison()
      .ignoringFields("lastChange")
      .isEqualTo(expected);
  }

  @Test
  @SneakyThrows
  void shouldReturnOrderBookForCurrencyInsensitivePair() {
    // given
    var currencyPair = "BTCZAR";
    var inputCurrencyPair = "btczar";
    given(orderBookQueryHandler.getOrderBook(inputCurrencyPair))
      .willReturn(orderBook(currencyPair));

    // when
    var response =
      mockMvc
        .perform(get(URI, inputCurrencyPair))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse();

    // then
    var responseDto =
      objectMapper.readValue(response.getContentAsString(), OrderBookResponseDto.class);

    var expected = mapper.toDto(orderBook(currencyPair));

    assertThat(responseDto)
      .usingRecursiveComparison()
      .ignoringFields("lastChange")
      .isEqualTo(expected);
  }

  @Test
  @SneakyThrows
  void shouldReturn404ForInvalidCurrencyPair() {
    // given
    var currencyPair = "BTCBBB";
    given(orderBookQueryHandler.getOrderBook(currencyPair))
      .willThrow(OrderBookNotFoundException.withCurrencyPair(currencyPair));

    // when
    var result =
      mockMvc
        .perform(get(URI, currencyPair))
        .andExpect(status().isNotFound())
        .andReturn()
        .getResponse();

    var expected =
      ApiError.toApiError(
        "ORDER_BOOK_NOT_FOUND",
        NOT_FOUND,
        "Order book not found for currency pair: %s".formatted(currencyPair),
        null
      );

    var responseDto = objectMapper.readValue(result.getContentAsString(), ApiError.class);

    assertThat(responseDto)
      .usingRecursiveComparison()
      .isEqualTo(expected);
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "  "})
  @SneakyThrows
  void shouldReturnNotFoundForEmptyCurrencyPair() {
    // given
    var currencyPair = "";

    // when
    mockMvc.perform(get(URI, currencyPair))
      .andExpect(status().isNotFound());
  }
}
