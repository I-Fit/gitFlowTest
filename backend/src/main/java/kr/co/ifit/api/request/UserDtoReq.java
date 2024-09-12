package kr.co.ifit.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserDtoReq {
    private String loginId;
    private String password;
    private String username;
    private String phoneNumber;
    private String email;
    private String enteredCode;
}