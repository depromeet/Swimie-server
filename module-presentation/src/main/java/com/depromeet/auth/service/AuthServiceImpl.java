package com.depromeet.auth.service;

import com.depromeet.auth.dto.request.LoginDto;
import com.depromeet.auth.dto.response.JwtTokenResponseDto;
import com.depromeet.exception.ForbiddenException;
import com.depromeet.member.Member;
import com.depromeet.member.dto.request.MemberCreateDto;
import com.depromeet.member.service.MemberService;
import com.depromeet.type.auth.AuthErrorType;
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
            throw new ForbiddenException(AuthErrorType.LOGIN_FAILED);
        }

        return jwtTokenService.generateToken(member.getId(), member.getRole());
    }

    @Override
    public void signUp(MemberCreateDto memberCreateDto) {
        Member member = memberService.save(memberCreateDto);
    }
}
