package com.xchange.valr.api.model;

import lombok.Builder;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Builder
@Jacksonized
public record ApiError(
  String code,
  String status,
  String message,
  Detail details
) {

  public ApiError {
    Objects.requireNonNull(code);
    Objects.requireNonNull(status);
    Objects.requireNonNull(message);
  }

  public static ApiError toApiError(
    String code,
    HttpStatus status,
    String message,
    Map<String, List<String>> details
  ) {
    return ApiError.builder()
      .code(code)
      .status(status.getReasonPhrase())
      .message(message)
      .details(details != null ? Detail.builder().errors(details).build() : null)
      .build();
  }

  @Builder
  @Jacksonized
  public record Detail(@Singular Map<String, List<String>> errors) {
  }
}
