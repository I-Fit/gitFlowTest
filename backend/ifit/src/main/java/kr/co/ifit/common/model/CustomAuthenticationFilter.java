//package kr.co.ifit.common.model;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import java.io.IOException;
//
//@RequiredArgsConstructor
//public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//    private final AuthenticationManager authenticationManager;
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        if ("/api/login".equals(request.getRequestURI()) && "POST".equals(request.getMethod())) {
//            try {
//                // Extract credentials from request
//                String loginId = request.getParameter("loginId");
//                String password = request.getParameter("password");
//
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginId, password);
//                return authenticationManager.authenticate(authToken);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//        // Return null if not a login request
//        return null;
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        // 인증 추가 후에 작업 작성
//        super.successfulAuthentication(request, response, chain, authResult);
//    }
//
//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//        // 인증 실패 후 작업 작성
//        super.unsuccessfulAuthentication(request, response, failed);
//    }
//}
