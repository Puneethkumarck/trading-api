package com.xchange.valr.trading.api.application.orderbook;

import com.xchange.valr.api.model.OrderBookResponseDto;
import com.xchange.valr.trading.api.domain.orderbook.OrderBookQueryHandler;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/orders")
@Validated
@RequiredArgsConstructor
public class OrderBookController {

  private final OrderBookQueryHandler orderBookQueryHandler;

  private final OrderBookDtoMapper mapper;

  @GetMapping("/{currencyPair}")
  public ResponseEntity<OrderBookResponseDto> getOrderBook(
    @PathVariable @NotEmpty String currencyPair
  ) {
    return ok(mapper.toDto(orderBookQueryHandler.getOrderBook(currencyPair)));
  }
}
