package kr.co.ifit.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDtoRes {
    private String loginId;
    private String username;
    private String email;
    private String profileUrl;
    private int points;
    private String MembershipGrade;
}
