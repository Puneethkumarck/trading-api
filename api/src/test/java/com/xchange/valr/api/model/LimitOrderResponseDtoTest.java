package com.xchange.valr.api.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LimitOrderResponseDtoTest {

  @Test
  void testLimitOrderResponse() {
    // given
    var response = LimitOrderResponseDto.builder()
      .orderId("orderId")
      .build();
    // when
    assertEquals("orderId", response.orderId());
  }
}