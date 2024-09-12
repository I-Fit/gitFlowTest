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
public class PostReq {

    private String title;
    private String content;
    private String imageUrl;
    private String exercise;
    private String location;

    private String author;  // User user

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
