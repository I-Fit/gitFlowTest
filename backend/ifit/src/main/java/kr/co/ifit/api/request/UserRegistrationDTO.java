package kr.co.ifit.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationDTO {
    private String loginId;
    private String password;
    private String userName;
    private String phoneNumber;
    private String email;
}