package kr.co.ifit.api.controller;

import kr.co.ifit.api.request.KakaoPayApproveDtoReq;
import kr.co.ifit.api.request.KakaoPayReadyDtoReq;
import kr.co.ifit.api.response.KakaoPayApproveDtoRes;
import kr.co.ifit.api.response.KakaoPayReadyDtoRes;
import kr.co.ifit.api.service.KakaoPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/payment/kakao")
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    @Autowired
    public KakaoPayController(KakaoPayService kakaoPayService) {
        this.kakaoPayService = kakaoPayService;
    }

    @PostMapping("/ready")
    public ResponseEntity<?> readyToKakaoPay(@RequestBody KakaoPayReadyDtoReq request) {
        try {
            log.info("Initiating Kakao Pay payment: {}", request);
            KakaoPayReadyDtoRes response = kakaoPayService.readyToKakaoPay(request);
            log.info("Kakao Pay payment initiated successfully: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error occurred while initiating Kakao Pay payment", e);
            return ResponseEntity.badRequest().body("결제 초기화 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @GetMapping("/success")
    public ResponseEntity<?> paymentSuccess(@RequestParam("pg_token") String pgToken) {
        try {
            log.info("Payment success callback received with pg_token: {}", pgToken);
            // KakaoPayService에서 저장된 tid와 다른 필요한 정보를 가져옵니다.
            String tid = kakaoPayService.getLatestTid();
            String partnerOrderId = kakaoPayService.getLatestPartnerOrderId();
            String partnerUserId = kakaoPayService.getLatestPartnerUserId();

            KakaoPayApproveDtoReq approveRequest = new KakaoPayApproveDtoReq(tid, partnerOrderId, partnerUserId, pgToken);
            KakaoPayApproveDtoRes approvalResponse = kakaoPayService.approveKakaoPay(approveRequest);

            log.info("Payment approved successfully: {}", approvalResponse);
            return ResponseEntity.ok(approvalResponse);
        } catch (Exception e) {
            log.error("Error approving payment", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error approving payment: " + e.getMessage());
        }
    }

    @PostMapping("/approve")
    public ResponseEntity<?> approveKakaoPay(@RequestBody KakaoPayApproveDtoReq request) {
        try {
            log.info("Approving Kakao Pay payment: {}", request);
            KakaoPayApproveDtoRes response = kakaoPayService.approveKakaoPay(request);
            log.info("Kakao Pay payment approved successfully: {}", response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error occurred while approving Kakao Pay payment", e);
            return ResponseEntity.badRequest().body("결제 승인 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    @GetMapping("/cancel")
    public ResponseEntity<?> paymentCancel() {
        log.info("Payment cancelled by user");
        // 여기에 결제 취소 시 처리 로직을 추가하세요
        return ResponseEntity.ok("결제가 취소되었습니다.");
    }

    @GetMapping("/fail")
    public ResponseEntity<?> paymentFail() {
        log.error("Payment failed");
        // 여기에 결제 실패 시 처리 로직을 추가하세요
        return ResponseEntity.badRequest().body("결제에 실패했습니다.");
    }
}