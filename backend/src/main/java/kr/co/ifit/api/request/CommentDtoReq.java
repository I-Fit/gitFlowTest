package kr.co.ifit.api.request;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CommentDtoReq {

    private Long postId;
    private String content;
    private Long userId;
}
