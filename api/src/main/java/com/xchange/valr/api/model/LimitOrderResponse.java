package com.xchange.valr.api.model;

import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Builder
@Jacksonized
public record LimitOrderResponse(String orderId) {
}
