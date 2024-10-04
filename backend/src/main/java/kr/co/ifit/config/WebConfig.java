package kr.co.ifit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")      // 모든 경로
                .allowedOrigins("http://localhost:8081")        // vue 앱의 주소
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")      // 허용할 http 메서드
                .allowCredentials(true);            //  쿠키를 포함한 요청 허용
    }

}
