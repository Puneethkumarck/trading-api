package com.xchange.valr.trading.api.application.tradehistory;

import com.xchange.valr.api.model.TradeHistoryResponseDto;
import com.xchange.valr.trading.api.domain.tradehistory.TradeHistoryQueryHandler;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/trades")
@RequiredArgsConstructor
public class TradeHistoryController {

  private final TradeHistoryQueryHandler handler;

  private final TradeHistoryDtoMapper mapper;
  private static final int DEFAULT_PAGE_SIZE = 50;
  private static final int MAX_PAGE_SIZE = 100;

  @GetMapping("/{currencyPair}/history")
  public ResponseEntity<List<TradeHistoryResponseDto>> getTradeHistory(
    @PathVariable @NotEmpty String currencyPair,
    @RequestParam(required = false, defaultValue = DEFAULT_PAGE_SIZE + "")
    @Min(value = 1, message = "Page size must be greater than 0")
    @Max(value = MAX_PAGE_SIZE, message = "Page size must not exceed " + MAX_PAGE_SIZE) int pageSize
  ) {

    return ok(mapper.toDtoList(handler.getTradeHistory(currencyPair, pageSize).orElse(List.of())));
  }
}
