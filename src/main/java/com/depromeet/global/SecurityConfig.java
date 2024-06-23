package com.depromeet.global;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .authorizeHttpRequests(
                        authorize ->
                                authorize
                                        .requestMatchers("/h2/**").permitAll()
                                        .requestMatchers("/depromeet-actuator/**").permitAll() // actuator
                                        .requestMatchers("/swagger-ui/**","/v3/**").permitAll() //swagger
                                        .requestMatchers("/auth/**").permitAll() //로그인 및 회원가입
                                        .requestMatchers("/v1/**").permitAll() // 임시
                                        .anyRequest().authenticated()
                );

        http.exceptionHandling(
                exception -> exception.authenticationEntryPoint(
                        (request, response, authException) -> response.setStatus(401)
                )
        );

        return http.build();
    }
}
