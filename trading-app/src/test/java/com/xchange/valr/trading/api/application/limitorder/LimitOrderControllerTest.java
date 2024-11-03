package com.xchange.valr.trading.api.application.limitorder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xchange.valr.trading.api.domain.limitorder.LimitOrderCommandHandler;
import com.xchange.valr.trading.api.infrastructure.config.GlobalConfig;
import com.xchange.valr.trading.api.infrastructure.config.SecurityConfig;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.xchange.valr.api.model.LimitOrderResponseDto;

import java.math.BigDecimal;
import java.util.Optional;

import static com.xchange.valr.trading.fixtures.LimitOrderRequestDtoFixtures.limitOrderRequestDto;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LimitOrderController.class)
@Import({LimitOrderDtoMapperImpl.class, GlobalConfig.class, SecurityConfig.class})
class LimitOrderControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private LimitOrderCommandHandler handler;
  @Spy
  private final LimitOrderDtoMapper mapper = new LimitOrderDtoMapperImpl();
  @Autowired
  private ObjectMapper objectMapper;
  private static final String URI = "/api/v1/orders";

  @Test
  @SneakyThrows
  @WithMockUser
  void shouldPlaceLimitOrderSuccessFully() {
    // given
    var request = limitOrderRequestDto();
    var expectedOrderId = randomAlphanumeric(50);
    var expected = LimitOrderResponseDto.builder().orderId(expectedOrderId).build();
    var command = mapper.toCommand(request);
    given(handler.handle(command)).willReturn(Optional.of(expectedOrderId));

    // when
    var response =
      mockMvc
        .perform(
          post(URI)
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
        )
        .andExpect(status().isOk())
        .andReturn()
        .getResponse();

    // then
    var responseDto =
      objectMapper.readValue(response.getContentAsString(), LimitOrderResponseDto.class);

    assertThat(responseDto).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  @SneakyThrows
  @WithMockUser
  void shouldReturnBadRequestWhenCurrencyPairIsNull() {
    // given
    var request = limitOrderRequestDto().toBuilder().pair(null).build();

    // when
    mockMvc
      .perform(
        post(URI)
          .contentType(APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request))
      )
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message", containsString("currency pair is required")));
  }

  @Test
  @SneakyThrows
  @WithMockUser
  void shouldReturnBadRequestWhenQuantityIsnull() {
    // given
    var request = limitOrderRequestDto().toBuilder().quantity(null).build();

    // when
    mockMvc
      .perform(
        post(URI)
          .contentType(APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request))
      )
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message", containsString("quantity is required")));
  }

  @Test
  @SneakyThrows
  @WithMockUser
  void shouldReturnBadRequestWhenPriceIsNull() {
    // given
    var request = limitOrderRequestDto().toBuilder().price(null).build();

    // when
    mockMvc
      .perform(
        post(URI)
          .contentType(APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request))
      )
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message", containsString("price is required")));
  }

  @Test
  @SneakyThrows
  @WithMockUser
  void shouldReturnBadRequestWhenSideIsNull() {
    // given
    var request = limitOrderRequestDto().toBuilder().side(null).build();

    // when
    mockMvc
      .perform(
        post(URI)
          .contentType(APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request))
      )
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message", containsString("order side is required")));
  }

  @Test
  @SneakyThrows
  @WithMockUser
  void shouldReturnBadRequestWhenSideIsInvalid() {
    // given
    var request = limitOrderRequestDto().toBuilder().side("INVALID").build();

    // when
    mockMvc
      .perform(
        post(URI)
          .contentType(APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request))
      )
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message", containsString("order side must be either BUY or SELL")));
  }

  @Test
  @SneakyThrows
  @WithMockUser
  void shouldReturnBadRequestWhenQuantityIsLessThanZero() {
    // given
    var request = limitOrderRequestDto().toBuilder().quantity(BigDecimal.valueOf(-1)).build();

    // when
    mockMvc
      .perform(
        post(URI)
          .contentType(APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request))
      )
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message", containsString("quantity must be greater than 0")));
  }

  @Test
  @SneakyThrows
  @WithMockUser
  void shouldReturnBadRequestWhenPriceIsLessThanZero() {
    // given
    var request = limitOrderRequestDto().toBuilder().price(BigDecimal.valueOf(-1)).build();

    // when
    mockMvc
      .perform(
        post(URI)
          .contentType(APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request))
      )
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message", containsString("price must be greater than 0")));
  }

  @Test
  @SneakyThrows
  @WithMockUser
  void shouldReturnBadRequestWhenCustomerOrderIdIsInvalid() {
    // given
    var request = limitOrderRequestDto().toBuilder().customerOrderId("INVALID$ORDER$ID").build();

    // when
    mockMvc
      .perform(
        post(URI)
          .contentType(APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request))
      )
      .andExpect(status().isBadRequest())
      .andExpect(jsonPath("$.message", containsString("Customer order ID must be alphanumeric")));
  }

  @Test
  @SneakyThrows
  @WithMockUser
  void shouldReturnBadRequestWhenCustomerOrderIdIsGreaterThan50Characters() {
    // given
    var request =
      limitOrderRequestDto().toBuilder().customerOrderId(randomAlphanumeric(51)).build();

    // when
    mockMvc
      .perform(
        post(URI)
          .contentType(APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request))
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        jsonPath(
          "$.message",
          containsString("Customer order ID must not exceed 50 characters")
        )
      );
  }

  @Test
  @SneakyThrows
  @WithMockUser
  void shouldReturnBadRequestWhenTimeInForceIsInvalid() {
    // given
    var request = limitOrderRequestDto().toBuilder().timeInForce("INVALID").build();

    // when
    mockMvc
      .perform(
        post(URI)
          .contentType(APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request))
      )
      .andExpect(status().isBadRequest())
      .andExpect(
        jsonPath("$.message", containsString("timeInForce must be either GTC, IOC or FOK"))
      );
  }

  @Test
  @SneakyThrows
  @WithMockUser
  void shouldNotReturnBadRequestWhenPostOnlyIsNull() {
    // given
    var request = limitOrderRequestDto().toBuilder().postOnly(null).build();

    // when
    mockMvc
      .perform(
        post(URI)
          .contentType(APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request))
      )
      .andExpect(status().isOk());
  }

  @Test
  @SneakyThrows
  @WithMockUser
  void shouldNotReturnBadRequestWhenTimeInForceIsNull() {
    // given
    var request = limitOrderRequestDto().toBuilder().timeInForce(null).build();

    // when
    mockMvc
      .perform(
        post(URI)
          .contentType(APPLICATION_JSON)
          .content(objectMapper.writeValueAsString(request))
      )
      .andExpect(status().isOk());
  }
}