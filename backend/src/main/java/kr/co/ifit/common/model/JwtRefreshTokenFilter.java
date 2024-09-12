//package kr.co.ifit.common.model;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import kr.co.ifit.api.service.JwtUserDetailService;
//import kr.co.ifit.common.auth.JwtTokenProvider;
//import kr.co.ifit.db.entity.Token;
//import kr.co.ifit.db.repository.TokenRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Optional;
//
//@Component
//@RequiredArgsConstructor
//public class JwtRefreshTokenFilter extends OncePerRequestFilter {
//
//    private final JwtTokenProvider jwtTokenProvider;
//    private final JwtUserDetailService userDetailService;
//    private final TokenRepository tokenRepository;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
//
//        String refreshToken = request.getHeader("refreshToken");
//        if (refreshToken != null && refreshToken.startsWith("Bearer ")) {
//            refreshToken = refreshToken.substring(7);
//
//            if (jwtTokenProvider.validateToken(refreshToken, jwtTokenProvider.extractUsername(refreshToken))) {
//                String loginId = jwtTokenProvider.extractUsername(refreshToken);
//
//                // Find the refresh token in the database
//                Optional<Token> tokenOptional = tokenRepository.findByRefreshToken(refreshToken);
//                if (tokenOptional.isPresent()) {
//                    UserDetails userDetails = userDetailService.loadUserByUsername(loginId);
//                    if (userDetails != null) {
//                        Authentication auth = new UsernamePasswordAuthenticationToken(
//                                userDetails,
//                                null,
//                                userDetails.getAuthorities()
//                        );
//                        SecurityContextHolder.getContext().setAuthentication(auth);
//
//                        // Generate new access token
//                        String newAccessToken = jwtTokenProvider.generateToken(loginId);
//
//                        // Send new access token in response
//                        response.setContentType("application/json");
//                        response.getWriter().write("{\"accessToken\": \"" + newAccessToken + "\"}");
//                        return;
//                    }
//                }
//            }
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            response.setContentType("application/json");
//            response.getWriter().write("{\"message\": \"Invalid refresh token\"}");
//        }
//    }
//}
