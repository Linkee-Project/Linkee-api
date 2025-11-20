package com.linkee.linkeeapi.common.config;

import com.linkee.linkeeapi.common.service.LoggingInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggingInterceptor())
                .addPathPatterns("/**")     // 어떤 요청에 적용할지
                .excludePathPatterns("/css/", "/js/", "/images/"); // 제외할 패턴
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // Vue dev 서버 출처
                .allowedOrigins("http://localhost:5173")
                // 필요한 메서드만 열어도 되지만, 여기서는 기본 CRUD 모두 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false)
                .maxAge(3600);
    }
}
