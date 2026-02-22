package com.qwerty.nexus.global.config.web;

import com.qwerty.nexus.global.interceptor.GameClientInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final GameClientInterceptor gameClientInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 게임 클라이언트 인터셉터 등록
        registry.addInterceptor(gameClientInterceptor)
                .addPathPatterns("/api/v1/**") // 적용범위
                .excludePathPatterns("/api/v1/auth/**") // 예외 (로그인, 토큰 처리)
                .excludePathPatterns("/swagger-ui/**", "/v3/api-docs/**"); // 예외 (스웨거 관련)
    }
}
