package com.depromeet.domain.member.domain;

import static com.depromeet.global.dto.type.member.MemberErrorType.*;

import com.depromeet.global.converter.AbstractCodedEnumConverter;
import com.depromeet.global.converter.CodedEnum;
import com.depromeet.global.exception.NotFoundException;
import java.util.Arrays;

public enum MemberRole implements CodedEnum<String> {
	USER("USER"),
	ADMIN("ADMIN");

	private final String value;

	MemberRole(String value) {
		this.value = value;
	}

	@Override
	public String getValue() {
		return this.value;
	}

	public static MemberRole findByValue(String value) {
		return Arrays.stream(MemberRole.values())
				.filter(m -> m.value.equals(value))
				.findFirst()
				.orElseThrow(() -> new NotFoundException(NOT_FOUND));
	}

	@jakarta.persistence.Converter(autoApply = true)
	static class Converter extends AbstractCodedEnumConverter<MemberRole, String> {
		public Converter() {
			super(MemberRole.class);
		}
	}
}
