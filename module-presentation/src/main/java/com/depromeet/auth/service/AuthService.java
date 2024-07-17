package com.depromeet.auth.service;

import com.depromeet.auth.dto.request.GoogleLoginRequest;
import com.depromeet.auth.dto.request.LoginDto;
import com.depromeet.auth.dto.response.JwtTokenResponseDto;
import com.depromeet.member.dto.request.MemberCreateDto;

public interface AuthService {
    JwtTokenResponseDto login(LoginDto loginDto);

    void signUp(MemberCreateDto memberCreateDto);

    JwtTokenResponseDto loginByGoogle(GoogleLoginRequest request);
}
