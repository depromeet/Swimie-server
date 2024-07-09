package com.depromeet.exception;

import com.depromeet.type.ErrorType;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends BaseException {
  public ForbiddenException(ErrorType errorType) {
    super(errorType, HttpStatus.FORBIDDEN);
  }
}
