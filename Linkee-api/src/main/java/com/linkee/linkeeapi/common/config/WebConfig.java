package com.linkee.linkeeapi.common.config;

import com.linkee.linkeeapi.common.service.LoggingInterceptor;
import org.springframework.context.annotation.Configuration;
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
}
