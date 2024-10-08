package kr.co.ifit.api.request;

import kr.co.ifit.db.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDtoReq {

    private String title;
    private String content;
//    private String imageStr;
    private String exercise;
    private String location;
    private User user;
}
