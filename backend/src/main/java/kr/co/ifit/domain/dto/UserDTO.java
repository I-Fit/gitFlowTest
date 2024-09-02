package kr.co.ifit.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class UserDTO {
    private Long userId;
    private String id;
    private String password;
    private String name;
    private String phoneNumber;
    private String email;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    public UserDTO() {
    }

    public UserDTO(Long userId, String id, String password, String name, String phoneNumber, String email, LocalDateTime createAt, LocalDateTime modifiedAt) {
        this.userId = userId;
        this.id = id;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
    }

}
