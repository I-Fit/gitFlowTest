package kr.co.ifit.api.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class AddGroupRequestDTO {
    private String title;
    private String topboxContext;
    private String sport;
    private String location;
    private Integer person;
    private LocalDateTime date;
    private Long userId;
}
