package com.xchange.valr.api.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LimitOrderResponseTest {

  @Test
  void testLimitOrderResponse() {
    // given
    var response = LimitOrderResponse.builder()
      .orderId("orderId")
      .build();
    // when
    assertEquals("orderId", response.orderId());
  }
}