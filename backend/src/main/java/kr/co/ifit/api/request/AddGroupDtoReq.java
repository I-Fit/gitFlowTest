package kr.co.ifit.api.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class AddGroupDtoReq {
    private String title;
    private String topboxContent;
    private String sport;
    private String fullLocation;
    private String location;
    private int person;
    private LocalDateTime date;
    private Long userId;
    private boolean saved;
}
