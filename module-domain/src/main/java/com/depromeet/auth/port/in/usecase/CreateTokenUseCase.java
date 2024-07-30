package com.depromeet.auth.port.in.usecase;

import com.depromeet.auth.vo.AccessTokenInfo;
import com.depromeet.auth.vo.JwtToken;
import com.depromeet.member.domain.MemberRole;

public interface CreateTokenUseCase {
    JwtToken generateToken(Long memberId, MemberRole memberRole);

    AccessTokenInfo generateAccessToken(String refreshToken);
}
