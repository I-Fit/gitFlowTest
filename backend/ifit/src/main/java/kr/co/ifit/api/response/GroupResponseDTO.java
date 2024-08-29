package kr.co.ifit.api.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class GroupResponseDTO {
    private Integer communityId;
    private String title;
    private String topboxContent;
    private String sport;
    private String location;
    private Integer person;
    private LocalDateTime date;

    public GroupResponseDTO(Integer communityId, String title, String topboxContent, String sport, String location, Integer person, LocalDateTime date) {
        this.communityId = communityId;
        this.title = title;
        this.topboxContent = topboxContent;
        this.sport = sport;
        this.location = location;
        this.person = person;
        this.date = date;
    }

}
