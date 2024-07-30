package com.depromeet;

import com.depromeet.oauth.CustomOAuth2User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationUtilImpl implements AuthorizationUtil {
    @Override
    public Long getLoginId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomOAuth2User userDetails = (CustomOAuth2User) authentication.getPrincipal();

        return userDetails.getId();
    }
}
