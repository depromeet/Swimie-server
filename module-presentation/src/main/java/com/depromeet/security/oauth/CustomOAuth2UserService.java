package com.depromeet.security.oauth;

import com.depromeet.member.Member;
import com.depromeet.member.mapper.MemberMapper;
import com.depromeet.member.repository.MemberRepository;
import com.depromeet.security.oauth.dto.GoogleResponse;
import com.depromeet.security.oauth.dto.KakaoResponse;
import com.depromeet.security.oauth.dto.MemberDto;
import com.depromeet.security.oauth.dto.OAuth2Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response;
        if (registrationId.equals("kakao")) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            throw new OAuth2AuthenticationException(
                    "failed to resolve registration id " + registrationId);
        }

        String nickname = oAuth2Response.getName();
        String email = oAuth2Response.getEmail();

        Member member = findByEmailOrSave(email, nickname);

        MemberDto memberDto =
                MemberDto.builder()
                        .id(member.getId())
                        .name(nickname)
                        .email(email)
                        .memberRole(member.getRole())
                        .build();
        return new CustomOAuth2User(memberDto);
    }

    private Member findByEmailOrSave(String email, String nickname) {
        return memberRepository
                .findByEmail(email)
                .orElse(memberRepository.save(MemberMapper.from(nickname, email)));
    }
}
