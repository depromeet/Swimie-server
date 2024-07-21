package com.depromeet.auth.api;

import com.depromeet.auth.dto.request.AccessTokenDto;
import com.depromeet.auth.dto.request.GoogleLoginRequest;
import com.depromeet.auth.dto.request.KakaoLoginRequest;
import com.depromeet.auth.dto.response.JwtTokenResponseDto;
import com.depromeet.auth.dto.response.RefreshTokenDto;
import com.depromeet.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "소셜로그인(auth)")
public interface AuthApi {
    @Operation(summary = "구글 소셜로그인")
    ApiResponse<JwtTokenResponseDto> loginByGoogle(
            @Valid @RequestBody final GoogleLoginRequest request);

    @Operation(summary = "카카오 소셜로그인")
    ApiResponse<JwtTokenResponseDto> loginByKakao(
            @Valid @RequestBody final KakaoLoginRequest request);

    @Operation(summary = "만료된 AccessToken으로 RefreshToken 전달")
    ApiResponse<RefreshTokenDto> getRefreshToken(@RequestBody final AccessTokenDto request);
}
