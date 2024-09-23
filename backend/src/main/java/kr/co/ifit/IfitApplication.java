package kr.co.ifit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
// 스케줄링 기능 활성화
@EnableScheduling
public class IfitApplication {

	public static void main(String[] args) {
		SpringApplication.run(IfitApplication.class, args);
	}

}