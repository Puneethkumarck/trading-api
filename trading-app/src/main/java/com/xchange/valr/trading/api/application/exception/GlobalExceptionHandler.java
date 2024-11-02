package com.xchange.valr.trading.api.application.exception;

import com.xchange.valr.api.model.ApiError;
import com.xchange.valr.trading.api.domain.orderbook.LimitOrderAlreadyExistsException;
import com.xchange.valr.trading.api.domain.orderbook.OrderBookNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(OrderBookNotFoundException.class)
  public ResponseEntity<ApiError> handleOrderBookNotFoundException(OrderBookNotFoundException ex) {
    var error = ApiError.toApiError("ORDER_BOOK_NOT_FOUND", NOT_FOUND, ex.getMessage(), null);
    return ResponseEntity.status(NOT_FOUND).body(error);
  }

  @ExceptionHandler(LimitOrderAlreadyExistsException.class)
  public ResponseEntity<ApiError> handleLimitOrderIdAlreadyExistException(
    LimitOrderAlreadyExistsException ex
  ) {
    var error = ApiError.toApiError("ORDER_ID_ALREADY_EXIST", CONFLICT, ex.getMessage(), null);
    return ResponseEntity.status(CONFLICT).body(error);
  }

  @ExceptionHandler(BindException.class)
  @ResponseStatus(BAD_REQUEST)
  public ApiError handleMethodArgumentNotValidException(BindException ex) {
    return ApiError.builder()
      .code(String.valueOf(BAD_REQUEST.value()))
      .status(BAD_REQUEST.getReasonPhrase())
      .message(
        ex.getBindingResult()
          .getAllErrors()
          .stream()
          .map(DefaultMessageSourceResolvable::getDefaultMessage)
          .collect(Collectors.joining(","))
      )
      .build();
  }
}
