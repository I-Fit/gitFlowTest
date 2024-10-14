package kr.co.ifit.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CouponDtoRes {
    private String name;
    private LocalDateTime expiredAt;
}
