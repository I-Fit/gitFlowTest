package kr.co.ifit.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    private String loginId;
    private String password;
}