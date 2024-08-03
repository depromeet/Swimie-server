package com.depromeet.auth.api;

import com.depromeet.auth.dto.request.GoogleLoginRequest;
import com.depromeet.auth.dto.request.KakaoLoginRequest;
import com.depromeet.auth.dto.response.JwtAccessTokenResponse;
import com.depromeet.auth.dto.response.JwtTokenResponse;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "소셜로그인(auth)")
public interface AuthApi {
    @Operation(summary = "구글 소셜로그인")
    ApiResponse<JwtTokenResponse> loginByGoogle(
            @Valid @RequestBody final GoogleLoginRequest request,
            HttpServletRequest httpServletRequest);

    @Operation(summary = "카카오 소셜로그인")
    ApiResponse<JwtTokenResponse> loginByKakao(
            @Valid @RequestBody final KakaoLoginRequest request,
            HttpServletRequest httpServletRequest);

    @Operation(summary = "Access 토큰 재발급 요청")
    ApiResponse<JwtAccessTokenResponse> reissueAccessToken(
            @RequestHeader("Authorization") String refreshToken);

    @Operation(summary = "로그아웃")
    ApiResponse<?> logout(@LoginMember Long memberId);
}
