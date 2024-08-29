package kr.co.ifit.api.response;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class AddGroupResponseDTO {
    private Integer communityId;
    private String message;

    public AddGroupResponseDTO(Integer communityId, String message) {
        this.communityId = communityId;
        this.message = message;
    }
}
