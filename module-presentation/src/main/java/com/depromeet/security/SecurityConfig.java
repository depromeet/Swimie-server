package com.depromeet.security;

import com.depromeet.auth.service.JwtTokenService;
import com.depromeet.member.repository.MemberRepository;
import com.depromeet.security.filter.JwtAuthenticationFilter;
import com.depromeet.security.oauth.CustomOAuth2UserService;
import com.depromeet.security.oauth.handler.OAuth2FailureHandler;
import com.depromeet.security.oauth.handler.OAuth2SuccessHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenService jwtTokenService;
    private final MemberRepository memberRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> corsConfigurationSource());

        // csrf disable
        http.csrf(CsrfConfigurer::disable);

        // From 로그인 방식 disable
        http.formLogin(FormLoginConfigurer::disable);

        // HTTP Basic 인증 방식 disable
        http.httpBasic(HttpBasicConfigurer::disable);

        // JWTFilter 추가
        http.addFilterAfter(jwtAuthenticationFilter(), OAuth2LoginAuthenticationFilter.class);

        // oauth2
        http.oauth2Login(
                oauth2 ->
                        oauth2.userInfoEndpoint(
                                        userInfoEndpointConfig ->
                                                userInfoEndpointConfig.userService(
                                                        customOAuth2UserService()))
                                .successHandler(oAuth2SuccessHandler())
                                .failureHandler(oAuth2FailureHandler()));

        // 경로별 인가 작업
        http.authorizeHttpRequests(
                authorize ->
                        authorize
                                .requestMatchers("/h2/**")
                                .permitAll()
                                .requestMatchers("/depromeet-actuator/**")
                                .permitAll() // actuator
                                .requestMatchers("/oauth2/**", "/login/**")
                                .permitAll() // oauth2
                                .requestMatchers("/swagger-ui/**", "/v3/**", "/favicon.ico")
                                .permitAll() // swagger
                                .requestMatchers("/api/v1/auth/**", "/api/login/**")
                                .permitAll() // 로그인 및 회원가입
                                .anyRequest()
                                .authenticated());

        // 세션 설정 : STATELESS
        http.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 예외 처리
        http.exceptionHandling(
                exception ->
                        exception.authenticationEntryPoint(
                                (request, response, authException) -> response.setStatus(401)));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "https://appu.o-r.kr"));
        configuration.setAllowedMethods(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenService);
    }

    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService(memberRepository);
    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(jwtTokenService, memberRepository);
    }

    @Bean
    public OAuth2FailureHandler oAuth2FailureHandler() {
        return new OAuth2FailureHandler();
    }
}
