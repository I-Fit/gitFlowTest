package kr.co.ifit.api.service;  // 패키지 선언

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.ifit.api.request.KakaoPayApproveDtoReq;
import kr.co.ifit.api.request.KakaoPayReadyDtoReq;
import kr.co.ifit.api.response.KakaoPayApproveDtoRes;
import kr.co.ifit.api.response.KakaoPayReadyDtoRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j  // Lombok의 로깅 기능 활성화
@Service  // 스프링 서비스 빈으로 등록
public class KakaoPayService {

    @Value("${kakao.pay.host}")  // application.properties에서 값 주입
    private String host;

    @Value("${kakao.pay.ready.url}")
    private String readyUrl;

    @Value("${kakao.pay.approve.url}")
    private String approveUrl;

    @Value("${kakao.secret.key}")
    private String secretKey;

    @Value("${kakao.pay.cid}")
    private String cid;

    private final RestTemplate restTemplate;  // REST API 호출을 위한 객체
    private final ObjectMapper objectMapper;  // JSON 변환을 위한 객체

    @Autowired  // 생성자 주입
    public KakaoPayService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    // 카카오페이 결제창 연결
    public KakaoPayReadyDtoRes readyToKakaoPay(KakaoPayReadyDtoReq request) {
        try {
            HttpHeaders headers = new HttpHeaders();  // HTTP 헤더 객체 생성
            headers.add("Authorization", "SECRET_KEY " + secretKey);  // 인증 헤더 추가
            headers.setContentType(MediaType.APPLICATION_JSON);  // JSON 형식으로 설정

            Map<String, Object> params = new HashMap<>();  // 요청 파라미터 맵 생성
            params.put("cid", cid);  // 가맹점 코드
            params.put("partner_order_id", UUID.randomUUID().toString());  // 주문 ID 생성
            params.put("partner_user_id", request.getPartnerUserId());  // 파트너 사용자 ID
            params.put("item_name", "iFit 멤버십 - " + request.getMembershipGrade());  // 상품명
            params.put("quantity", 1);  // 수량
            params.put("total_amount", request.getTotalAmount());  // 총 금액
            params.put("tax_free_amount", 0);  // 비과세 금액
            params.put("approval_url", "http://localhost:8080/api/payment/kakao/success");  // 성공 시 리다이렉트 URL
            params.put("cancel_url", "http://localhost:8080/api/payment/kakao/cancel");  // 취소 시 리다이렉트 URL
            params.put("fail_url", "http://localhost:8080/api/payment/kakao/fail");  // 실패 시 리다이렉트 URL

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);  // HTTP 요청 엔티티 생성

            log.info("Sending request to Kakao Pay. URL: {}, Headers: {}, Body: {}", readyUrl, headers, params);  // 요청 로깅

            KakaoPayReadyDtoRes response = restTemplate.postForObject(readyUrl, entity, KakaoPayReadyDtoRes.class);  // API 호출

            log.info("Received response from Kakao Pay: {}", response);  // 응답 로깅

            // partner_order_id를 응답에 포함
            if (response != null) {
                response.setPartner_order_id((String) params.get("partner_order_id"));
            }
            return response;  // 응답 반환
        } catch (Exception e) {  // HTTP 클라이언트 에러 처리
            log.error("Error in readyToKakaoPay", e);
            throw new RuntimeException("카카오페이 결제 준비 중 오류가 발생했습니다.", e);
        }
    }

    // 카카오페이 결제 승인
    public KakaoPayApproveDtoRes approveKakaoPay(KakaoPayApproveDtoReq request) {
        try {
            HttpHeaders headers = new HttpHeaders();  // HTTP 헤더 객체 생성
            headers.add("Authorization", "SECRET_KEY " + secretKey);  // 인증 헤더 추가
            headers.setContentType(MediaType.APPLICATION_JSON);  // JSON 형식으로 설정

            Map<String, Object> params = new HashMap<>();  // 요청 파라미터 맵 생성
            params.put("cid", cid);  // 가맹점 코드
            params.put("tid", request.getTid());  // 거래 ID
            params.put("partner_order_id", request.getPartnerOrderId());  // 주문 ID
            params.put("partner_user_id", request.getPartnerUserId());  // 파트너 사용자 ID
            params.put("pg_token", request.getPgToken());  // PG 토큰

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);  // HTTP 요청 엔티티 생성

            log.info("Sending approval request to Kakao Pay. URL: {}, Headers: {}, Body: {}", approveUrl, headers, params);  // 요청 로깅

            KakaoPayApproveDtoRes response = restTemplate.postForObject(approveUrl, entity, KakaoPayApproveDtoRes.class);  // API 호출

            log.info("Received approval response from Kakao Pay: {}", response);  // 응답 로깅

            return response;  // 응답 반환
        } catch (HttpClientErrorException e) {  // HTTP 클라이언트 에러 처리
            log.error("Kakao Pay API error during approval. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("카카오페이 결제 승인 중 오류가 발생했습니다.", e);
        } catch (Exception e) {  // 기타 예외 처리
            log.error("Unexpected error during Kakao Pay approval process", e);
            throw new RuntimeException("카카오페이 결제 승인 중 예상치 못한 오류가 발생했습니다.", e);
        }
    }
}