package kr.co.ifit.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoPayReadyDtoReq {
    private String partnerOrderId;
    private String partnerUserId;
    private String itemName;
    private String membershipGrade;
    private Integer quantity;
    private Integer totalAmount;
    private Integer taxFreeAmount;
    private String approvalUrl;
    private String cancelUrl;
    private String failUrl;
}
