package com.depromeet.auth.port.out;

import com.depromeet.auth.vo.AccessTokenInfo;
import com.depromeet.auth.vo.RefreshTokenInfo;
import com.depromeet.member.domain.MemberRole;
import java.util.Optional;

public interface SecurityPort {
    AccessTokenInfo generateAccessToken(Long memberId, MemberRole memberRole);

    RefreshTokenInfo generateRefreshToken(Long memberId, MemberRole memberRole);

    String findTokenType(String token);

    Optional<AccessTokenInfo> parseAccessToken(String token);

    Optional<RefreshTokenInfo> parseRefreshToken(String token);
}
