package kr.co.ifit.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class PaymentUserInfoDtoRes {
    private String username;
    private MembershipInfo membershipInfo;
    private int points;
    private List<CouponInfo> coupons;

    public PaymentUserInfoDtoRes(String username, MembershipInfo membershipInfo, List<CouponInfo> coupons, int points) {
        this.username = username;
        this.membershipInfo = membershipInfo;
        this.points = points;
        this.coupons = coupons;
    }

    @Getter @Setter @AllArgsConstructor @NoArgsConstructor
    public static class MembershipInfo {
        private String grade;
        private int price;
    }

    @Getter
    @Setter @AllArgsConstructor
    @NoArgsConstructor
    public static class CouponInfo {
        private Long id;
        private String name;
        private double percentage;
    }
}
