package com.qwerty.nexus.global.config.security;

import com.qwerty.nexus.global.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

/**
 * SecurityConfig
 * 시큐리티 관련 설정
 *
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf
                        .disable()) // CSRF 보호기능 비활성화 (REST API 는 JWT 토큰 사용하기 때문)
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // CORS 설정 추가
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 서버에서 세션 생성하지 않는 정책 (무상태)
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui.html").permitAll() // swagger 허용
                    //.requestMatchers(ApiConstants.Path.AUTH_PATH + "/**").permitAll() // 인증 관련 API 경로는 인증 없이 허용
                    //.requestMatchers(ApiConstants.Path.ADMIN_PATH + "/**").permitAll()  // 관리자 관련 API (추후 /initialize, /login 이거만 허용처리)
                    //.requestMatchers(ApiConstants.Path.ORG_PATH + "/**").permitAll()        // 단체 관련 API (나머지는 인증된 사용자만 할 수 있게)
                    //.requestMatchers(ApiConstants.Path.GAME_PATH + "/**").permitAll() // 게임 관련 API (얘도 마찬가지임)
                    //.anyRequest().authenticated() // 위에서 허용하지 않은 것들은 인증 필
                    .anyRequest().permitAll() // 전체 허용
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("*")); // 모든 origin 허용 (개발용)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        /* 추후 운영환경에서는 허용하는 도메인만 아래에 입력하면 됨
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",
                "https://yourdomain.com"
        ));
         */

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
