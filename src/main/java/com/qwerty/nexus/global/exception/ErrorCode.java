package com.qwerty.nexus.global.exception;

import org.springframework.http.HttpStatus;

/**
 *
 */
public enum ErrorCode {

    // 클로드에서 그냥 복붙한거임. 추후 정리할건 정리해야 할듯.

    // =================================================================
    // 시스템 공통 에러 (SYS)
    // =================================================================
    INTERNAL_ERROR("SYS001", "내부 서버 오류가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_REQUEST("SYS002", "잘못된 요청입니다", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED("SYS003", "인증이 필요합니다", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("SYS004", "접근 권한이 없습니다", HttpStatus.FORBIDDEN),
    NOT_FOUND("SYS005", "요청한 리소스를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    METHOD_NOT_ALLOWED("SYS006", "허용되지 않은 HTTP 메서드입니다", HttpStatus.METHOD_NOT_ALLOWED),
    CONFLICT("SYS007", "요청이 현재 서버 상태와 충돌합니다", HttpStatus.CONFLICT),
    TOO_MANY_REQUESTS("SYS008", "너무 많은 요청이 발생했습니다. 잠시 후 다시 시도해주세요", HttpStatus.TOO_MANY_REQUESTS),

    // =================================================================
    // 입력 검증 에러 (VAL)
    // =================================================================
    VALIDATION_ERROR("VAL001", "입력값 검증에 실패했습니다", HttpStatus.BAD_REQUEST),
    MISSING_REQUIRED_FIELD("VAL002", "필수 입력값이 누락되었습니다", HttpStatus.BAD_REQUEST),
    INVALID_FORMAT("VAL003", "입력값 형식이 올바르지 않습니다", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL_FORMAT("VAL004", "이메일 형식이 올바르지 않습니다", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD_FORMAT("VAL005", "비밀번호 형식이 올바르지 않습니다", HttpStatus.BAD_REQUEST),
    INVALID_PHONE_FORMAT("VAL006", "전화번호 형식이 올바르지 않습니다", HttpStatus.BAD_REQUEST),
    INVALID_DATE_FORMAT("VAL007", "날짜 형식이 올바르지 않습니다", HttpStatus.BAD_REQUEST),
    VALUE_OUT_OF_RANGE("VAL008", "입력값이 허용 범위를 벗어났습니다", HttpStatus.BAD_REQUEST),

    // =================================================================
    // 인증/인가 에러 (AUTH)
    // =================================================================
    INVALID_CREDENTIALS("AUTH001", "이메일 또는 비밀번호가 올바르지 않습니다", HttpStatus.UNAUTHORIZED),
    EXPIRED_TOKEN("AUTH002", "토큰이 만료되었습니다", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN("AUTH003", "유효하지 않은 토큰입니다", HttpStatus.UNAUTHORIZED),
    TOKEN_NOT_FOUND("AUTH004", "토큰이 제공되지 않았습니다", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_EXPIRED("AUTH005", "리프레시 토큰이 만료되었습니다", HttpStatus.UNAUTHORIZED),
    ACCOUNT_LOCKED("AUTH006", "계정이 잠겨있습니다", HttpStatus.FORBIDDEN),
    ACCOUNT_DISABLED("AUTH007", "비활성화된 계정입니다", HttpStatus.FORBIDDEN),
    INSUFFICIENT_PERMISSIONS("AUTH008", "권한이 부족합니다", HttpStatus.FORBIDDEN),

    // =================================================================
    // 사용자 관련 에러 (USER)
    // =================================================================
    USER_NOT_FOUND("USER001", "사용자를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS("USER002", "이미 존재하는 사용자입니다", HttpStatus.CONFLICT),
    DUPLICATE_EMAIL("USER003", "이미 사용 중인 이메일입니다", HttpStatus.CONFLICT),
    DUPLICATE_USERNAME("USER004", "이미 사용 중인 사용자명입니다", HttpStatus.CONFLICT),
    INVALID_USER_STATUS("USER005", "사용자 상태가 올바르지 않습니다", HttpStatus.BAD_REQUEST),
    USER_REGISTRATION_FAILED("USER006", "사용자 등록에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    PASSWORD_MISMATCH("USER007", "현재 비밀번호가 일치하지 않습니다", HttpStatus.BAD_REQUEST),
    WEAK_PASSWORD("USER008", "비밀번호가 너무 간단합니다", HttpStatus.BAD_REQUEST),

    // =================================================================
    // 주문 관련 에러 (ORDER)
    // =================================================================
    ORDER_NOT_FOUND("ORDER001", "주문을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    ORDER_ALREADY_CANCELLED("ORDER002", "이미 취소된 주문입니다", HttpStatus.CONFLICT),
    ORDER_CANNOT_BE_CANCELLED("ORDER003", "취소할 수 없는 주문 상태입니다", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_STOCK("ORDER004", "재고가 부족합니다", HttpStatus.CONFLICT),
    INVALID_ORDER_STATUS("ORDER005", "주문 상태가 올바르지 않습니다", HttpStatus.BAD_REQUEST),
    ORDER_AMOUNT_MISMATCH("ORDER006", "주문 금액이 일치하지 않습니다", HttpStatus.BAD_REQUEST),

    // =================================================================
    // 결제 관련 에러 (PAYMENT)
    // =================================================================
    PAYMENT_FAILED("PAY001", "결제에 실패했습니다", HttpStatus.BAD_REQUEST),
    PAYMENT_CANCELLED("PAY002", "결제가 취소되었습니다", HttpStatus.CONFLICT),
    INVALID_PAYMENT_METHOD("PAY003", "유효하지 않은 결제 수단입니다", HttpStatus.BAD_REQUEST),
    PAYMENT_AMOUNT_MISMATCH("PAY004", "결제 금액이 일치하지 않습니다", HttpStatus.BAD_REQUEST),
    REFUND_FAILED("PAY005", "환불 처리에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),

    // =================================================================
    // 파일 관련 에러 (FILE)
    // =================================================================
    FILE_NOT_FOUND("FILE001", "파일을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    FILE_UPLOAD_FAILED("FILE002", "파일 업로드에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_SIZE_EXCEEDED("FILE003", "파일 크기가 제한을 초과했습니다", HttpStatus.BAD_REQUEST),
    INVALID_FILE_FORMAT("FILE004", "지원하지 않는 파일 형식입니다", HttpStatus.BAD_REQUEST),
    FILE_PROCESSING_FAILED("FILE005", "파일 처리에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),

    // =================================================================
    // 외부 API 연동 에러 (EXT)
    // =================================================================
    EXTERNAL_API_ERROR("EXT001", "외부 서비스 연동 중 오류가 발생했습니다", HttpStatus.SERVICE_UNAVAILABLE),
    EXTERNAL_API_TIMEOUT("EXT002", "외부 서비스 응답 시간이 초과되었습니다", HttpStatus.REQUEST_TIMEOUT),
    EXTERNAL_API_RATE_LIMIT("EXT003", "외부 서비스 호출 한도를 초과했습니다", HttpStatus.TOO_MANY_REQUESTS),

    // =================================================================
    // 데이터베이스 관련 에러 (DB)
    // =================================================================
    DATABASE_CONNECTION_ERROR("DB001", "데이터베이스 연결에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR),
    DATA_INTEGRITY_VIOLATION("DB002", "데이터 무결성 제약 조건을 위반했습니다", HttpStatus.CONFLICT),
    DUPLICATE_KEY_ERROR("DB003", "중복된 키 값입니다", HttpStatus.CONFLICT),
    OPTIMISTIC_LOCK_ERROR("DB004", "동시 수정으로 인한 충돌이 발생했습니다", HttpStatus.CONFLICT);

    // =================================================================
    // Enum 필드 및 생성자
    // =================================================================

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    // =================================================================
    // Getter 메서드들
    // =================================================================

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getStatus() {
        return httpStatus.value();
    }

    // =================================================================
    // 유틸리티 메서드들
    // =================================================================

    /**
     * 에러 코드 문자열로 ErrorCode enum 찾기
     *
     * @param code 에러 코드 문자열
     * @return ErrorCode enum (없으면 INTERNAL_ERROR)
     */
    public static ErrorCode fromCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return INTERNAL_ERROR;
        }

        for (ErrorCode errorCode : values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode;
            }
        }
        return INTERNAL_ERROR;
    }

    /**
     * HTTP 상태코드로 기본 ErrorCode 찾기
     *
     * @param httpStatus HTTP 상태코드
     * @return ErrorCode enum
     */
    public static ErrorCode fromHttpStatus(HttpStatus httpStatus) {
        return switch (httpStatus) {
            case BAD_REQUEST -> INVALID_REQUEST;
            case UNAUTHORIZED -> UNAUTHORIZED;
            case FORBIDDEN -> FORBIDDEN;
            case NOT_FOUND -> NOT_FOUND;
            case METHOD_NOT_ALLOWED -> METHOD_NOT_ALLOWED;
            case CONFLICT -> CONFLICT;
            case TOO_MANY_REQUESTS -> TOO_MANY_REQUESTS;
            default -> INTERNAL_ERROR;
        };
    }

    /**
     * 도메인별 에러 코드인지 확인
     *
     * @param domain 도메인 PREFIX (예: "USER", "ORDER")
     * @return 해당 도메인 에러 여부
     */
    public boolean isDomainError(String domain) {
        return this.code.startsWith(domain);
    }

    /**
     * 클라이언트 에러인지 확인 (4xx)
     *
     * @return 클라이언트 에러 여부
     */
    public boolean isClientError() {
        return httpStatus.is4xxClientError();
    }

    /**
     * 서버 에러인지 확인 (5xx)
     *
     * @return 서버 에러 여부
     */
    public boolean isServerError() {
        return httpStatus.is5xxServerError();
    }

    /**
     * 로깅용 문자열 표현
     *
     * @return 로깅에 적합한 형태의 문자열
     */
    public String toLogString() {
        return String.format("[%s] %s (HTTP %d)", code, message, httpStatus.value());
    }

    @Override
    public String toString() {
        return String.format("ErrorCode{code='%s', message='%s', httpStatus=%s}",
                code, message, httpStatus);
    }
}
