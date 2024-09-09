package kr.co.ifit.common.model;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.ifit.api.service.JwtUserDetailService;
import kr.co.ifit.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
//  OncePerRequestFilter : 매 요청마다 한 번만 실행된다
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final JwtUserDetailService userDetailService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        // http 요청의 헤더
        String authorizationHeader = request.getHeader("Authorization");
        //  토큰 추출 : Bearer 다음의 문자열을 잘라내어 JWT 토큰을 추출
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            //  토큰에서 사용자 이름을 추출
            String username = jwtUtil.extractUsername(token);
            
            //  추출한 사용자의 이름이 null이 아니고, 현재 SecurityContext에 인증 정보가 없는 경우에 다음 단계 진행
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //  메서드 호출하여 사용자 정보를 로드
                UserDetails userDetails = userDetailService.loadUserByUsername(username);
                //  토큰의 유효성 검사, 유효한 경우에만 인증을 설정
                if (jwtUtil.validateToken(token, username)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    //  SecurityContext에 인증 정보를 설정
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        try {
            //  필터로 요청을 전달
            filterChain.doFilter(request, response);
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }
}
