package com.xchange.valr.trading.api.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CurrencyPair {
  BTCZAR("BTCZAR");

  public final String value;
}
