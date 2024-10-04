package kr.co.ifit.common.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.access_expiration}")
    private Long accessExpiration;

    @Getter
    @Value("${jwt.refresh_expiration}")
    private Long refreshExpiration;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    // ECDSA 키 쌍 생성
    @PostConstruct
    public void init() throws Exception {
        File privateKeyFile = new File("privateKey.ser");
        File publicKeyFile = new File("publicKey.ser");

        if (privateKeyFile.exists() && publicKeyFile.exists()) {
            // 파일에서 키를 로드
            this.privateKey = loadPrivateKeyFromFile();
            this.publicKey = loadPublicKeyFromFile();
        } else {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            keyPairGenerator.initialize(256);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            this.privateKey = keyPair.getPrivate();
            this.publicKey = keyPair.getPublic();
            //  생성된 키를 파일에 저장
            saveKeyToFile(this.privateKey, this.publicKey);
        }
    }

    private void saveKeyToFile(PrivateKey privateKey, PublicKey publicKey) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("privateKey.ser"))) {
            out.writeObject(privateKey);
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("publicKey.ser"))) {
            out.writeObject(publicKey);
        }
    }

    private PrivateKey loadPrivateKeyFromFile() throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("privateKey.ser"))) {
            return (PrivateKey) in.readObject();
        }
    }

    private PublicKey loadPublicKeyFromFile() throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("publicKey.ser"))) {
            return (PublicKey) in.readObject();
        }
    }

    // Access Token 생성
    public String generateToken(String loginId) {
        return Jwts.builder()
                .setSubject(loginId)
                .setIssuedAt(new Date())            //  발급된 시간
                .setExpiration(new Date(System.currentTimeMillis() + accessExpiration))     //  만료 시간 설정
                //  ECDSA 알고리즘을 사용
                .signWith(privateKey, SignatureAlgorithm.ES256)    //  비밀 키 사용
                .compact();     //  문자열 상태로 압축하여 반환
    }

    // Refresh Token 생성
    public String generateRefreshToken(String loginId) {
        return Jwts.builder()
                .setSubject(loginId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpiration))
                //  ECDSA 알고리즘을 사용
                .signWith(privateKey, SignatureAlgorithm.ES256)    //  비밀 키 사용
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    //  사용자 이름 또는 loginId를 추출
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    //  JWT가 만료되었는지를 확인
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }


    public boolean validateToken(String token, String loginId) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(publicKey)
                    .build()
                    .parseClaimsJws(token);

            return (loginId.equals(extractUsername(token)) && !isTokenExpired(token));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
