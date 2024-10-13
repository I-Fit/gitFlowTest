package kr.co.ifit.db.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "membership")
public class Membership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "grade", nullable = false, length = 10)
    private String grade;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "benefit", nullable = false)
    private String benefit;

}