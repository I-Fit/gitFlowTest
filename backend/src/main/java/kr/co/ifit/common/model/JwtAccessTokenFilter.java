package kr.co.ifit.common.model;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.ifit.api.service.JwtUserDetailService;
import kr.co.ifit.common.auth.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
//  OncePerRequestFilter : 매 요청마다 한 번만 실행된다
public class JwtAccessTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtUserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // url 패턴 확인
        String uri = request.getRequestURI();
        if (uri.startsWith("/api/create-group")
                || uri.startsWith("/api/created") || uri.startsWith("/api/created/delete")  || uri.startsWith("/api/created/like")
                || uri.startsWith("/api/created/sort") || uri.startsWith("/api/created/search")
                || uri.startsWith("/api/joined") || uri.startsWith("/api/joined/delete")  || uri.startsWith("/api/joined/like")
                || uri.startsWith("/api/joined/sort") || uri.startsWith("/api/joined/search")
                || uri.startsWith("/api/liked") || uri.startsWith("/api/liked/sort") || uri.startsWith("/api/liked/search")
                || uri.startsWith("/api/user-info") || uri.startsWith("/api/home/like-group") || uri.startsWith("/api/join-group")
        ) {

            String token = resolveToken(request);

            if (token != null) {
                try {
                    if (jwtTokenProvider.validateToken(token, jwtTokenProvider.extractUsername(token))) {
                        UserDetails userDetails = userDetailService.loadUserByUsername(jwtTokenProvider.extractUsername(token));

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    } else {
                        // Token이 유효하지 않은 경우
                        try {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Token이 만료되었습니다.");
                        } catch (java.io.IOException e) {
                            throw new RuntimeException(e);
                        }
                        return;
                    }
                } catch (ExpiredJwtException e) {
                    // Token이 만료된 경우
                    try {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Access Token이 만료되었습니다.");
                    } catch (java.io.IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    return;
                }
            } else {
                try {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인 후 이용해주세요.");
                } catch (java.io.IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        try {
            filterChain.doFilter(request, response);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
