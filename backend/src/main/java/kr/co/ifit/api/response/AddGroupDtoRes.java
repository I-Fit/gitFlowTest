package kr.co.ifit.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
public class AddGroupDtoRes {
    private Long communityId;
    private String message;
}
