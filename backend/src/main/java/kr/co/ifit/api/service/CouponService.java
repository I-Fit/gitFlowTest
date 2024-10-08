package kr.co.ifit.api.service;

import jakarta.transaction.Transactional;
import kr.co.ifit.api.response.CouponDtoRes;
import kr.co.ifit.db.entity.Coupon;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.CouponRepository;
import kr.co.ifit.db.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserRepository userRepository;

    // 가입 시, 축하 쿠폰 추가
    @Transactional
    public void addWelcomeCoupon(User user) {
        if (user == null) {
            throw new IllegalArgumentException("사용자가 없습니다.");
        }
        try {
            Coupon coupon = new Coupon();
            coupon.setName("회원가입 축하 쿠폰 10%");
            coupon.setPercentage(10.0);
            coupon.setCreatedAt(LocalDateTime.now());

            // 쿠폰 만료일 설정 (30일)
            coupon.setExpiredAt(LocalDateTime.now().plusDays(30));
            coupon.setUser(user);
            couponRepository.save(coupon);

        } catch (Exception e) {
            throw new RuntimeException("쿠폰 생성 중 오류 발생: " + e.getMessage());
        }
    }

    // 사용자 쿠폰 조회 (couponDtoRes 반환)
    public List<CouponDtoRes> getUserCoupons(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("사용자가 없습니다."));
        List<Coupon> coupons = couponRepository.findByUser(user);
        return coupons.stream()
                .map(coupon -> new CouponDtoRes(
                        coupon.getName(),
                        coupon.getExpiredAt()
                ))
                .collect(Collectors.toList());
    }
}