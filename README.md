# Nexus

## 개요
Nexus는 게임 서비스 운영을 위한 백엔드 애플리케이션으로, 관리자와 유저, 게임 내 리소스를 통합적으로 관리할 수 있는 REST API를 제공합니다. 스프링 부트와 jOOQ를 기반으로 PostgreSQL 데이터베이스와 연동하며, JWT 기반 인증과 Swagger 문서화를 통해 운영자가 손쉽게 기능을 확인하고 연동할 수 있도록 구성했습니다.【F:build.gradle†L8-L61】【F:src/main/resources/application.properties†L3-L36】【F:src/main/java/com/qwerty/nexus/global/util/jwt/JwtUtil.java†L21-L197】【F:src/main/java/com/qwerty/nexus/global/config/swagger/SwaggerConfig.java†L22-L45】

## 기술 스택
- **언어 & 런타임**: Java 21, Spring Boot 3.5.0【F:build.gradle†L8-L22】
- **웹 & 시큐리티**: Spring Web, Spring Security, JWT(JSON Web Token) 필터 구성【F:build.gradle†L36-L60】【F:src/main/java/com/qwerty/nexus/global/config/security/SecurityConfig.java†L21-L48】【F:src/main/java/com/qwerty/nexus/global/util/jwt/JwtUtil.java†L21-L197】
- **데이터 액세스**: jOOQ 코드 생성기, PostgreSQL 드라이버 사용【F:build.gradle†L36-L60】【F:build.gradle†L63-L104】
- **문서화 & 도구**: springdoc-openapi, Swagger UI 설정, Lombok, Google OAuth 클라이언트【F:build.gradle†L39-L60】【F:src/main/resources/application.properties†L21-L29】【F:src/main/java/com/qwerty/nexus/global/config/swagger/SwaggerConfig.java†L22-L45】

## 주요 기능
- **관리자 관리**: 초기 관리자 생성, CRUD, 로그인 등 관리자 운영 기능을 제공합니다.【F:src/main/java/com/qwerty/nexus/domain/management/admin/controller/AdminController.java†L31-L144】
- **게임 정보 관리**: 게임 메타데이터 생성, 수정, 단건 조회 API를 제공합니다.【F:src/main/java/com/qwerty/nexus/domain/management/game/controller/GameController.java†L31-L82】
- **게임 유저 운영**: 유저 생성/수정, 정지, 탈퇴 처리 등 라이프사이클 제어 기능을 제공합니다.【F:src/main/java/com/qwerty/nexus/domain/game/user/controller/GameUserController.java†L35-L103】
- **재화 및 유저 재화 관리**: 재화 생성/수정/삭제 및 유저 보유 재화 연산 API를 제공합니다.【F:src/main/java/com/qwerty/nexus/domain/game/data/currency/controller/CurrencyController.java†L31-L98】【F:src/main/java/com/qwerty/nexus/domain/game/data/currency/controller/UserCurrencyController.java†L24-L95】
- **상품 관리 및 지급**: 상품 CRUD와 구매 처리 로직을 제공합니다.【F:src/main/java/com/qwerty/nexus/domain/game/product/controller/ProductController.java†L31-L82】
- **소셜 로그인 인증**: Google/Apple 토큰 검증을 포함한 소셜 로그인과 JWT 발급을 지원합니다.【F:src/main/java/com/qwerty/nexus/domain/auth/controller/AuthController.java†L35-L150】【F:src/main/java/com/qwerty/nexus/global/util/jwt/JwtUtil.java†L21-L197】
- **API 문서 & 보안 설정**: Swagger 기반 문서화와 JWT 인증 필터가 적용된 시큐리티 구성을 제공합니다.【F:src/main/java/com/qwerty/nexus/global/config/swagger/SwaggerConfig.java†L22-L45】【F:src/main/java/com/qwerty/nexus/global/config/security/SecurityConfig.java†L21-L48】

## 환경 설정 및 실행
1. PostgreSQL 데이터베이스를 준비하고 `application.properties`의 접속 정보와 JWT/Swagger 설정을 환경에 맞게 조정합니다.【F:src/main/resources/application.properties†L5-L36】
2. 의존성 설치 및 애플리케이션 실행
   ```bash
   ./gradlew bootRun
   ```
   또는 빌드 후 실행하려면 다음 명령을 사용할 수 있습니다.
   ```bash
   ./gradlew build
   java -jar build/libs/nexus-0.0.1-SNAPSHOT.jar
   ```
3. 애플리케이션이 시작되면 기본 포트 `9630`에서 API가 노출되며, Swagger UI는 `/swagger-ui.html` 경로에서 확인할 수 있습니다.【F:src/main/resources/application.properties†L3-L29】

## 참고
- jOOQ 코드 생성을 사용하므로 데이터베이스 스키마 변경 시 `jooq` 관련 Gradle 작업을 다시 실행해 생성된 코드를 최신화해야 합니다.【F:build.gradle†L63-L112】
- Swagger UI에서는 JWT 인증 스킴이 미리 정의되어 있어 토큰을 등록하고 보호된 API를 손쉽게 테스트할 수 있습니다.【F:src/main/java/com/qwerty/nexus/global/config/swagger/SwaggerConfig.java†L22-L45】
