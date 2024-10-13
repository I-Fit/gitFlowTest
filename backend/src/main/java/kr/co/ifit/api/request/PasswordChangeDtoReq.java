package kr.co.ifit.api.request;

import lombok.Data;

@Data
public class PasswordChangeDtoReq {
    private Long userId;
    private String loginId;
    private String email;
    private String currentPassword;
    private String password;
    private String passwordCheck;
    private String enteredCode;
}