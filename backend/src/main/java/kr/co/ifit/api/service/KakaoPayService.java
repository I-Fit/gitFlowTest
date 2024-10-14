package kr.co.ifit.api.service;

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

@Slf4j
@Service
public class KakaoPayService {

    @Value("${kakao.pay.host}")
    private String host;

    @Value("${kakao.pay.ready.url}")
    private String readyUrl;

    @Value("${kakao.pay.approve.url}")
    private String approveUrl;

    @Value("${kakao.secret.key}")
    private String secretKey;

    @Value("${kakao.pay.cid}")
    private String cid;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private String latestTid;
    private String latestPartnerOrderId;
    private String latestPartnerUserId;

    @Autowired
    public KakaoPayService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public KakaoPayReadyDtoRes readyToKakaoPay(KakaoPayReadyDtoReq request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "SECRET_KEY " + secretKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> params = new HashMap<>();
            params.put("cid", cid);
            params.put("partner_order_id", UUID.randomUUID().toString());
            params.put("partner_user_id", request.getPartnerUserId());
            params.put("item_name", "iFit 멤버십 - " + request.getMembershipGrade());
            params.put("quantity", 1);
            params.put("total_amount", request.getTotalAmount());
            params.put("tax_free_amount", 0);
            params.put("approval_url", "http://localhost:8080/api/payment/kakao/success");
            params.put("cancel_url", "http://localhost:8080/api/payment/kakao/cancel");
            params.put("fail_url", "http://localhost:8080/api/payment/kakao/fail");

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);

            log.info("Sending request to Kakao Pay. URL: {}, Headers: {}, Body: {}", readyUrl, headers, params);

            KakaoPayReadyDtoRes response = restTemplate.postForObject(readyUrl, entity, KakaoPayReadyDtoRes.class);

            log.info("Received response from Kakao Pay: {}", response);

            if (response != null) {
                response.setPartner_order_id((String) params.get("partner_order_id"));
                saveTransactionInfo(response.getTid(), (String) params.get("partner_order_id"), request.getPartnerUserId());
            }
            return response;
        } catch (Exception e) {
            log.error("Error in readyToKakaoPay", e);
            throw new RuntimeException("카카오페이 결제 준비 중 오류가 발생했습니다.", e);
        }
    }

    public KakaoPayApproveDtoRes approveKakaoPay(KakaoPayApproveDtoReq request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "SECRET_KEY " + secretKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> params = new HashMap<>();
            params.put("cid", cid);
            params.put("tid", request.getTid());
            params.put("partner_order_id", request.getPartnerOrderId());
            params.put("partner_user_id", request.getPartnerUserId());
            params.put("pg_token", request.getPgToken());

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(params, headers);

            log.info("Sending approval request to Kakao Pay. URL: {}, Headers: {}, Body: {}", approveUrl, headers, params);

            KakaoPayApproveDtoRes response = restTemplate.postForObject(approveUrl, requestEntity, KakaoPayApproveDtoRes.class);

            log.info("Received approval response from Kakao Pay: {}", response);

            return response;
        } catch (HttpClientErrorException e) {
            log.error("Kakao Pay API error during approval. Status: {}, Response: {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("카카오페이 결제 승인 중 오류가 발생했습니다.", e);
        } catch (Exception e) {
            log.error("Unexpected error during Kakao Pay approval process", e);
            throw new RuntimeException("카카오페이 결제 승인 중 예상치 못한 오류가 발생했습니다.", e);
        }
    }

    private void saveTransactionInfo(String tid, String partnerOrderId, String partnerUserId) {
        this.latestTid = tid;
        this.latestPartnerOrderId = partnerOrderId;
        this.latestPartnerUserId = partnerUserId;
        // 여기에 tid, partnerOrderId, partnerUserId를 저장하는 추가 로직을 구현?
        // 예: 데이터베이스에 저장
    }

    // 새로 추가된 메서드들
    public String getLatestTid() {
        return this.latestTid;
    }

    public String getLatestPartnerOrderId() {
        return this.latestPartnerOrderId;
    }

    public String getLatestPartnerUserId() {
        return this.latestPartnerUserId;
    }
}