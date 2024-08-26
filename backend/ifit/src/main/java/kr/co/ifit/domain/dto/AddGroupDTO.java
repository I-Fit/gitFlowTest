package kr.co.ifit.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class AddGroupDTO {
    private String title;
    private String topboxContext;
    private String sport;
    private String location;
    private Integer person;
    private LocalDateTime date;
    private Integer userId;
}
