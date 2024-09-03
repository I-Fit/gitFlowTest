package kr.co.ifit.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
public class GroupResponseDTO {
    private Long communityId;
    private String title;
    private String topboxContent;
    private String sport;
    private String location;
    private int person;
    private int peopleParticipation;
    private LocalDateTime date;
}
