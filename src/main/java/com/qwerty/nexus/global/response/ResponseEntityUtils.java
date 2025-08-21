package com.qwerty.nexus.global.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseEntityUtils {
    /**
     * 커스텀 성공 상태 코드 지원 (데이터 포함)
     */
    public static <T> ResponseEntity<ApiResponse<T>> toResponseEntity(
            Result<T> result,
            HttpStatus successStatus) {
        return switch(result) {
            case Result.Success<T> success ->
                    ResponseEntity.status(successStatus)
                            .body(ApiResponse.success(success.message(), success.data()));
            case Result.Failure<T> failure ->
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(ApiResponse.error(failure.message(), failure.errorCode()));
        };
    }

    /**
     * 커스텀 성공 상태 코드 지원 (데이터 없음)
     */
    public static <T> ResponseEntity<ApiResponse<Void>> toResponseEntityVoid(
            Result<T> result,
            HttpStatus successStatus) {
        return switch(result) {
            case Result.Success<T> success ->
                    ResponseEntity.status(successStatus)
                            .body(ApiResponse.success(success.message()));
            case Result.Failure<T> failure ->
                    ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(ApiResponse.error(failure.message(), failure.errorCode()));
        };
    }
}
