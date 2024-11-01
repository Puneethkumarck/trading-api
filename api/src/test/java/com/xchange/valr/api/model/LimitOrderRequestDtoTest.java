package com.xchange.valr.api.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.Set;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;

class LimitOrderRequestDtoTest {

  private static final Validator VALIDATOR =
    Validation.buildDefaultValidatorFactory().getValidator();

  @Test
  void testValidLimitOrderRequestDto() {
    // given
    var request = validRequest().build();

    // when
    var violations = VALIDATOR.validate(request);

    // then
    assertThat(violations).isEmpty();
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "INVALID", "Buy", "Sell", "null"})
  void shouldValidateInvalidOrderSide(String side) {
    // given
    var request = validRequest().side(side).build();

    // when
    var violations = VALIDATOR.validate(request);

    // then
    assertSingleViolation(violations, "order side must be either BUY or SELL");
  }

  @ParameterizedTest
  @CsvSource(
    value = {
      "0.0, quantity must be greater than 0",
      "-1.0, quantity must be greater than 0",
      "null, quantity is required"
    },
    nullValues = {"null"})
  void shouldValidateQuantityGreaterThanZero(BigDecimal quantity, String expectedMessage) {
    // given
    var request = validRequest().quantity(quantity).build();

    // when
    var violations = VALIDATOR.validate(request);

    // then
    assertSingleViolation(violations, expectedMessage);
  }

  @ParameterizedTest
  @CsvSource(
    value = {
      "0.0, price must be greater than 0",
      "-1.0, price must be greater than 0",
      "null, price is required"
    },
    nullValues = {"null"})
  void shouldValidateInvalidPrice(BigDecimal price, String expectedMessage) {
    // given
    var request = validRequest().price(price).build();

    // when
    var violations = VALIDATOR.validate(request);

    // then
    assertSingleViolation(violations, expectedMessage);
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " "})
  void shouldValidateInvalidPair(String pair) {
    // given
    var request = validRequest().pair(pair).build();

    // when
    var violations = VALIDATOR.validate(request);

    // then
    assertSingleViolation(violations, "currency pair is required");
  }

  @ParameterizedTest
  @ValueSource(strings = {"customer_order_id", "customer@order", "customer order"})
  void shouldValidateInvalidCustomerOrderIdFormat(String customerOrderId) {
    // given
    var request = validRequest().customerOrderId(customerOrderId).build();

    // when
    var violations = VALIDATOR.validate(request);

    // then
    assertSingleViolation(violations, "Customer order ID must be alphanumeric");
  }

  @Test
  void shouldValidateCustomerOrderIdLength() {
    // given
    var request = validRequest().customerOrderId("a".repeat(51)).build();

    // when
    var violations = VALIDATOR.validate(request);

    // then
    assertSingleViolation(violations, "Customer order ID must not exceed 50 characters");
  }

  @ParameterizedTest
  @ValueSource(strings = {"", " ", "GTD", "FOC", "INVALID"})
  void shouldValidateInvalidTimeInForce(String timeInForce) {
    // given
    var request = validRequest().timeInForce(timeInForce).build();

    // when
    var violations = VALIDATOR.validate(request);

    // then
    assertSingleViolation(violations, "timeInForce must be either GTC, IOC or FOK");
  }

  @Test
  void shouldSetDefaultValues() {
    // given
    var request = validRequest().postOnly(null).timeInForce(null).build();

    // when/then
    assertThat(request.postOnly()).isFalse();
    assertThat(request.timeInForce()).isEqualTo("GTC");
  }

  private LimitOrderRequestDto.LimitOrderRequestDtoBuilder validRequest() {
    return LimitOrderRequestDto.builder()
      .side("BUY")
      .quantity(ONE)
      .price(TEN)
      .pair("BTCZAR")
      .postOnly(false)
      .customerOrderId("validId123")
      .timeInForce("GTC");
  }

  private void assertSingleViolation(
    Set<ConstraintViolation<LimitOrderRequestDto>> violations,
    String expectedMessage
  ) {
    assertThat(violations)
      .hasSize(1)
      .extracting(ConstraintViolation::getMessage)
      .containsExactly(expectedMessage);
  }
}
