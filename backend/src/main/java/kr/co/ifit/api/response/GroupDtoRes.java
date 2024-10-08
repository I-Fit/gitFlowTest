package kr.co.ifit.api.response;

import kr.co.ifit.db.entity.Group;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Setter
@Getter
@NoArgsConstructor
public class GroupDtoRes {
    private Long communityId;
    private String title;
    private String topboxContent;
    private String sport;
    private Integer key;
    private String fullLocation;
    private String location;
    private int person;
    private int peopleParticipation;
    private String date;
    private String username;
    private boolean saved;

    public GroupDtoRes(Long communityId, String title, String topboxContent, String sport, String location, String fullLocation, int person, int peopleParticipation, String date, User user, boolean saved) {
        this.communityId = communityId;
        this.title = title;
        this.topboxContent = topboxContent;
        this.sport = sport;
        this.location = location;   // 시, 군
        this.fullLocation = fullLocation; //  전체 주소
        this.person = person;
        this.peopleParticipation = peopleParticipation;
        this.date = date;
        this.username = user != null ? user.getUsername() : null;
        this.saved = saved;
    }

    public static GroupDtoRes convertToDto(Group group) {

        LocalDateTime dateTime = group.getDate();

        DateTimeFormatter customDate = DateTimeFormatter.ofPattern("yy.MM.dd (E) HH:mm", Locale.KOREAN);
        String formattedDate = dateTime.format(customDate);


        return new GroupDtoRes(
                group.getCommunityId(),
                group.getTitle(),
                group.getTopboxContent(),
                group.getSport(),
                group.getLocation(),
                group.getFullLocation(),
                group.getPerson(),
                group.getPeopleParticipation(),
                formattedDate,
                group.getUser(),
                group.isSaved());
    }
}
