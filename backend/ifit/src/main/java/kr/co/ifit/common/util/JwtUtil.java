package kr.co.ifit.common.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Date;
import java.util.HashMap;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private Long expiration;

    //  비밀 키 생성 : HMAC 비밀 키를 생성
//    private SecretKey getSigningKey() {
//        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
//    }

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

    //  Jwt 토큰 생성
    public String generateToken(String loginId) {
        return Jwts.builder()
                .setSubject(loginId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
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
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    //  토큰 만료 여부 확인
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    //  토큰 유효성 검증
    public boolean validateToken(String token, String username) {
        try {
            return (username.equals(extractUsername(token)) && !isTokenExpired(token));
        } catch (Exception e) {
            return false;
        }
    }


}
