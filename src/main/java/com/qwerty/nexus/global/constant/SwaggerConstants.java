package com.qwerty.nexus.global.constant;

/**
 * Swagger 에서 사용하는 상수
 */
public final class SwaggerConstants {
    // 인스턴스 생성 방지
    private SwaggerConstants() {
        throw new UnsupportedOperationException("해당 클래스는 인스턴스 생성이 불가능합니다.");
    }

    public static final class Parameter {
        private Parameter(){};
        public static final String ADMIN_INIT = """
                {
                    "adminNm":"홍길동",
                    "adminId":"admin",
                    "adminPw":"admin",
                    "adminEmail":"harpuria87@gmail.com",
                    "adminPhone":"010-1234-5678"
                }
                """;
    }
}
