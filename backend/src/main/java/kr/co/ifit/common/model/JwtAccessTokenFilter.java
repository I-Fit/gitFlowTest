//package kr.co.ifit.common.model;
//
//import io.jsonwebtoken.ExpiredJwtException;
//import io.jsonwebtoken.io.IOException;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import kr.co.ifit.api.service.JwtUserDetailService;
//import kr.co.ifit.common.auth.JwtTokenProvider;
//import kr.co.ifit.common.util.JwtUtil;
//import kr.co.ifit.db.entity.User;
//import kr.co.ifit.db.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//@Component
//@RequiredArgsConstructor
////  OncePerRequestFilter : 매 요청마다 한 번만 실행된다
//public class JwtAccessTokenFilter extends OncePerRequestFilter {
//
//    private final JwtTokenProvider jwtTokenProvider;
//    private final JwtUserDetailService userDetailService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//        throws ServletException, IOException {
//
//        String token = request.getHeader("Authorization");
//        if (token != null && token.startsWith("Bearer ")) {
//            token = token.substring(7);
//        }
//
//        if (token != null) {
//            try {
//                if (jwtTokenProvider.validateToken(token, jwtTokenProvider.extractUsername(token))) {
//                    String username = jwtTokenProvider.extractUsername(token);
//                    UserDetails userDetails = userDetailService.loadUserByUsername(username);
//
//                    if (userDetails != null) {
//                        Authentication auth = new UsernamePasswordAuthenticationToken(
//                                userDetails, null, userDetails.getAuthorities());
//                        SecurityContextHolder.getContext().setAuthentication(auth);
//                    }
//                }
//            } catch (ExpiredJwtException e) {
//                // Handle expired token
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.setContentType("application/json");
//                try {
//                    response.getWriter().write("{\"message\": \"Access token expired\"}");
//                } catch (java.io.IOException ex) {
//                    throw new RuntimeException(ex);
//                }
//                return; // Early return to stop further processing
//            } catch (Exception e) {
//                // Handle other exceptions related to token validation
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.setContentType("application/json");
//                try {
//                    response.getWriter().write("{\"message\": \"Invalid access token\"}");
//                } catch (java.io.IOException ex) {
//                    throw new RuntimeException(ex);
//                }
//                return; // Early return to stop further processing
//            }
//        }
//        try {
//            filterChain.doFilter(request, response);
//        } catch (java.io.IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
