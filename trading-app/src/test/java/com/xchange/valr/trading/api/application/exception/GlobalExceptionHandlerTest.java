package com.xchange.valr.trading.api.application.exception;

import com.xchange.valr.api.model.ApiError;
import com.xchange.valr.trading.api.domain.orderbook.LimitOrderAlreadyExistsException;
import com.xchange.valr.trading.api.domain.orderbook.OrderBookNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;

import static com.xchange.valr.trading.api.domain.model.CurrencyPair.BTCZAR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {
  @InjectMocks
  private GlobalExceptionHandler globalExceptionHandler;

  @Test
  void handleOrderBookNotFoundException() {
    // given
    var exception = OrderBookNotFoundException.withCurrencyPair(BTCZAR.name());

    // when
    var result = globalExceptionHandler.handleOrderBookNotFoundException(exception);

    // then
    var expected =
      ResponseEntity.status(NOT_FOUND)
        .body(
          ApiError.toApiError(
            "ORDER_BOOK_NOT_FOUND",
            NOT_FOUND,
            exception.getMessage(),
            null
          )
        );

    assertThat(result).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void limitOrderAlreadyExistsException() {
    // given
    var exception = LimitOrderAlreadyExistsException.withOrderId("123");

    // when
    var result = globalExceptionHandler.handleLimitOrderIdAlreadyExistException(exception);

    // then
    var expected =
      ResponseEntity.status(CONFLICT)
        .body(
          ApiError.toApiError(
            "ORDER_ID_ALREADY_EXIST",
            CONFLICT,
            exception.getMessage(),
            null
          )
        );

    assertThat(result).usingRecursiveComparison().isEqualTo(expected);
  }

  @Test
  void handleMethodArgumentNotValidException() {
    // given
    var bindingResult = new BeanPropertyBindingResult(new Object(), "target");
    bindingResult.addError(new ObjectError("target", "currency pair must not be null"));
    var exception = new BindException(bindingResult);

    // when
    var result = globalExceptionHandler.handleMethodArgumentNotValidException(exception);

    // then
    var expected =
      ApiError.builder()
        .code(String.valueOf(BAD_REQUEST.value()))
        .status(BAD_REQUEST.getReasonPhrase())
        .message("currency pair must not be null")
        .build();

    assertThat(result).usingRecursiveComparison().isEqualTo(expected);
  }
}
