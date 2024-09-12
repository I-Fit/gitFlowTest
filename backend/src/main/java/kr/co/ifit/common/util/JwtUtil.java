package kr.co.ifit.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Date;
import java.util.HashMap;

@Component
public class JwtUtil {

    @Value("${jwt.access_expiration}")
    private Long accessExpiration;

    @Getter
    @Value("${jwt.refresh_expiration}")
    private Long refreshExpiration;

    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    // ECDSA 키 쌍 생성
    public JwtUtil() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
        keyPairGenerator.initialize(256);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    //  Jwt 토큰 생성(access token)
    public String generateToken(String loginId) {
        return Jwts.builder()
                .setSubject(loginId)
                .setIssuedAt(new Date())    // 발급된 시간
                .setExpiration(new Date(System.currentTimeMillis() + accessExpiration))     // 만료 시간
                //  ECDSA 알고리즘을 사용
                .signWith(privateKey, SignatureAlgorithm.ES256)    //  비밀 키 사용
                .compact();
    }

    //  토큰에서 Claims(사용자 정보) 추출
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    //  토큰에서 사용자 이름 추출
    public String extractUserId(String token) {
        return extractClaims(token).getSubject();
    }

    //  토큰 만료 여부 확인
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    //  토큰 유효성 검증
    public boolean validateToken(String token, String loginId) {
        try {
            return (loginId.equals(extractUserId(token)) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }
    
    // refresh token 생성
    public String generateRefreshToken(String loginId) {
        return Jwts.builder()
                .setSubject(loginId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(privateKey, SignatureAlgorithm.ES256)
                .compact();
    }

    //  refresh token에서 Claims(사용자 정보) 추출
    public Claims extractRefreshClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // refresh token 사용자 이름 추출
    public String extractRefreshUsername(String token) {
        return extractRefreshClaims(token).getSubject();
    }

    // refresh token 만료 여부 확인
    public boolean isRefreshTokenExpired(String token) {
        return extractRefreshClaims(token).getExpiration().before(new Date());
    }

    public boolean validateRefreshToken(String token, String loginId) {
        try {
            return (loginId.equals(extractRefreshUsername(token)) && !isRefreshTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }


}
