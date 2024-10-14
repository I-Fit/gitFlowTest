package kr.co.ifit.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoPayApproveDtoReq {
    private String tid;
    private String partnerOrderId;
    private String partnerUserId;
    private String pgToken;

    public KakaoPayApproveDtoReq(String pgToken) {
        this.pgToken = pgToken;
    }
}
