package com.depromeet.auth.api;

import com.depromeet.auth.dto.request.GoogleLoginRequest;
import com.depromeet.auth.dto.request.KakaoLoginRequest;
import com.depromeet.auth.dto.response.JwtAccessTokenResponse;
import com.depromeet.auth.dto.response.JwtTokenResponse;
import com.depromeet.auth.facade.AuthFacade;
import com.depromeet.dto.response.ApiResponse;
import com.depromeet.type.auth.AuthSuccessType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/login")
public class AuthController implements AuthApi {
    public final AuthFacade authFacade;

    @PostMapping("/google")
    public ApiResponse<JwtTokenResponse> loginByGoogle(
            @Valid @RequestBody final GoogleLoginRequest request,
            HttpServletRequest httpServletRequest) {
        String origin = getOrigin(httpServletRequest);
        return ApiResponse.success(
                AuthSuccessType.LOGIN_SUCCESS, authFacade.loginByGoogle(request, origin));
    }

    @PostMapping("/kakao")
    public ApiResponse<JwtTokenResponse> loginByKakao(
            @Valid @RequestBody final KakaoLoginRequest request,
            HttpServletRequest httpServletRequest) {
        String origin = getOrigin(httpServletRequest);
        return ApiResponse.success(
                AuthSuccessType.LOGIN_SUCCESS, authFacade.loginByKakao(request, origin));
    }

    @PostMapping("/refresh")
    public ApiResponse<JwtAccessTokenResponse> reissueAccessToken(
            @RequestHeader("Authorization") String refreshToken) {
        return ApiResponse.success(
                AuthSuccessType.REISSUE_ACCESS_TOKEN_SUCCESS,
                authFacade.getReissuedAccessToken(refreshToken));
    }

    private static String getOrigin(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader("Origin");
    }
}
