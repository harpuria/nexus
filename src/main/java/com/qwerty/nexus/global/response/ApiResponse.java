package com.qwerty.nexus.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.qwerty.nexus.global.exception.ErrorCode;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.Objects;

/**
 * RestController 에서 응답용으로 쓸 공통 API Response
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record ApiResponse<T>(
        boolean success,
        String message,
        T data,
        String errorCode,
        OffsetDateTime timestamp
) {
    // =================================================================
    // 성공 응답 생성 메서드들
    // =================================================================

    /**
     * 성공여부, 메시지, 데이터, 시간 모두 반환하는 성공 응답 (default)
     * @param message 성공 시 메시지
     * @param data 응답 데이터
     * @param <T> 데이터 타입
     * @return 성공 응답
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<T>(
          true, Objects.requireNonNull(message, "메시지가 존재하지 않습니다."), data, null, OffsetDateTime.now()
        );
    }

    /**
     * 기본 메시지와 데이터를 포함한 성공 응답
     * @param data 응답 뎅터
     * @param <T> 데이터 타입
     * @return 성공 응답
     */
    public static <T> ApiResponse<T> success(T data){
        return success("요청이 성공적으로 처리되었습니다.", data);
    }

    /**
     * 메시지만 포함한 성공 응답
     * @param message 성공 시 메시지
     * @return 성공 응답
     */
    public static ApiResponse<Void> success(String message){
        return success(message, null);
    }

    // =================================================================
    // 실패 응답 생성 메서드들
    // =================================================================

    /**
     * 성공여부, 메시지, 에러코드, 시간 모두 반환하는 실패 응답 (default)
     * @param message 실패 시 에러 메시지
     * @param errorCode 에러 코드
     * @param <T> 데이터 타입
     * @return 실패 응답
     */
    public static <T> ApiResponse<T> error(String message, String errorCode) {
        return new ApiResponse<T>(
                false, Objects.requireNonNull(message, "메시지가 존재하지 않습니다."), null, Objects.requireNonNull(errorCode, "에러코드가 존재하지 않습니다."), OffsetDateTime.now()
        );
    }

    /**
     * 메시지만 포함한 실패 응답
     * @param message 실패 시 에러 메시지
     * @param <T> 데이터 타입
     * @return 실패 응답
     */
    public static <T> ApiResponse<T> error(String message){
        return error(message, ErrorCode.INTERNAL_ERROR.getMessage());
    }

    /**
     * ErrorCode enum 을 사용한 실패 응답
     * @param errorCode 에러 코드 enum
     * @param <T> 데이터 타입
     * @return 실패 응답
     */
    public static <T> ApiResponse<T> error(ErrorCode errorCode){
        return error(errorCode.getMessage(), errorCode.getCode());
    }

    /**
     * 커스텀 메시지와 ErrorCode enum 을 사용한 실패 응답
     * @param errorCode 에러 코드 enum
     * @param message 실패 응답 메시지
     * @param <T> 데이터 타입
     * @return 실패 응답
     */
    public static <T> ApiResponse<T> error(ErrorCode errorCode, String message){
        return error(message, errorCode.getCode());
    }

    // =================================================================
    // 기타 메서드들
    // =================================================================

    public boolean isSuccess(){
        return this.success;
    }

    public boolean isError(){
        return !this.success;
    }

    public boolean hasData(){
        return this.data != null;
    }

    public boolean hasErrorCode() {
        return this.errorCode != null && !this.errorCode.trim().isEmpty();
    }

    // =================================================================
    // 유틸리티 메서드들
    // =================================================================

    /**
     * 다른 타입의 ApiResponse로 변환. 메타데이터는 유지하되 데이터만 변경할 수 있음.
     * @param newData 새로운 데이터
     * @param <U> 새로운 데이터 타입
     * @return 변환된 ApiResponse
     */
    public <U> ApiResponse<U> withData(U newData){
        return new ApiResponse<>(this.success, this.message, newData, this.errorCode, this.timestamp);
    }

    /**
     * 메시지 변경
     * @param newMessage 새로운 메시지
     * @return 메시지가 변경된 ApiResponse
     */
    public ApiResponse<T> withMessage(String newMessage){
        return new ApiResponse<>(this.success, newMessage, this.data, this.errorCode, this.timestamp);
    }

}
