package com.depromeet.auth.service;

import com.depromeet.auth.dto.request.LoginDto;
import com.depromeet.auth.dto.response.JwtTokenResponseDto;
import com.depromeet.member.Member;
import com.depromeet.member.dto.request.MemberCreateDto;
import com.depromeet.member.service.MemberService;
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

  @Deprecated
  @Override
  public JwtTokenResponseDto login(LoginDto loginDto) {
    Member member = memberService.findByEmail(loginDto.email());

    return jwtTokenService.generateToken(member.getId(), member.getRole());
  }

  @Deprecated
  @Override
  public void signUp(MemberCreateDto memberCreateDto) {
    Member member = memberService.save(memberCreateDto);
  }
}
