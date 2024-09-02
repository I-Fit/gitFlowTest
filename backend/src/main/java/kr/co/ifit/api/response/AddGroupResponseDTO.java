package kr.co.ifit.api.response;

import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class AddGroupResponseDTO {
    private Long communityId;
    private String message;

    public AddGroupResponseDTO(Long communityId, String message) {
        this.communityId = communityId;
        this.message = message;
    }
}
