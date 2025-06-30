package com.qwerty.nexus.global.response;

/**
 * Service 에서 Controller 로 결과를 반환할 용도로 사용하는 공통 Result interface
 * @param <T>
 */
public sealed interface Result<T> permits Result.Success, Result.Failure {

    /**
     * 성공
     * @param data
     * @param message
     * @param <T>
     */
    record Success<T>(T data, String message) implements Result<T> {
        public static <T> Success<T> of(T data, String message) {
            return new Success<>(data, message);
        }

        public static <T> Success<T> of(T data) {
            return new Success<>(data, "성공");
        }
    }

    /**
     * 실패
     * @param message
     * @param errorCode
     * @param cause
     * @param <T>
     */
    record Failure<T>(String message, String errorCode, Exception cause) implements Result<T> {
        public static <T> Failure<T> of(String message, String errorCode) {
            return new Failure<>(message, errorCode, null);
        }

        public static <T> Failure<T> of(String message, String errorCode, Exception cause) {
            return new Failure<>(message, errorCode, cause);
        }
    }
}
