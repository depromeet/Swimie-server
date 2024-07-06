package com.depromeet.domain.auth.service;

import static com.depromeet.global.dto.type.auth.AuthErrorType.*;

import com.depromeet.domain.auth.dto.request.LoginDto;
import com.depromeet.domain.auth.dto.response.JwtTokenResponseDto;
import com.depromeet.domain.member.controller.port.MemberService;
import com.depromeet.domain.member.domain.Member;
import com.depromeet.domain.member.dto.request.MemberCreateDto;
import com.depromeet.global.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
	private final MemberService memberService;
	private final JwtTokenService jwtTokenService;

	@Override
	public JwtTokenResponseDto login(LoginDto loginDto) {
		Member member = memberService.findByEmail(loginDto.email());
		boolean pwMatch = memberService.matchPassword(loginDto.password(), member.getPassword());

		if (!pwMatch) {
			throw new ForbiddenException(LOGIN_FAILED);
		}

		return jwtTokenService.generateToken(member.getId(), member.getRole());
	}

	@Override
	public void signUp(MemberCreateDto memberCreateDto) {
		Member member = memberService.save(memberCreateDto);
	}
}
