package com.depromeet.type.member;

import com.depromeet.type.SuccessType;

public enum MemberSuccessType implements SuccessType {
  GET_SUCCESS("MEMBER_1", "멤버 조회에 성공하였습니다");

  private final String code;

  private final String message;

  MemberSuccessType(String code, String message) {
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
