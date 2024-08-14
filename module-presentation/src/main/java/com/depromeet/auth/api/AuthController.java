package com.depromeet.auth.api;

import com.depromeet.auth.dto.request.AppleLoginRequest;
import com.depromeet.auth.dto.request.GoogleLoginRequest;
import com.depromeet.auth.dto.request.KakaoLoginRequest;
import com.depromeet.auth.dto.response.JwtAccessTokenResponse;
import com.depromeet.auth.dto.response.JwtTokenResponse;
import com.depromeet.auth.facade.AuthFacade;
import com.depromeet.config.Logging;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.member.annotation.LoginMember;
import com.depromeet.type.auth.AuthSuccessType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthApi {
    public final AuthFacade authFacade;

    @PostMapping("/login/google")
    @Logging(item = "Auth", action = "POST")
    public ApiResponse<JwtTokenResponse> loginByGoogle(
            @Valid @RequestBody final GoogleLoginRequest request,
            HttpServletRequest httpServletRequest) {
        String origin = getOrigin(httpServletRequest);
        return ApiResponse.success(
                AuthSuccessType.LOGIN_SUCCESS, authFacade.loginByGoogle(request, origin));
    }

    @PostMapping("/login/kakao")
    @Logging(item = "Auth", action = "POST")
    public ApiResponse<JwtTokenResponse> loginByKakao(
            @Valid @RequestBody final KakaoLoginRequest request,
            HttpServletRequest httpServletRequest) {
        String origin = getOrigin(httpServletRequest);
        return ApiResponse.success(
                AuthSuccessType.LOGIN_SUCCESS, authFacade.loginByKakao(request, origin));
    }

    @PostMapping("/login/apple")
    @Logging(item = "Auth", action = "POST")
    public ApiResponse<JwtTokenResponse> loginByApple(
            @Valid @RequestBody final AppleLoginRequest request) {
        return ApiResponse.success(AuthSuccessType.LOGIN_SUCCESS, authFacade.loginByApple(request));
    }

    @PostMapping("/login/refresh")
    @Logging(item = "Auth", action = "POST")
    public ApiResponse<JwtAccessTokenResponse> reissueAccessToken(
            @RequestHeader("Authorization") String refreshToken) {
        return ApiResponse.success(
                AuthSuccessType.REISSUE_ACCESS_TOKEN_SUCCESS,
                authFacade.getReissuedAccessToken(refreshToken));
    }

    @DeleteMapping("/auth/delete")
    @Logging(item = "Auth", action = "DELETE")
    public ApiResponse<?> deleteAccount(@LoginMember Long memberId) {
        authFacade.deleteAccount(memberId);
        return ApiResponse.success(AuthSuccessType.DELETE_ACCOUNT_SUCCESS);
    }

    private static String getOrigin(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader("Origin");
    }
}
