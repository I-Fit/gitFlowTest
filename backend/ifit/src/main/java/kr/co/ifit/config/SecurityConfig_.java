//package kr.co.ifit.config;
//
//import kr.co.ifit.api.service.CustomUserDetailsService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//
////  이 클래스가 spring의 설정 클래스임을 나타냄
//@Configuration
////  spring security의 웹 보안 기능을 활성화
//@EnableWebSecurity
//public class SecurityConfig_ {
//
//    @Bean
//    // 비밀번호 암호화 처리
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    // HTTP 요청에 대한 보안 규칙 정의
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/api/sendVerificationCode", "/api/verifyEmail", "/api/user-account",
//                                "/api/login").permitAll()   // 이메일 인증, 회원가입, 로그인 허용
//                        .requestMatchers("/api/user-logout").authenticated() // 로그아웃은 인증된 사용자만 접근 가능
//                        .anyRequest().authenticated()                        // 그 외 모든 요청은 인증된 사용자만 접근 가능
//                )
//                .formLogin(form -> form
//                        .loginProcessingUrl("/api/login") // 로그인 처리 URL
//                        .defaultSuccessUrl("/", true)    // 로그인 성공 후 리다이렉트 URL
//                )
//                .logout(logout -> logout
//                        .logoutUrl("/api/user-logout")   // 로그아웃 URL
//                        .logoutSuccessUrl("/")            // 로그아웃 성공 후 리다이렉트 URL
//                        .invalidateHttpSession(true)      // 로그아웃 시 세션 무효화
//                        .deleteCookies("JSESSIONID")      // 로그아웃 시 쿠키 삭제
//                )
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED) // 세션 생성 정책 설정
//                )
//                .csrf(AbstractHttpConfigurer::disable // CSRF 보호 비활성화 (필요에 따라 설정)
//                );
//        return http.build();
//    }
//
////    @Bean
////    public CorsConfigurationSource corsConfigurationSource() {
////        CorsConfiguration configuration = new CorsConfiguration();
////        configuration.setAllowedOrigins(List.of("http://localhost:8081"));
////        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
////        configuration.setAllowCredentials(true);
////        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
////        configuration.setExposedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
////
////        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
////        source.registerCorsConfiguration("/**", configuration);
////        return source;
////    }
//
//    // 인증 관리자 설정 (인증 요청을 처리하는데 사용)
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//    @Bean
//    // 사용자 인증을 처리, 사용자 세부 정보를 제공하는 UserDetialsSevice와 PasswordEncoder를 설정
//    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder) {
//        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//        authProvider.setUserDetailsService(userDetailsService);
//        authProvider.setPasswordEncoder(passwordEncoder);
//        return authProvider;
//    }
//}
