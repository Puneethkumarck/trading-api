package com.xchange.valr.trading.api.application.limitorder;

import com.xchange.valr.api.model.LimitOrderRequestDto;
import com.xchange.valr.api.model.LimitOrderResponseDto;
import com.xchange.valr.trading.api.domain.limitorder.LimitOrderCommandHandler;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping("/api/v1/orders")
@Tag(name = "Limit Orders", description = "Operations related to limit orders")
@RequiredArgsConstructor
public class LimitOrderController {

  private final LimitOrderCommandHandler handler;
  private final LimitOrderDtoMapper mapper;

  @PostMapping
  public ResponseEntity<LimitOrderResponseDto> placeLimitOrder(
    @Valid @RequestBody LimitOrderRequestDto request
  ) {
    log.info("Received request to place limit order for pair: {}", request.pair());
    return ok(
      LimitOrderResponseDto.builder()
        .orderId(
          handler.handle(mapper.toCommand(request))
            .orElse(null)
        )
        .build()
    );
  }
}
