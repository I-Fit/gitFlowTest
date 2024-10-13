package kr.co.ifit.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KakaoPayApproveDtoRes {
    private String aid;
    private String tid;
    private String cid;
    private String partner_order_id;
    private String partner_user_id;
    private String payment_method_type;
    private Amount amount;
    private String item_name;
    private String item_code;
    private String created_at;
    private String approved_at;



    public static class Amount {
        private int total;
        private int tax_free;
        private int vat;
        private int point;
        private int discount;

    }
}
