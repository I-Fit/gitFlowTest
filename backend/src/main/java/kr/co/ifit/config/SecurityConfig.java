package kr.co.ifit.config;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.ifit.api.service.JwtUserDetailService;
import kr.co.ifit.common.model.JwtAccessTokenFilter;
import kr.co.ifit.common.model.JwtRefreshTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAccessTokenFilter jwtAccessTokenFilter;
    private final JwtRefreshTokenFilter jwtRefreshTokenFilter;
    private final JwtUserDetailService userDetailService;

    @Bean
    // 비밀번호 암호화 처리
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                .requestMatchers("/api/user-account", "/api/check-id" ,"/api/find-id" ,"/api/password/check-id",
                                        "/api/password/emailVerification", "/api/password/modified", "/api/login", "/api/logout",
                                        "/api/sendVerificationCode", "/api/verifyEmail",
                                        "/api/filter/exercises", "/api/filter/exercises/sport", "/api/filter/location", "/api/filter/date", "/api/filter/time",
                                        "/api/search", "/api/sort", "/api/group-list",
                                        "/api/board/list","/api/board/search", "/api/board/sort", "/api/comments/post/**",
                                        "/api/board/delete"
                                        ).permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)  // 상태 비저장 세션
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint((request, response, authException) -> {       // 401 에러(인증 실패시)
                                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                                })
                                .accessDeniedHandler((request, response, accessDeniedException) -> {    // 403 에러(권한 부족 시)
                                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                                })
                );
//        //  Jwt 필터 추가
//        // 이렇게만 해놓으면 모든 요청에 대해서 필터를 적용
        http.addFilterBefore(jwtAccessTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtRefreshTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailService)
                .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}
