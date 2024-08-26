package kr.co.ifit.domain.dto;

import java.time.LocalDateTime;

public class UserDTO {
    private int userId;
    private String id;
    private String password;
    private String name;
    private String phoneNumber;
    private String email;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    public UserDTO() {
    }

    public UserDTO(int userId, String id, String password, String name, String phoneNumber, String email, LocalDateTime createAt, LocalDateTime modifiedAt) {
        this.userId = userId;
        this.id = id;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public LocalDateTime getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(LocalDateTime modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
}
