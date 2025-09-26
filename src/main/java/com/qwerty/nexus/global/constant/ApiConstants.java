package com.qwerty.nexus.global.constant;

import java.time.Duration;
import java.util.List;

/**
 * API 에서 자주 사용되는 상수들 정의
 * 네이밍 규칙 :
 * - 대분류별로 내부 클래스 사용
 * - 모든 상수는 public static final 로 선언
 * - 상수명은 UPPER_SNAKE_CASE 로 명명
 */
public final class ApiConstants {

    // 인스턴스 생성 방지
    private ApiConstants() {
        throw new UnsupportedOperationException("해당 클래스는 인스턴스 생성이 불가능합니다.");
    }

    // =================================================================
    // API 버전 및 경로 관련
    // =================================================================

    public static final class Path {
        private Path() {}

        public static final String API_VERSION = "v1";
        public static final String API_BASE_PATH = "/api/" + API_VERSION;

        // 도메인별 기본 경로
        public static final String AUTH_PATH = API_BASE_PATH + "/auth";
        public static final String ADMIN_PATH = API_BASE_PATH + "/admin";
        public static final String ORG_PATH = API_BASE_PATH + "/org";
        public static final String GAME_PATH = API_BASE_PATH + "/game";
        public static final String GAME_USER_PATH = API_BASE_PATH + "/game-user";
        public static final String CURRENCY_PATH = API_BASE_PATH + "/currency";
        public static final String USER_CURRENCY_PATH = API_BASE_PATH + "/user-currency";

        public static final String GAME_TABLE_PATH = API_BASE_PATH + "/game-table";
        public static final String TABLE_COLUMN_PATH = API_BASE_PATH + "/table-column";
        public static final String USER_COLUMN_DATA_PATH = API_BASE_PATH + "/user-column-data";
    }

    // =================================================================
    // 페이징 관련 상수
    // =================================================================

    public static final class Pagination {
        private Pagination() {}

        public static final int DEFAULT_PAGE_SIZE = 20;
        public static final int MAX_PAGE_SIZE = 100;
        public static final int MIN_PAGE_SIZE = 1;
        public static final int DEFAULT_PAGE_NUMBER = 0; // Spring Data는 0부터 시작

        public static final String PAGE_PARAM = "page";
        public static final String SIZE_PARAM = "size";
        public static final String SORT_PARAM = "sort";

        // 정렬 방향
        public static final String SORT_ASC = "asc";
        public static final String SORT_DESC = "desc";

        // 기본 정렬 필드
        public static final String DEFAULT_SORT_FIELD = "createdAt";
        public static final String DEFAULT_SORT_DIRECTION = SORT_DESC;
    }

    // =================================================================
    // HTTP 헤더 관련
    // =================================================================

    public static final class Headers {
        private Headers() {}

        // 표준 헤더
        public static final String AUTHORIZATION = "Authorization";
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String ACCEPT = "Accept";
        public static final String USER_AGENT = "User-Agent";
        public static final String CACHE_CONTROL = "Cache-Control";
        public static final String ETAG = "ETag";
        public static final String LOCATION = "Location";

        // 커스텀 헤더
        public static final String API_VERSION_HEADER = "X-API-Version";
        public static final String REQUEST_ID_HEADER = "X-Request-ID";
        public static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
        public static final String CLIENT_VERSION_HEADER = "X-Client-Version";
        public static final String DEVICE_ID_HEADER = "X-Device-ID";
        public static final String PLATFORM_HEADER = "X-Platform";

        // JWT 관련
        public static final String BEARER_PREFIX = "Bearer ";
        public static final String REFRESH_TOKEN_HEADER = "X-Refresh-Token";

        // CORS 관련
        public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
        public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";
        public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";
    }

    // =================================================================
    // 미디어 타입 (Content-Type)
    // =================================================================

    public static final class MediaTypes {
        private MediaTypes() {}

        public static final String APPLICATION_JSON = "application/json";
        public static final String APPLICATION_XML = "application/xml";
        public static final String TEXT_PLAIN = "text/plain";
        public static final String TEXT_HTML = "text/html";
        public static final String MULTIPART_FORM_DATA = "multipart/form-data";
        public static final String APPLICATION_FORM_URLENCODED = "application/x-www-form-urlencoded";

        // 파일 관련
        public static final String APPLICATION_PDF = "application/pdf";
        public static final String IMAGE_JPEG = "image/jpeg";
        public static final String IMAGE_PNG = "image/png";
        public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

        // UTF-8 인코딩 포함
        public static final String APPLICATION_JSON_UTF8 = APPLICATION_JSON + ";charset=UTF-8";
        public static final String TEXT_PLAIN_UTF8 = TEXT_PLAIN + ";charset=UTF-8";
    }

    // =================================================================
    // 응답 메시지
    // =================================================================

    public static final class Messages {
        private Messages() {}

        // 성공 메시지
        public static final class Success {
            private Success() {}

            public static final String CREATED = "성공적으로 생성되었습니다";
            public static final String UPDATED = "성공적으로 수정되었습니다";
            public static final String DELETED = "성공적으로 삭제되었습니다";
            public static final String RETRIEVED = "성공적으로 조회되었습니다";
            public static final String PROCESSED = "요청이 성공적으로 처리되었습니다";

            // 도메인별 메시지
            public static final String USER_REGISTERED = "사용자 등록이 완료되었습니다";
            public static final String LOGIN_SUCCESS = "로그인이 완료되었습니다";
            public static final String LOGOUT_SUCCESS = "로그아웃이 완료되었습니다";
            public static final String PASSWORD_CHANGED = "비밀번호가 변경되었습니다";
            public static final String EMAIL_SENT = "이메일이 발송되었습니다";
            public static final String FILE_UPLOADED = "파일 업로드가 완료되었습니다";
        }

        // 에러 메시지 (ErrorCode와 중복을 피하기 위해 일반적인 것만)
        public static final class Error {
            private Error() {}

            public static final String INTERNAL_SERVER_ERROR = "서버 내부 오류가 발생했습니다";
            public static final String BAD_REQUEST = "잘못된 요청입니다";
            public static final String UNAUTHORIZED = "인증이 필요합니다";
            public static final String FORBIDDEN = "접근 권한이 없습니다";
            public static final String NOT_FOUND = "요청한 리소스를 찾을 수 없습니다";
            public static final String VALIDATION_FAILED = "입력값 검증에 실패했습니다";
        }
    }

    // =================================================================
    // 캐시 관련 상수
    // =================================================================

    public static final class Cache {
        private Cache() {}

        // 캐시 이름
        public static final String USER_CACHE = "users";
        public static final String PRODUCT_CACHE = "products";
        public static final String CONFIG_CACHE = "configs";

        // 캐시 TTL (초 단위)
        public static final long USER_CACHE_TTL = 3600; // 1시간
        public static final long PRODUCT_CACHE_TTL = 1800; // 30분
        public static final long CONFIG_CACHE_TTL = 86400; // 24시간

        // Cache-Control 값
        public static final String NO_CACHE = "no-cache";
        public static final String NO_STORE = "no-store";
        public static final String MAX_AGE_1_HOUR = "max-age=3600";
        public static final String MAX_AGE_1_DAY = "max-age=86400";
    }

    // =================================================================
    // 시간 관련 상수
    // =================================================================

    public static final class Time {
        private Time() {}

        // Duration 상수
        public static final Duration JWT_ACCESS_TOKEN_DURATION = Duration.ofHours(1);
        public static final Duration JWT_REFRESH_TOKEN_DURATION = Duration.ofDays(30);
        public static final Duration SESSION_TIMEOUT = Duration.ofMinutes(30);
        public static final Duration API_TIMEOUT = Duration.ofSeconds(30);
        public static final Duration FILE_UPLOAD_TIMEOUT = Duration.ofMinutes(5);

        // 밀리초 단위
        public static final long ONE_SECOND_MS = 1000L;
        public static final long ONE_MINUTE_MS = 60 * ONE_SECOND_MS;
        public static final long ONE_HOUR_MS = 60 * ONE_MINUTE_MS;
        public static final long ONE_DAY_MS = 24 * ONE_HOUR_MS;

        // 날짜 포맷
        public static final String DATE_FORMAT = "yyyy-MM-dd";
        public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
        public static final String ISO_DATETIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
        public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    }

    // =================================================================
    // 파일 관련 상수
    // =================================================================

    public static final class Files {
        private Files() {}

        // 파일 크기 제한 (바이트)
        public static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB
        public static final long MAX_IMAGE_SIZE = 5 * 1024 * 1024; // 5MB
        public static final long MAX_DOCUMENT_SIZE = 20 * 1024 * 1024; // 20MB

        // 허용 파일 확장자
        public static final List<String> ALLOWED_IMAGE_EXTENSIONS =
                List.of("jpg", "jpeg", "png", "gif", "webp");
        public static final List<String> ALLOWED_DOCUMENT_EXTENSIONS =
                List.of("pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt");

        // 파일 업로드 경로
        public static final String UPLOAD_BASE_PATH = "/uploads";
        public static final String IMAGE_UPLOAD_PATH = UPLOAD_BASE_PATH + "/images";
        public static final String DOCUMENT_UPLOAD_PATH = UPLOAD_BASE_PATH + "/documents";
        public static final String TEMP_UPLOAD_PATH = UPLOAD_BASE_PATH + "/temp";
    }

    // =================================================================
    // 보안 관련 상수
    // =================================================================

    public static final class Security {
        private Security() {}

        // 패스워드 정책
        public static final int MIN_PASSWORD_LENGTH = 8;
        public static final int MAX_PASSWORD_LENGTH = 128;
        public static final int MAX_LOGIN_ATTEMPTS = 5;
        public static final Duration ACCOUNT_LOCK_DURATION = Duration.ofMinutes(30);

        // JWT 관련
        public static final String JWT_SECRET_KEY_PROPERTY = "app.jwt.secret";
        public static final String JWT_ISSUER = "yourcompany-api";
        public static final String JWT_AUDIENCE = "yourcompany-client";

        // CORS 설정
        public static final List<String> ALLOWED_ORIGINS =
                List.of("http://localhost:3000", "https://yourdomain.com");
        public static final List<String> ALLOWED_METHODS =
                List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS");
        public static final List<String> ALLOWED_HEADERS =
                List.of("Authorization", "Content-Type", "X-Request-ID", "X-API-Version");
    }

    // =================================================================
    // 데이터베이스 관련 상수
    // =================================================================

    public static final class Database {
        private Database() {}

        // 테이블/컬럼 이름 길이 제한
        public static final int MAX_STRING_LENGTH = 255;
        public static final int MAX_TEXT_LENGTH = 65535;
        public static final int MAX_USERNAME_LENGTH = 50;
        public static final int MAX_EMAIL_LENGTH = 100;
        public static final int MAX_NAME_LENGTH = 100;

        // 기본값
        public static final String DEFAULT_LOCALE = "ko_KR";
        public static final String DEFAULT_TIMEZONE = "Asia/Seoul";
        public static final String DEFAULT_CURRENCY = "KRW";
    }

    // =================================================================
    // 비즈니스 로직 상수
    // =================================================================

    public static final class Business {
        private Business() {}

        // 주문 관련
        public static final int MAX_ORDER_ITEMS = 50;
        public static final long ORDER_TIMEOUT_MINUTES = 30;

        // 검색 관련
        public static final int MIN_SEARCH_KEYWORD_LENGTH = 2;
        public static final int MAX_SEARCH_KEYWORD_LENGTH = 100;
        public static final int MAX_SEARCH_RESULTS = 1000;

        // 이메일 관련
        public static final int EMAIL_VERIFICATION_CODE_LENGTH = 6;
        public static final Duration EMAIL_VERIFICATION_TIMEOUT = Duration.ofMinutes(30);

        // 기타
        public static final String DEFAULT_LANGUAGE = "ko";
        public static final String DEFAULT_COUNTRY = "KR";
    }

    // =================================================================
    // 정규표현식 패턴
    // =================================================================

    public static final class Patterns {
        private Patterns() {}

        public static final String EMAIL_PATTERN =
                "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        public static final String PHONE_PATTERN =
                "^(\\+82|0)?1[0-9]{1}-?[0-9]{3,4}-?[0-9]{4}$";
        public static final String PASSWORD_PATTERN =
                "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        public static final String USERNAME_PATTERN =
                "^[a-zA-Z0-9._-]{3,20}$";
        public static final String ALPHANUMERIC_PATTERN =
                "^[a-zA-Z0-9]+$";
        public static final String UUID_PATTERN =
                "^[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}$";
    }

    // =================================================================
    // 환경 및 프로필 관련
    // =================================================================

    public static final class Profiles {
        private Profiles() {}

        public static final String LOCAL = "local";
        public static final String DEVELOPMENT = "dev";
        public static final String STAGING = "staging";
        public static final String PRODUCTION = "prod";
        public static final String TEST = "test";
    }

    // =================================================================
    // 유틸리티 메서드들
    // =================================================================

    /**
     * 현재 API 버전을 반환
     *
     * @return API 버전 문자열
     */
    public static String getCurrentApiVersion() {
        return Path.API_VERSION;
    }

    /**
     * 도메인별 기본 경로 생성
     *
     * @param domain 도메인 이름
     * @return 전체 경로
     */
    public static String buildDomainPath(String domain) {
        return Path.API_BASE_PATH + "/" + domain.toLowerCase();
    }

    /**
     * Bearer 토큰 헤더 값 생성
     *
     * @param token JWT 토큰
     * @return Bearer 형태의 헤더 값
     */
    public static String createBearerToken(String token) {
        return Headers.BEARER_PREFIX + token;
    }

    /**
     * 페이지 사이즈 검증 및 조정
     *
     * @param size 요청된 페이지 사이즈
     * @return 검증된 페이지 사이즈
     */
    public static int validatePageSize(int size) {
        if (size < Pagination.MIN_PAGE_SIZE) {
            return Pagination.DEFAULT_PAGE_SIZE;
        }
        return Math.min(size, Pagination.MAX_PAGE_SIZE);
    }
}
