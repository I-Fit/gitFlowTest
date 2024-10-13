package kr.co.ifit.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoPayReadyDtoRes {
    private String tid; // 결제 고유번호
    private String next_redirect_pc_url;    // web - 받는 결제 페이지
    private String partner_order_id;
}
