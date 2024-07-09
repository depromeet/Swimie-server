package com.depromeet.type.common;

import com.depromeet.type.ErrorType;

public enum CommonErrorType implements ErrorType {
  REQUEST_VALIDATION("COMMON_1", "입력 값 검증에 실패하였습니다"),
  INVALID_TYPE("COMMON_2", "잘못된 타입이 입력되었습니다"),
  INVALID_MISSING_HEADER("COMMON_3", "요청에 필요한 헤더값이 존재하지 않습니다"),
  INVALID_HTTP_REQUEST("COMMON_4", "허용되지 않는 문자열이 입력되었습니다"),
  METHOD_NOT_ALLOWED("COMMON_5", "잘못된 HTTP 메소드의 요청입니다"),
  INTERNAL_SERVER("COMMON_6", "알 수 없는 서버 에러가 발생했습니다");

  private final String code;
  private final String message;

  CommonErrorType(String code, String message) {
    this.code = code;
    this.message = message;
  }

  @Override
  public String getCode() {
    return this.code;
  }

  @Override
  public String getMessage() {
    return this.message;
  }
}
