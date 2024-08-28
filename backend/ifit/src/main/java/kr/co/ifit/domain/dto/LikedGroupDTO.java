package kr.co.ifit.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LikedGroupDTO {
    private Integer communityId;
    private Integer userId;

    public LikedGroupDTO() {
    }

    public LikedGroupDTO(Integer communityId, Integer userId) {
        this.communityId = communityId;
        this.userId = userId;
    }
}

