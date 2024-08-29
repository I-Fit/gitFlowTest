package kr.co.ifit.api.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LikedGroupRequestDTO {
    private Integer communityId;
    private Integer userId;
    private boolean isHeartFilled;

    public LikedGroupRequestDTO() {
    }

    public LikedGroupRequestDTO(Integer communityId, Integer userId, boolean isHeartFilled) {
        this.communityId = communityId;
        this.userId = userId;
        this.isHeartFilled = isHeartFilled;
    }
}

