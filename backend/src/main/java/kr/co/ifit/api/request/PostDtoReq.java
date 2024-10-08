package kr.co.ifit.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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
    private Long userId;
}
