package kr.co.ifit.common.model;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.ifit.api.service.JwtUserDetailService;
import kr.co.ifit.common.auth.JwtTokenProvider;
import kr.co.ifit.db.entity.Token;
import kr.co.ifit.db.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtRefreshTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {

        String uri = request.getRequestURI();
        if (uri.startsWith("/api/refresh-token")) {
            // 헤더에서 토큰 추출
            String refreshToken = resolveToken(request);

            if (refreshToken != null) {
                String loginId = jwtTokenProvider.extractUsername(refreshToken);
                boolean isTokenValid = jwtTokenProvider.validateToken(refreshToken, loginId);

                //   리프레시 토큰이 유효한 경우, 데이터베이스에서 refreshToken을 찾는다.
                if (isTokenValid) {
                    Optional<Token> tokenOptional = tokenRepository.findByRefreshToken(refreshToken);

                    //   새로운 액세스 토큰을 생성하고 Json 형식으로 응답하기 때문에 filterChanin을 controller에서 호출할 필요가 없다.
                    if (tokenOptional.isPresent()) {
                        //   유효한 refreshToken이 데이터베이스에 존재하는 경우
                        String newAccessToken = jwtTokenProvider.generateToken(loginId);
                        response.setContentType("application/json");
                        response.getWriter().write("{\"accessToken\": \"" + newAccessToken + "\"}");
                        return;
                    } else {
                        // 데이터베이스에 토큰이 존재하지 않는 경우
                        sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "refreshToken이 데이터베이스에 존재하지 않습니다.");
                    }
                } else {
                    // 리프레시 토큰이 만료된 경우
                    sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "refreshToken이 만료되었습니다. 재로그인 해주세요.");
                }
            } else {
                // 토큰이 제공되지 않은 경우
                sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, "토큰을 주세요.");
            }
        } else {
            try {
                filterChain.doFilter(request, response);
            } catch (ServletException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("refresh-token");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
