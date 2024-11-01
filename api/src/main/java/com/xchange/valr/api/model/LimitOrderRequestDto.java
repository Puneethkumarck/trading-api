package com.xchange.valr.api.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Builder(toBuilder = true)
@Jacksonized
public record LimitOrderRequestDto(
  @NotNull(message = "order side is required")
  @Pattern(regexp = "^(BUY|SELL)$", message = "order side must be either BUY or SELL") String side,
  @NotNull(message = "quantity is required")
  @DecimalMin(value = "0.0", inclusive = false, message = "quantity must be greater than 0") BigDecimal quantity,
  @NotNull(message = "price is required")
  @DecimalMin(value = "0.0", inclusive = false, message = "price must be greater than 0") BigDecimal price,
  @NotBlank(message = "currency pair is required") String pair,
  Boolean postOnly,
  @Size(max = 50, message = "Customer order ID must not exceed 50 characters")
  @Pattern(regexp = "^[a-zA-Z0-9-]*$", message = "Customer order ID must be alphanumeric") String customerOrderId,
  @Pattern(regexp = "^(GTC|IOC|FOK)$", message = "timeInForce must be either GTC, IOC or FOK") String timeInForce
) {

  public LimitOrderRequestDto {
    if (postOnly == null) {
      postOnly = false;
    }
    timeInForce = timeInForce == null ? "GTC" : timeInForce;
  }
}
