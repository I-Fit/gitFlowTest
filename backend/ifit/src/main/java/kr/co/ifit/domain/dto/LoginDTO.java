package kr.co.ifit.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDTO {
    private String loginId;
    private String password;
}