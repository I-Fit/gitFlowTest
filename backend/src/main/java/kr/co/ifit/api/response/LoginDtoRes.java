package kr.co.ifit.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginDtoRes {
    private String message;
    private String accessToken;
    private String refreshToken;
    private Long userId;
}
