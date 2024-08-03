package com.depromeet;

import com.depromeet.auth.service.JwtTokenService;
import com.depromeet.filter.JwtAuthenticationFilter;
import com.depromeet.filter.JwtExceptionFilter;
import com.depromeet.member.port.out.persistence.MemberPersistencePort;
import com.depromeet.oauth.CustomOAuth2UserService;
import com.depromeet.oauth.handler.OAuth2FailureHandler;
import com.depromeet.oauth.handler.OAuth2SuccessHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final MemberPersistencePort memberPersistencePort;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> corsConfigurationSource());

        // csrf disable
        http.csrf(CsrfConfigurer::disable);

        // From 로그인 방식 disable
        http.formLogin(FormLoginConfigurer::disable);

        // HTTP Basic 인증 방식 disable
        http.httpBasic(HttpBasicConfigurer::disable);

        // JWT Filter 추가
        http.addFilterAfter(jwtAuthenticationFilter(), OAuth2LoginAuthenticationFilter.class);

        // JWT Error Handle
        http.addFilterBefore(jwtExceptionFilter(), JwtAuthenticationFilter.class);

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
                                .requestMatchers("/login/kakao", "/login/google")
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
        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:3000",
                        "https://api.swimie.life",
                        "https://swimie.life",
                        "https://www.swimie.life"));
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.addExposedHeader("Authorization");

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
    public JwtExceptionFilter jwtExceptionFilter() {
        return new JwtExceptionFilter(new ObjectMapper());
    }

    @Bean
    public CustomOAuth2UserService customOAuth2UserService() {
        return new CustomOAuth2UserService(memberPersistencePort);
    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(jwtTokenService, memberPersistencePort);
    }

    @Bean
    public OAuth2FailureHandler oAuth2FailureHandler() {
        return new OAuth2FailureHandler();
    }
}
