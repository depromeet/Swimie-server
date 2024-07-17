package com.depromeet.auth.service;

import com.depromeet.auth.dto.request.GoogleLoginRequest;
import com.depromeet.auth.dto.request.LoginDto;
import com.depromeet.auth.dto.response.GoogleAccountProfileResponse;
import com.depromeet.auth.dto.response.JwtTokenResponseDto;
import com.depromeet.auth.util.GoogleClient;
import com.depromeet.member.Member;
import com.depromeet.member.dto.request.MemberCreateDto;
import com.depromeet.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final MemberService memberService;
    private final JwtTokenService jwtTokenService;
    private final GoogleClient googleClient;

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

    @Override
    public JwtTokenResponseDto loginByGoogle(GoogleLoginRequest request) {
        final GoogleAccountProfileResponse profile = googleClient.getGoogleAccountProfile(request.code());
        final Member member = memberService.findOrCreateMemberBy(profile);
        return jwtTokenService.generateToken(member.getId(), member.getRole());
    }
}
