package kr.co.ifit.api.controller;

import kr.co.ifit.api.service.CouponService;
import kr.co.ifit.common.util.UserContextUtil;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.api.response.CouponDtoRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CouponController {

    private final CouponService couponService;
    private final UserContextUtil userContextUtil;

    @GetMapping("/coupon")
    public ResponseEntity<List<CouponDtoRes>> getUserCoupons() {
        Long userId = userContextUtil.getAuthenticatedUserId();
        if (userId == null) {
            // 인증되지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<CouponDtoRes> coupons = couponService.getUserCoupons(userId);
        return ResponseEntity.ok(coupons); // 쿠폰 리스트를 포함한 응답
    }
}
