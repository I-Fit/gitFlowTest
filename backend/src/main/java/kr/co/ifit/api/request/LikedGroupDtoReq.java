package kr.co.ifit.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class LikedGroupDtoReq {
    private Long userId;
    private Long communityId;
    private boolean isHeartFilled;
}

