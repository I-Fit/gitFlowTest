package kr.co.ifit.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private int userId;

    @Column(name = "loginId")
    private int id;

    @Column(name = "password")
    private String password;

    @Column(name = "userName")
    private String name;

    @Column(name = "phoneNumber")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    @Column(name = "modifiedAt")
    private LocalDateTime modifiedAt;


    public User() {

    }

    public User(int id, String password, String name, String phone, String email) {
        this.id = id;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.createdAt = LocalDateTime.now();
        this.modifiedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onUpdate() {
        this.modifiedAt = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", id=" + id +
                ", password=" + password +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                ", modifiedAt=" + modifiedAt +
                '}';
    }
}
