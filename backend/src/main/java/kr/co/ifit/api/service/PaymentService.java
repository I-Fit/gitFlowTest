package kr.co.ifit.api.service;

import kr.co.ifit.api.response.PaymentUserInfoDtoRes;
import kr.co.ifit.db.entity.Membership;
import kr.co.ifit.db.entity.Point;
import kr.co.ifit.db.entity.User;
import kr.co.ifit.db.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final PointRepository pointRepository;
    private final CouponRepository couponRepository;
    private final MembershipRepository membershipRepository;

    @Autowired
    public PaymentService(UserRepository userRepository,
                          PaymentRepository paymentRepository,
                          PointRepository pointRepository,
                          CouponRepository couponRepository, MembershipRepository membershipRepository) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.pointRepository = pointRepository;
        this.couponRepository = couponRepository;
        this.membershipRepository = membershipRepository;
    }

    @Transactional(readOnly = true)
    public PaymentUserInfoDtoRes getUserPaymentInfo(Long userId, int id) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        // Payment와 Membership 정보 조회
        //  클라이언트가 보내준 id에 대한 membership 정보 조회
        Membership membership = membershipRepository.findById((long) id).orElse(new Membership());

        PaymentUserInfoDtoRes.MembershipInfo membershipInfo = new PaymentUserInfoDtoRes.MembershipInfo(
                membership.getGrade(),
                membership.getPrice()
        );

        // Point 정보 조회 - 회원이 가지고 있는 point를 찾아서 모두 더해서 추출
        List<Point> pointsList = pointRepository.findByUser_UserId(userId);
        int totalPoints = pointsList.stream()
                .mapToInt(Point::getAmount)
                .sum();

        // 사용 가능한 쿠폰 목록 조회
        List<PaymentUserInfoDtoRes.CouponInfo> coupons = couponRepository.findByUser_UserIdAndIsUsedFalse(userId)
                .stream()
                .map(coupon -> new PaymentUserInfoDtoRes.CouponInfo(
                        coupon.getId(),
                        coupon.getName(),
                        coupon.getPercentage()))
                .collect(Collectors.toList());

        return new PaymentUserInfoDtoRes(
                user.getUsername(),
                membershipInfo,
                totalPoints,
                coupons
        );
    }
}

    /*public PaymentDtoRes processPayment(PaymentDtoReq paymentDtoReq) {
        String loginId = userContextUtil.getAuthenticatedLoginId();
        User user = userService.findByLoginId(loginId);
        if (user == null) {
            throw new RuntimeException("사용자를 찾을 수 없습니다.");
        }

        Membership newMembership = membershipRepository.findByGrade(paymentDtoReq.getMembershipGrade())
                .orElseThrow(() -> new RuntimeException("유효하지 않은 멤버십 등급입니다."));

        // 결제 로직 구현
        // 1. 쿠폰 적용
        if (paymentDtoReq.getCouponId() != null) {
            Coupon coupon = user.getCoupons().stream()
                    .filter(c -> c.getId().equals(paymentDtoReq.getCouponId()) && !c.getIsUsed())
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("유효한 쿠폰을 찾을 수 없습니다."));
            coupon.setIsUsed(true);
        }

        // 2. 포인트 사용 (User 엔티티에 points 필드가 없으므로 이 부분은 주석 처리)
        // if (user.getPoints() < paymentDtoReq.getPointsUsed()) {
        //     throw new IllegalArgumentException("사용 가능한 포인트를 초과했습니다.");
        // }
        // user.setPoints(user.getPoints() - paymentDtoReq.getPointsUsed());

        // 3. 결제 처리 (실제 결제 로직 구현 필요)

        // 4. 사용자 멤버십 업데이트 (User 엔티티에 membershipGrade 필드가 없으므로 이 부분은 주석 처리)
        // user.setMembershipGrade(newMembership.getGrade());

        userRepository.save(user);

        return new PaymentDtoRes(true, "결제가 성공적으로 처리되었습니다.");
    }

    // 사용자의 멤버십 등급을 결정하는 메서드
    private String determineMembershipGrade(User user) {
        // 현재 User 엔티티에 points 필드가 없으므로, 임시로 기본 등급을 반환
        return "SILVER";
    }
}*/