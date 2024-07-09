package com.depromeet.security;

import com.depromeet.auth.service.JwtTokenService;
import com.depromeet.member.repository.MemberRepository;
import com.depromeet.security.filter.JwtAuthenticationFilter;
import com.depromeet.security.jwt.util.JwtUtils;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtUtils jwtUtils;
  private final JwtTokenService jwtTokenService;
  private final MemberRepository memberRepository;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.httpBasic(HttpBasicConfigurer::disable)
        .formLogin(FormLoginConfigurer::disable)
        .cors(cors -> corsConfigurationSource())
        .csrf(CsrfConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
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
                    .requestMatchers("/api/v1/auth/**")
                    .permitAll() // 로그인 및 회원가입
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(
            exception ->
                exception.authenticationEntryPoint(
                    (request, response, authException) -> response.setStatus(401)))
        .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(jwtAuthenticationFilter(), OAuth2LoginAuthenticationFilter.class)
        .oauth2Login(
            oauth2 ->
                oauth2
                    .userInfoEndpoint(
                        userInfoEndpointConfig ->
                            userInfoEndpointConfig.userService(customOAuth2UserService()))
                    .redirectionEndpoint(
                        redirectionEndpointConfig ->
                            redirectionEndpointConfig.baseUri("/oauth2/callback/*"))
                    .successHandler(oAuth2SuccessHandler())
                    .failureHandler(oAuth2FailureHandler()));

    return http.build();
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(List.of("http://localhost:3000"));
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
    return new OAuth2SuccessHandler(jwtUtils, memberRepository);
  }

  @Bean
  public OAuth2FailureHandler oAuth2FailureHandler() {
    return new OAuth2FailureHandler();
  }
}
