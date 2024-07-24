package com.depromeet.auth.api;

import com.depromeet.auth.dto.request.GoogleLoginRequest;
import com.depromeet.auth.dto.request.KakaoLoginRequest;
import com.depromeet.auth.dto.response.JwtAccessTokenResponse;
import com.depromeet.auth.dto.response.JwtTokenResponseDto;
import com.depromeet.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Tag(name = "소셜로그인(auth)")
public interface AuthApi {
    @Operation(summary = "구글 소셜로그인")
    ApiResponse<JwtTokenResponseDto> loginByGoogle(
            @Valid @RequestBody final GoogleLoginRequest request);

    @Operation(summary = "카카오 소셜로그인")
    ApiResponse<JwtTokenResponseDto> loginByKakao(
            @Valid @RequestBody final KakaoLoginRequest request);

    @Operation(summary = "Access 토큰 재발급 요청")
    ApiResponse<JwtAccessTokenResponse> reissueAccessToken(
            @RequestHeader("Authorization") String refreshToken);
}
