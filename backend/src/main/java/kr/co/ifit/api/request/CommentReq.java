package kr.co.ifit.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentReq {

    private Long postId;
    private String content;
    private String author;
    private LocalDateTime createdAt;

}
