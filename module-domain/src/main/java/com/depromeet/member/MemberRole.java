package com.depromeet.member;

import com.depromeet.converter.AbstractCodedEnumConverter;
import com.depromeet.converter.CodedEnum;
import java.util.Arrays;

public enum MemberRole implements CodedEnum<String> {
  USER("USER"),
  ADMIN("ADMIN");

  private final String value;

  MemberRole(String value) {
    this.value = value;
  }

  public static MemberRole findByValue(String value) {
    return Arrays.stream(MemberRole.values())
        .filter(m -> m.value.equals(value))
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("변환할 수 없습니다"));
  }

  @Override
  public String getValue() {
    return this.value;
  }

  @jakarta.persistence.Converter(autoApply = true)
  static class Converter extends AbstractCodedEnumConverter<MemberRole, String> {
    public Converter() {
      super(MemberRole.class);
    }
  }
}
