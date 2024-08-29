package kr.co.ifit.api.request;

import lombok.Getter;
import lombok.Setter;

// 클라이언트와 서버 간 데이터 전송을 위한 객체
// 클라이언트에게 communityId, userId를 받아오는데 사용된다
// getter, setter 를 자동으로 생성
@Setter
@Getter
public class JoinedGroupRequestDTO {
    private Integer communityId;
    private Integer userId;

    public JoinedGroupRequestDTO() {
    }

    public JoinedGroupRequestDTO(Integer communityId, Integer userId) {
        this.communityId = communityId;
        this.userId = userId;
    }

}
