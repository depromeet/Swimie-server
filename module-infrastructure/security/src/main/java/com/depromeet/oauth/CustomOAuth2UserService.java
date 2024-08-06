package com.depromeet.oauth;

import com.depromeet.member.domain.Member;
import com.depromeet.member.domain.MemberRole;
import com.depromeet.member.port.out.persistence.MemberPersistencePort;
import com.depromeet.oauth.dto.MemberDto;
import com.depromeet.oauth.dto.OAuth2Response;
import com.depromeet.oauth.dto.response.GoogleResponse;
import com.depromeet.oauth.dto.response.KakaoResponse;
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
    private final MemberPersistencePort memberPersistencePort;

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

        String name = oAuth2Response.getName();
        String email = oAuth2Response.getEmail();
        String providerId = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        Member member = findByProviderIdOrSave(email, name, providerId);

        MemberDto memberDto =
                MemberDto.builder()
                        .id(member.getId())
                        .name(member.getName())
                        .email(member.getEmail())
                        .memberRole(member.getRole())
                        .providerId(providerId)
                        .build();
        return new CustomOAuth2User(memberDto);
    }

    private Member findByProviderIdOrSave(String email, String name, String providerId) {
        return memberPersistencePort
                .findByProviderId(providerId)
                .orElseGet(() -> memberPersistencePort.save(createMember(name, email, providerId)));
    }

    private Member createMember(String name, String email, String providerId) {
        return Member.builder()
                .name(name)
                .email(email)
                .role(MemberRole.USER)
                .providerId(providerId)
                .build();
    }
}
