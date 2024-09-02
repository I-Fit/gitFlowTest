package kr.co.ifit.api.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CreatedGroupRequestDTO {
    private Long communityId;
    private Long userId;

}
