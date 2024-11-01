package com.xchange.valr.trading.api.application.tradehistory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xchange.valr.api.model.TradeHistoryResponseDto;
import com.xchange.valr.trading.api.domain.tradehistory.TradeHistoryQueryHandler;
import com.xchange.valr.trading.api.infrastructure.config.GlobalConfig;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static com.xchange.valr.trading.api.domain.model.CurrencyPair.BTCZAR;
import static com.xchange.valr.trading.api.domain.tradehistory.Trade.TakerSide.BUY;
import static com.xchange.valr.trading.fixtures.TradeFixtures.createTrade;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TradeHistoryController.class)
@Import({GlobalConfig.class, TradeHistoryDtoMapperImpl.class})
class TradeHistoryControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private TradeHistoryQueryHandler handler;
  @Spy
  private final TradeHistoryDtoMapper mapper = new TradeHistoryDtoMapperImpl();
  @Autowired
  private ObjectMapper objectMapper;
  private static final String TRADE_HISTORY_URI = "/api/v1/trades/{currencyPair}/history";

  @Test
  @SneakyThrows
  void shouldReturnTradeHistoryWithDefaultPageSize() {
    // given
    var trade = createTrade(BTCZAR.name(), BUY);
    var tradeDto = mapper.toDto(trade);
    var trades = List.of(trade);

    when(handler.getTradeHistory(BTCZAR.name(), 50)).thenReturn(Optional.of(trades));

    // when
    var response =
      mockMvc
        .perform(get(TRADE_HISTORY_URI, BTCZAR.name()))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse();

    var responseDtos =
      parseResponse(response, new TypeReference<List<TradeHistoryResponseDto>>() {
      });

    // then
    assertThat(responseDtos).usingRecursiveComparison().isEqualTo(List.of(tradeDto));
  }

  @Test
  @SneakyThrows
  void shouldReturnTradeHistoryWithCustomPageSize() {
    // given
    var trade = createTrade(BTCZAR.name(), BUY);
    var tradeDto = mapper.toDto(trade);
    var trades = List.of(trade);

    when(handler.getTradeHistory(BTCZAR.name(), 10)).thenReturn(Optional.of(trades));

    // when
    var response =
      mockMvc
        .perform(get(TRADE_HISTORY_URI, BTCZAR.name()).param("pageSize", "10"))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse();

    var responseDtos =
      parseResponse(response, new TypeReference<List<TradeHistoryResponseDto>>() {
      });

    // then
    assertThat(responseDtos).usingRecursiveComparison().isEqualTo(List.of(tradeDto));
  }

  @Test
    @SneakyThrows
    void shouldReturnEmptyTradeHistoryWhenNoTradesFound() {
        // given
        when(handler.getTradeHistory(BTCZAR.name(), 50)).thenReturn(Optional.empty());

        // when
        var response =
                mockMvc
                        .perform(get(TRADE_HISTORY_URI, BTCZAR.name()))
                        .andExpect(status().isOk())
                        .andReturn()
                        .getResponse();

        var responseDtos =
                parseResponse(response, new TypeReference<List<TradeHistoryResponseDto>>() {});

        // then
        assertThat(responseDtos).isEmpty();
    }

  @SneakyThrows
  private <T> T parseResponse(MockHttpServletResponse response, TypeReference<T> typeReference) {
    return objectMapper.readValue(response.getContentAsString(), typeReference);
  }
}
