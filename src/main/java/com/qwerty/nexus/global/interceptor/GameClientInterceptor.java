package com.qwerty.nexus.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class GameClientInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientId = request.getHeader("X-CLIENT-ID");

        // 게임 클리이언트 아이디가 없는 경우 차단처리
        if(clientId == null || clientId.isBlank()){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"success\":false,\"message\":\"게임 클라이언트 아이디가 존재하지 않습니다.\"}");
            return false;
        }

        // TODO : GAME 테이블 조회해서 유효성 검사. 존재하면 true, 아니면 false

        return true;
    }
}
