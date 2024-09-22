package kr.co.ifit.api.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreatedGroupDtoReq {
    private Long userId;
    private Long communityId;

}
