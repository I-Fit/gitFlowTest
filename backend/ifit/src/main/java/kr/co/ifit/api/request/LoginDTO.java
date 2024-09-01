package kr.co.ifit.api.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDTO {
    private String loginId;
    private String password;
}