package kr.co.ifit.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDTO {
    private String loginId;
    private String password;
    private String userName;
    private String phoneNumber;
    private String email;

}