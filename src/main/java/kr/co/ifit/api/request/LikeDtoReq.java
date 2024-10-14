package kr.co.ifit.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LikeDtoReq {

    private Long userId;
    private Long postId;
//    private boolean isHeartFilled;
}
