package com.qwerty.nexus.global.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

/***
 * 상속용 DTO
 */
@Getter
@Setter
public class BaseResponseDTO {
    // 각 테이블마다 들어가 있는 공통 컬럼
    private OffsetDateTime createdAt;
    private String createdBy;
    private OffsetDateTime updatedAt;
    private String updatedBy;
    private String isDel;
}
