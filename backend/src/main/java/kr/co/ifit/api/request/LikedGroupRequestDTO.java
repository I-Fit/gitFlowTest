package kr.co.ifit.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class LikedGroupRequestDTO {
    private Long communityId;
    private Long userId;
    private boolean isHeartFilled;
}

